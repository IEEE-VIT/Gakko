package com.benrostudios.gakko.ui.classroom.createclassroom

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation

import com.benrostudios.gakko.R
import com.benrostudios.gakko.data.models.Classroom
import com.benrostudios.gakko.internal.Utils
import com.benrostudios.gakko.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.create_classroom_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class CreateClassroom : ScopedFragment(), KodeinAware {
    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: CreateClassroomViewModelFactory by instance()
    private val utils: Utils by instance()
    private lateinit var navController: NavController


    companion object {
        fun newInstance() = CreateClassroom()
    }

    private lateinit var viewModel: CreateClassroomViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.create_classroom_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(CreateClassroomViewModel::class.java)
        usr_create_classroom_btn.setOnClickListener {
            validate()
        }
    }

    private fun validate() {
        var validation = true
        if (create_class_name_input.text.toString().isEmpty()) {
            create_class_name_input.error = "Please Enter a Valid Classname"
            validation = false
        }
        if (validation) {
            fetchClassroomId()
        }
    }

    private fun fetchClassroomId() = launch {
        viewModel.fetchNewClassroomId()
        viewModel.fetchClassroomIdResponse.observe(viewLifecycleOwner, Observer {
            if (it.classroomId.isNotEmpty()) {
                Log.d("fetchId", it.toString())
                createClassroom(
                    create_class_name_input.text.toString(),
                    create_class_course_input.text.toString(),
                    private_class_input_switch.isChecked,
                    it.classroomId
                )
            } else {
                Log.d("fetchId", "Null Fetches" + it.toString())
            }
        })
    }

    private fun createClassroom(classTitle: String , classCourseCode: String, classPrivate: Boolean,newClassroomId: String) = launch{
        if(newClassroomId.isNotEmpty()){
            val newClassroom = Classroom(newClassroomId,classCourseCode,utils.retrieveMobile()?:"",classTitle,"",classPrivate, emptyList(),
                listOf(utils.retrieveMobile()?:""), emptyMap())
            viewModel.createClassroom(newClassroom)
        }
        viewModel.createClassroomResponse.observe(viewLifecycleOwner, Observer {
            if(it){
                navController.navigateUp()
            }
        })
    }



}
