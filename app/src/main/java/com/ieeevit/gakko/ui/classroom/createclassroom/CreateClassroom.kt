package com.ieeevit.gakko.ui.classroom.createclassroom


import android.os.Bundle
import android.util.Log


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope


import com.ieeevit.gakko.R
import com.ieeevit.gakko.data.models.Classroom
import com.ieeevit.gakko.internal.Utils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.create_classroom_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class CreateClassroom() : BottomSheetDialogFragment(), KodeinAware{
    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: CreateClassroomViewModelFactory by instance()
    private val utils: Utils by instance()
    private lateinit var viewModel: CreateClassroomViewModel

    companion object {
        fun newInstance() = CreateClassroom()
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
            this.isCancelable = false
            create_classroom_progress.visibility = View.VISIBLE
            fetchClassroomId()
        }
    }

    private fun fetchClassroomId() = viewLifecycleOwner.lifecycleScope.launch {
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

    private fun createClassroom(classTitle: String , classCourseCode: String, classPrivate: Boolean,newClassroomId: String) = viewLifecycleOwner.lifecycleScope.launch{
        if(newClassroomId.isNotEmpty()){
            val newClassroom = Classroom(newClassroomId,classCourseCode,utils.retrieveMobile()?:"",classTitle,"",classPrivate, emptyList(),
                listOf(utils.retrieveMobile()?:""), emptyList()
            )
            viewModel.createClassroom(newClassroom)
        }
        viewModel.createClassroomResponse.observe(viewLifecycleOwner, Observer {
            if(it){
                dismiss()
            }
        })
    }



}
