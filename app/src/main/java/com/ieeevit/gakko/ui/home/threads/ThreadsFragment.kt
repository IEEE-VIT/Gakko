package com.ieeevit.gakko.ui.home.threads

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.ieeevit.gakko.R
import com.ieeevit.gakko.adapters.ThreadsDisplayAdapter
import com.ieeevit.gakko.data.models.Threads
import com.ieeevit.gakko.data.models.User
import com.ieeevit.gakko.internal.Utils
import com.ieeevit.gakko.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.threads_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class ThreadsFragment : ScopedFragment(), KodeinAware {
    override val kodein: Kodein by closestKodein()
    private val threadsViewModelFactory: ThreadsViewModelFactory by instance()
    private lateinit var threadsViewModel: ThreadsViewModel
    private val utils: Utils by instance()
    private var threadList = mutableListOf<Threads>()
    private var studentsList = mutableListOf<String>()
    private var teachersList = mutableListOf<String>()
    private var phoneNumberList = mutableListOf<String>()
    private lateinit var adapter: ThreadsDisplayAdapter
    private var map: HashMap<String, User> = HashMap()
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.threads_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        threadsViewModel = ViewModelProvider(this, threadsViewModelFactory).get(ThreadsViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        threads_progress_bar.visibility = View.VISIBLE
        getThreadsList(utils.retrieveCurrentClassroom()!!)

        thread_edit_text.setOnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2

            if(event.action == MotionEvent.ACTION_UP) {
                if(event.rawX >= (thread_edit_text.right - thread_edit_text.compoundDrawables[DRAWABLE_RIGHT].bounds.width())) {
                    if(thread_edit_text.text.toString().isNotEmpty()) {
                        postThread(thread_edit_text.text.toString())
                        thread_edit_text.text = null
                    }
                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener false
        }
    }

    private fun postThread(threadBody: String) = launch {
        val thread: Threads = Threads(threadBody, emptyMap(), utils.retrieveMobile() ?: "", System.currentTimeMillis(), "")
        threadsViewModel.postThread(thread,  utils.retrieveCurrentClassroom()!!)
    }

    private fun getThreadsList(threadId: String) = launch {
        threadList.clear()
        threadsViewModel.getThreads(threadId)
        threadsViewModel.threads.observe(viewLifecycleOwner, Observer {
            threadList = it as MutableList<Threads>
            if(threadList.isNotEmpty()) {
                getThreadUser(threadList)
            } else {
                threads_recycler_view.visibility = View.GONE
                threads_default_image_background.visibility = View.VISIBLE
                threads_default_text_view.visibility = View.VISIBLE
                threads_progress_bar.visibility = View.GONE
            }
        })
    }


    private fun getThreadUser(threadList: List<Threads>) {
        for(thread: Threads in  threadList) {
            phoneNumberList.add(thread.user)
        }
        getThreadUsers(phoneNumberList)
    }

    private fun getThreadUsers(userIds: List<String>) {
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
                        getThreadClassroom(utils.retrieveCurrentClassroom() ?: " ")
                    }
                }
            }
            databaseReference.addValueEventListener(valueEventListener)
        }
    }

    private fun getThreadClassroom(classroomId: String) = launch {
        teachersList.clear()
        studentsList.clear()
        threadsViewModel.getThreadClassroom(classroomId)
        threadsViewModel.threadClassroom.observe(viewLifecycleOwner, Observer {
            if(it != null){
                teachersList = it.teachers as MutableList<String>
                updateUI(threadList, teachersList, map)
            }

        })
    }

    private fun updateUI(threadList: List<Threads>, teachersList: List<String>, map: HashMap<String, User>) {
        if(threadList.isEmpty()) {
            threads_recycler_view.visibility = View.GONE
            threads_default_image_background.visibility = View.VISIBLE
            threads_default_text_view.visibility = View.VISIBLE
            threads_progress_bar.visibility = View.GONE
        }
        else {
            threads_recycler_view.visibility = View.VISIBLE
            threads_default_image_background.visibility = View.GONE
            threads_default_text_view.visibility = View.GONE
            adapter = ThreadsDisplayAdapter(threadList, teachersList, map, utils)
            threads_recycler_view.layoutManager = LinearLayoutManager(context)
            threads_recycler_view.adapter = adapter
            threads_progress_bar.visibility = View.GONE
        }
    }
}
