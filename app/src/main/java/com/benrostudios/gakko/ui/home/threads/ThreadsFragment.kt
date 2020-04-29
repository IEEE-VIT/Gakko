package com.benrostudios.gakko.ui.home.threads

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.benrostudios.gakko.R
import com.benrostudios.gakko.adapters.ThreadsDisplayAdapter
import com.benrostudios.gakko.data.models.Classroom
import com.benrostudios.gakko.data.models.Threads
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
    private var threadList = listOf<Threads>()
    private var threadClassroom = Classroom()
    private lateinit var adapter: ThreadsDisplayAdapter

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
    }

    private fun getThreadsList(threadId: String) = launch {
        threadsViewModel.getThreads(threadId)
        threadsViewModel.threads.observe(viewLifecycleOwner, Observer {
            threadList = it
            getThreadClassroom(utils.retrieveCurrentClassroom() ?: "")
        })
    }

    private fun getThreadClassroom(classroomId: String) = launch {
        threadsViewModel.getThreadClassroom(classroomId)
        threadsViewModel.threadClassroom.observe(viewLifecycleOwner, Observer {
            updateUI(threadList, it, threadsViewModel)
        })
    }

    private fun updateUI(threadList: List<Threads>, threadClassroom: Classroom, threadsViewModel: ThreadsViewModel) {
        if(threadList.isEmpty()) {
            threads_recycler_view.visibility = View.GONE
            threads_default_image_background.visibility = View.VISIBLE
            threads_default_text_view.visibility = View.VISIBLE
        }
        else {
            threads_recycler_view.visibility = View.VISIBLE
            threads_default_image_background.visibility = View.GONE
            threads_default_text_view.visibility = View.GONE
            adapter = ThreadsDisplayAdapter(threadList, threadClassroom)
            threads_recycler_view.layoutManager = LinearLayoutManager(context)
            threads_recycler_view.adapter = adapter
        }
    }
}
