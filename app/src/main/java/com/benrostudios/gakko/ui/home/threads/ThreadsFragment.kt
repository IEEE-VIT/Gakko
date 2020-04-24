package com.benrostudios.gakko.ui.home.threads

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.benrostudios.gakko.R
import com.benrostudios.gakko.data.repository.ThreadsRepository
import com.benrostudios.gakko.ui.base.ScopedFragment
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class ThreadsFragment : ScopedFragment(), KodeinAware{

    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: ThreadsViewModelFactory by instance()
    private lateinit var viewModel: ThreadsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.threads_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ThreadsViewModel::class.java)
        getUserResponse()
    }

    private fun getUserResponse() = launch {
        viewModel.getThreads("-sihshidv213")
        viewModel.threads.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT)
                .show()
        })
    }

}
