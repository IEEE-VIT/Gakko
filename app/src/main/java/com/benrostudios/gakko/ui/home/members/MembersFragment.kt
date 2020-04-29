package com.benrostudios.gakko.ui.home.members

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.benrostudios.gakko.R
import com.benrostudios.gakko.adapters.MembersDisplayAdapter
import com.benrostudios.gakko.data.models.Classroom
import com.benrostudios.gakko.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.members_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class MembersFragment : ScopedFragment(),KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: MembersViewModelFactory by instance()
    private lateinit var classroom: Classroom
    private lateinit var studentsAdapter: MembersDisplayAdapter
    private lateinit var teachersAdapter: MembersDisplayAdapter


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
        viewModel = ViewModelProvider(this,viewModelFactory).get(MembersViewModel::class.java)
        fetchClassroom()
        emmebrs_students_recycler.layoutManager = LinearLayoutManager(context)
        members_teacher_recycler.layoutManager = LinearLayoutManager(context)
    }

    private fun fetchClassroom() = launch{
        viewModel.classroom.observe(viewLifecycleOwner, Observer {
            classroom = it
            getStudents()
            getTeachers()
        })

    }

    private fun getStudents() =launch {
        viewModel.getStudentsList(classroom.students)
        viewModel.studentsList.observe(viewLifecycleOwner, Observer {
            studentsAdapter = MembersDisplayAdapter(it)
            emmebrs_students_recycler.adapter = studentsAdapter
        })
    }
    private fun getTeachers() = launch {
        viewModel.getTeachersList(classroom.teachers)
        viewModel.teachersList.observe(viewLifecycleOwner, Observer {
            teachersAdapter = MembersDisplayAdapter(it)
            members_teacher_recycler.adapter = teachersAdapter
        })

    }

}
