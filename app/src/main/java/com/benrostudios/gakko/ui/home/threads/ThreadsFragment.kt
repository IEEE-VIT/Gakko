package com.benrostudios.gakko.ui.home.threads

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.benrostudios.gakko.R

class ThreadsFragment : Fragment() {

    companion object {
        fun newInstance() = ThreadsFragment()
    }

    private lateinit var viewModel: ThreadsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.threads_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ThreadsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
