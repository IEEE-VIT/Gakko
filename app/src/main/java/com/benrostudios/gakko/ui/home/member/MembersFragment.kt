package com.benrostudios.gakko.ui.home.member

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.benrostudios.gakko.R

class MembersFragment : Fragment() {

    companion object {
        fun newInstance() = MembersFragment()
    }

    private lateinit var viewModel: MembersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.members_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MembersViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
