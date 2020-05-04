package com.benrostudios.gakko.ui.home.comments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.benrostudios.gakko.R
import com.benrostudios.gakko.adapters.CommentsDisplayAdapter
import com.benrostudios.gakko.data.models.Comments
import com.benrostudios.gakko.data.models.Threads
import com.benrostudios.gakko.data.models.User
import com.benrostudios.gakko.internal.GlideApp
import com.benrostudios.gakko.internal.Utils
import com.benrostudios.gakko.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.comment_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class CommentFragment : ScopedFragment(), KodeinAware {
    private var teacherList = mutableListOf<String>()
    private lateinit var currentThread: Threads
    private lateinit var threadUser: User
    override val kodein: Kodein by closestKodein()
    private lateinit var commentsViewModel: CommentViewModel
    private val viewModelFactory: CommentViewModelFactory by instance()
    private val utils: Utils by instance()
    private val map: HashMap<String, User> = HashMap()
    private lateinit var adapter: CommentsDisplayAdapter
    @SuppressLint("SimpleDateFormat")
    private var dateFormatter: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.comment_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        commentsViewModel = ViewModelProvider(this, viewModelFactory).get(CommentViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getClassroom(utils.retrieveCurrentClassroom() ?: "")

        comments_edit_text.setOnTouchListener { v, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3

            if(event.action == MotionEvent.ACTION_UP) {
                if(event.rawX >= (comments_edit_text.right - comments_edit_text.compoundDrawables[DRAWABLE_RIGHT].bounds.width())) {
                    if(comments_edit_text.text.toString().isNotEmpty()) {
                        postComment(comments_edit_text.text.toString())
                        comments_edit_text.text = null
                    }
                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener false
        }
    }

    private fun postComment(commentBody: String) = launch {
        val comment: Comments = Comments(commentBody, System.currentTimeMillis(), utils.retrieveMobile()!!.toLong())
        commentsViewModel.postComment(comment, utils.retrieveCurrentClassroom() ?: "", "ckdnkdndjksn")
    }

    private fun getClassroom(classroomId: String) = launch {
        teacherList.clear()
        commentsViewModel.commentsClassroom.observe(viewLifecycleOwner, Observer {
            teacherList = it.teachers as MutableList<String>
            getThread(classroomId, "hhbchbdb")
        })
    }

    private fun getThread(threadId: String, specificThreadId: String) = launch {
        commentsViewModel.getSpecificThread(threadId, specificThreadId)
        commentsViewModel.specificThread.observe(viewLifecycleOwner, Observer {
            currentThread = it
            getThreadUser(it.user, it.comments)
        })
    }

    private fun getThreadUser(userId: String, comments: List<Comments>) = launch {
        commentsViewModel.getCommentUser(userId)
        commentsViewModel.commenter.observe(viewLifecycleOwner, Observer {
            threadUser = it
            getCommenters(comments)
        })
    }

    private fun getCommenters(comments: List<Comments>) = launch {
        var counter: Int = 0
        for(comment: Comments in comments) {
            commentsViewModel.getCommentUser(comment.user.toString())
            commentsViewModel.commenter.observe(viewLifecycleOwner, Observer {
                map[it.id] = it
                counter++
            })
            if(counter == comments.size) {
                updateUI()
            }
        }
    }

    private fun updateUI() {
        GlideApp.with(requireContext())
            .load(threadUser.profileImage)
            .centerCrop()
            .placeholder(R.drawable.ic_defualt_profile_pic)
            .into(comments_fragment_profile_picture)
        comments_fragment_person_name.text = threadUser.name
        comments_fragment_person_designation.text = if(teacherList.contains(threadUser.id)) "Teacher" else "Student"
        comments_fragment_person_day.text = dateFormatter.format(Date(currentThread.timestamp))
        comments_fragment_thread_body.text = currentThread.body
        adapter = CommentsDisplayAdapter(currentThread.comments, map)
        comments_recycler_view.adapter = adapter
        comments_recycler_view.layoutManager = LinearLayoutManager(requireContext())
    }

}