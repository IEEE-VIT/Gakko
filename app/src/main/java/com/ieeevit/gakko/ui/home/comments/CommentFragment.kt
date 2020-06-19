package com.ieeevit.gakko.ui.home.comments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ieeevit.gakko.R
import com.ieeevit.gakko.adapters.CommentsDisplayAdapter
import com.ieeevit.gakko.data.models.Comments
import com.ieeevit.gakko.data.models.Threads
import com.ieeevit.gakko.data.models.User
import com.ieeevit.gakko.internal.AvatarConstants
import com.ieeevit.gakko.internal.AvatarGenerator
import com.ieeevit.gakko.internal.GlideApp
import com.ieeevit.gakko.internal.Utils
import com.ieeevit.gakko.ui.base.ScopedFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.comment_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class CommentFragment : ScopedFragment(), KodeinAware {
    private var teacherList = mutableListOf<String>()
    private lateinit var currentThread: Threads
    private lateinit var threadUser: User
    override val kodein: Kodein by closestKodein()
    private lateinit var commentsViewModel: CommentViewModel
    private val viewModelFactory: CommentViewModelFactory by instance()
    private val utils: Utils by instance()
    private var phoneNumberList = mutableListOf<String>()
    private val map: HashMap<String, User> = HashMap()
    private lateinit var adapter: CommentsDisplayAdapter
    private lateinit var databaseReference: DatabaseReference
    @SuppressLint("SimpleDateFormat")
    private var dateFormatter: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
    private lateinit var navController: NavController


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

        navController = view.findNavController()
        comments_progress_bar.visibility = View.VISIBLE
        getClassroom(utils.retrieveCurrentClassroom() ?: "")

        back_to_threads_arrow_image.setOnClickListener {
            navController.popBackStack()
        }

        comments_edit_text.setOnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
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
        val comment: Comments = Comments(commentBody, System.currentTimeMillis(), utils.retrieveMobile()!!)
        commentsViewModel.postComment(comment, utils.retrieveCurrentClassroom() ?: "", utils.retrieveThread()!!)
    }

    private fun getClassroom(classroomId: String) = launch {
        teacherList.clear()
        commentsViewModel.commentsClassroom.observe(viewLifecycleOwner, Observer {
            teacherList = it.teachers as MutableList<String>
            getThread(classroomId, utils.retrieveThread()!!)
        })
    }

    private fun getThread(threadId: String, specificThreadId: String) = launch {
        commentsViewModel.getSpecificThread(threadId, specificThreadId)
        commentsViewModel.specificThread.observe(viewLifecycleOwner, Observer {
            currentThread = it
            val commitsList: List<Comments> = ArrayList<Comments>(it.comments.values)
            getThreadUser(it.user, commitsList)
        })
    }

    private fun getThreadUser(userId: String, comments: List<Comments>) = launch {
        commentsViewModel.getThreadUser(userId)
        commentsViewModel.threadUser.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                threadUser = it

                if(!comments.isNullOrEmpty()) {
                    getCommentUser(comments)
                    val options: RequestOptions = RequestOptions()
                        .error(AvatarGenerator.avatarImage(requireContext(), 200, AvatarConstants.CIRCLE, threadUser.name))
                        .placeholder(AvatarGenerator.avatarImage(requireContext(), 200, AvatarConstants.CIRCLE, threadUser.name))
                        .circleCrop()

                    Glide.with(requireContext())
                        .load(threadUser.profileImage)
                        .apply(options)
                        .placeholder(AvatarGenerator.avatarImage(requireContext(), 200, AvatarConstants.CIRCLE, threadUser.name))
                        .into(comments_fragment_profile_picture)

                    comments_fragment_person_name.text = threadUser.name
                    comments_fragment_person_designation.text = if(teacherList.contains(threadUser.id)) "Teacher" else "Student"
                    comments_fragment_person_day.text = dateFormatter.format(Date(currentThread.timestamp))
                    comments_fragment_thread_body.text = currentThread.body
                    comments_progress_bar.visibility = View.GONE

                }
                else {

                    val options: RequestOptions = RequestOptions()
                        .error(AvatarGenerator.avatarImage(requireContext(), 200, AvatarConstants.CIRCLE, threadUser.name))
                        .placeholder(AvatarGenerator.avatarImage(requireContext(), 200, AvatarConstants.CIRCLE, threadUser.name))
                        .circleCrop()

                    Glide.with(requireContext())
                        .load(threadUser.profileImage)
                        .apply(options)
                        .placeholder(AvatarGenerator.avatarImage(requireContext(), 200, AvatarConstants.CIRCLE, threadUser.name))
                        .into(comments_fragment_profile_picture)

                    comments_fragment_person_name.text = threadUser.name
                    comments_fragment_person_designation.text = if(teacherList.contains(threadUser.id)) "Teacher" else "Student"
                    comments_fragment_person_day.text = dateFormatter.format(Date(currentThread.timestamp))
                    comments_fragment_thread_body.text = currentThread.body
                    comments_progress_bar.visibility = View.GONE

                }
            }
        })
    }

    private fun getCommentUser(comments: List<Comments>) {
        phoneNumberList.clear()
        for(comment: Comments in comments) {
            phoneNumberList.add(comment.user)
        }
        getCommenters(phoneNumberList)
    }

    private fun getCommenters(userIds: List<String>) {
        map.clear()
        var counter = 0
        for(userId: String in userIds) {
            databaseReference = FirebaseDatabase.getInstance().getReference("/users/$userId/")
            val valueEventListener: ValueEventListener = object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
                override fun onDataChange(p0: DataSnapshot) {
                    val user = p0.getValue(User::class.java)
                    map[user!!.id] = user
                    counter++

                    if(counter == userIds.size) {
                        updateUI()
                    }
                }
            }
            databaseReference.addValueEventListener(valueEventListener)
        }
    }

    private fun timeUtilization() {
        var i = 0

    }

    private fun updateUI() {
        adapter = CommentsDisplayAdapter(ArrayList<Comments>(currentThread.comments.values), map, utils)
        comments_recycler_view.adapter = adapter
        comments_recycler_view.layoutManager = LinearLayoutManager(requireContext())
        comments_progress_bar.visibility = View.GONE
    }

}
