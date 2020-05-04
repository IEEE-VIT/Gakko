package com.benrostudios.gakko.ui.home.notifications

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.benrostudios.gakko.R
import com.benrostudios.gakko.adapters.RequestsAdapter
import com.benrostudios.gakko.internal.Utils
import com.benrostudios.gakko.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.notifications_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class Notifications : ScopedFragment(),KodeinAware {
    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: NotificationsViewModelFactory by instance()
    private val utils: Utils by instance()

    companion object {
        fun newInstance() = Notifications()
    }

    private lateinit var viewModel: NotificationsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.notifications_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this,viewModelFactory).get(NotificationsViewModel::class.java)
        request_recycler.layoutManager = LinearLayoutManager(context)
        fetchRequestsList()
        listenRequestsList()

    }

    private fun fetchRequestsList() = launch{
        viewModel.fetchRequestList(utils.retrieveTeachersList()?.toList() ?: emptyList())
        Log.d("melo",utils.retrieveTeachersList().toString())
    }

    private fun listenRequestsList() = launch {
        viewModel.requestList.observe(viewLifecycleOwner, Observer {
            request_recycler.adapter = RequestsAdapter(it, object: RequestsAdapter.ClickListener{
                override fun acceptTrigger(posistion: Int) {
                    acceptRequest(it[posistion].classroomId,it[posistion].phone)
                }
                override fun declineTrigger(posistion: Int) {
                    declineRequest(it[posistion].classroomId,it[posistion].phone)
                }
            })
        })
    }

    private fun declineRequest(classroomId: String, person: String) = launch {
        viewModel.declineRequests(classroomId,person)

    }

    private fun acceptRequest(clasroomId: String, person: String) = launch {
        viewModel.acceptRequest(clasroomId,person)
    }

}
