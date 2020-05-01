package com.benrostudios.gakko.ui.home.threads

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.benrostudios.gakko.R
import com.benrostudios.gakko.adapters.ThreadsDisplayAdapter
import com.benrostudios.gakko.data.models.Threads
import com.benrostudios.gakko.data.models.User
import com.benrostudios.gakko.internal.Utils
import com.benrostudios.gakko.ui.base.ScopedFragment
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
    private lateinit var adapter: ThreadsDisplayAdapter
    private var map: HashMap<String, User> = HashMap()

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
        getThreadsList("-sihshidv213")

        thread_edit_text.setOnTouchListener { v, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3

            if(event.action == MotionEvent.ACTION_UP) {
                if(event.rawX >= (thread_edit_text.getRight() - thread_edit_text.compoundDrawables[DRAWABLE_RIGHT].bounds.width())) {
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
        val thread: Threads = Threads(threadBody, emptyList(), utils.retrieveMobile() ?: "", System.currentTimeMillis()/1000)
        threadsViewModel.postThread(thread,  "-sihshidv213")
    }

    private fun getThreadsList(threadId: String) = launch {
        threadList.clear()
        threadsViewModel.getThreads(threadId)
        threadsViewModel.threads.observe(viewLifecycleOwner, Observer {
            threadList = it as MutableList<Threads>
            getThreadUsers(threadList)
        })
    }

    private fun getThreadUsers(threadList: List<Threads>) = launch {
        map.clear()
        var counter = 0
        for(thread: Threads in threadList) {
            threadsViewModel.getThreadUser(thread.user)
            threadsViewModel.threadUser.observe(viewLifecycleOwner, Observer {
               if(it != null) {
                   map[it.id] = it
                   counter++
               }
                if(counter == threadList.size) {
                    getThreadClassroom(utils.retrieveCurrentClassroom()?:"")
                    Log.d("CurrentClass",utils.retrieveCurrentClassroom())
                }
            })
        }
    }

    private fun getThreadClassroom(classroomId: String) = launch {
        teachersList.clear()
        studentsList.clear()
        threadsViewModel.getThreadClassroom(classroomId)
        threadsViewModel.threadClassroom.observe(viewLifecycleOwner, Observer {
            teachersList = it.teachers as MutableList<String>
            studentsList = it.students as MutableList<String>
            updateUI(threadList, teachersList, map)
        })
    }

    private fun updateUI(threadList: List<Threads>, teachersList: List<String>, map: HashMap<String, User>) {
        if(threadList.isEmpty()) {
            threads_recycler_view.visibility = View.GONE
            threads_default_image_background.visibility = View.VISIBLE
            threads_default_text_view.visibility = View.VISIBLE
        }
        else {
            threads_recycler_view.visibility = View.VISIBLE
            threads_default_image_background.visibility = View.GONE
            threads_default_text_view.visibility = View.GONE
            adapter = ThreadsDisplayAdapter(threadList, teachersList, map)
            threads_recycler_view.layoutManager = LinearLayoutManager(context)
            threads_recycler_view.adapter = adapter
        }
    }
}
