package com.benrostudios.gakko.ui.home.notifications

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.benrostudios.gakko.R
import com.benrostudios.gakko.internal.Utils
import com.benrostudios.gakko.ui.base.ScopedFragment
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.allInstances
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
        teso()
        recieveo()
        //delo()
        addo()

    }

    private fun teso() = launch{
        viewModel.test(utils.retrieveTeachersList()?.toList() ?: emptyList())
        Log.d("melo",utils.retrieveTeachersList().toString())
    }

    private fun recieveo() = launch {
        viewModel.requestList.observe(viewLifecycleOwner, Observer {
            Log.d("Notifications",it.toString())
        })
    }

    private fun delo() = launch {
        viewModel.dello("1588248796933","+919999999969")
    }

    private fun addo() = launch {
        viewModel.accepto("1588248796933","+919999999969")
    }

}
