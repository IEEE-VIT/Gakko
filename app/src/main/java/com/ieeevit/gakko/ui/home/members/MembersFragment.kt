package com.ieeevit.gakko.ui.home.members

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ieeevit.gakko.R
import com.ieeevit.gakko.adapters.MembersDisplayAdapter
import com.ieeevit.gakko.data.models.Classroom
import com.ieeevit.gakko.data.models.Members
import com.ieeevit.gakko.internal.Utils
import com.ieeevit.gakko.ui.base.ScopedFragment
import com.ieeevit.gakko.ui.classroom.joinclassroom.JoinClassroom
import com.ieeevit.gakko.ui.home.exitclassroom.ExitClassroom
import kotlinx.android.synthetic.main.members_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class MembersFragment : ScopedFragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: MembersViewModelFactory by instance()
    private val utils: Utils by instance()
    private var classroom: Classroom = Classroom()
    private lateinit var studentsAdapter: MembersDisplayAdapter
    private lateinit var teachersAdapter: MembersDisplayAdapter
    private var localStudents = emptyList<Members>()


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
        viewModel = ViewModelProvider(this, viewModelFactory).get(MembersViewModel::class.java)

        fetchClassroom()
        memebrs_students_recycler.layoutManager = LinearLayoutManager(context)
        members_teacher_recycler.layoutManager = LinearLayoutManager(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        add_members_icon.setOnClickListener {
            val shareBody: String =
                "Use this code to join \"" + classroom.name + "\" class in Gakko \n\n" + classroom.classroomID
            val subject: String = "Use this code to join " + classroom.name + " class in Gakko"
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(sharingIntent, "Tag"))
        }

        exit_icon.setOnClickListener {
            val bottomSheetFragment = ExitClassroom()
            bottomSheetFragment.show(
                activity?.supportFragmentManager!!,
                bottomSheetFragment.tag
            )
        }
    }

    private fun fetchClassroom() = launch {
        viewModel.getClassroom(utils.retrieveCurrentClassroom() ?: "")
        viewModel.classroom.observe(viewLifecycleOwner, Observer {
            if(it != null){
                classroom = it
                if (classroom.students.isNullOrEmpty()) {
                    //Log.d("Trigger","Empty List")
                    memebrs_students_recycler.adapter = MembersDisplayAdapter(emptyList())
                } else {
                    getStudents()
                }
                getTeachers()
            }
        })

    }

    private fun getStudents() = launch {
        viewModel.getStudentsList(classroom.students)
        viewModel.studentsList.observe(viewLifecycleOwner, Observer {
            //Log.d("Trigger", "$it , classroom = ${classroom.students}")
            if (it != localStudents) {
                var sortedList: List<Members> = it.sortedBy { it.name }
                localStudents = sortedList
                studentsAdapter = MembersDisplayAdapter(sortedList)
                memebrs_students_recycler.adapter = studentsAdapter
            }
        })

    }

    private fun getTeachers() = launch {
        viewModel.getTeachersList(classroom.teachers)
        viewModel.teachersList.observe(viewLifecycleOwner, Observer {
            teachersAdapter = MembersDisplayAdapter(it)
            members_teacher_recycler.adapter = teachersAdapter
            populateUI()
        })

    }

    private fun populateUI() {
        members_progress.visibility = View.GONE
        members_teacher_recycler.visibility = View.VISIBLE
        members_students_title.visibility = View.VISIBLE
        members_teacher_title.visibility = View.VISIBLE
        memebrs_students_recycler.visibility = View.VISIBLE
        add_members_icon.visibility = View.VISIBLE
        exit_icon.visibility = View.VISIBLE
    }

}
