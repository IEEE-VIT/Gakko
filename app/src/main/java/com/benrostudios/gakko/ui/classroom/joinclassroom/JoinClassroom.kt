package com.benrostudios.gakko.ui.classroom.joinclassroom

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.benrostudios.gakko.R

class JoinClassroom : Fragment() {

    companion object {
        fun newInstance() = JoinClassroom()
    }

    private lateinit var viewModel: JoinClassroomViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.join_classroom_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(JoinClassroomViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
