package com.ieeevit.gakko.ui.home.exitclassroom

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ieeevit.gakko.R
import com.ieeevit.gakko.internal.Utils
import com.ieeevit.gakko.ui.base.ScopedFragment
import com.ieeevit.gakko.ui.classroom.createclassroom.CreateClassroomViewModel
import com.ieeevit.gakko.ui.classroom.createclassroom.CreateClassroomViewModelFactory
import com.ieeevit.gakko.ui.home.homehost.HomeHostViewModel
import com.ieeevit.gakko.ui.home.homehost.HomeHostViewModelFactory
import kotlinx.android.synthetic.main.fragment_exit_classroom.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class ExitClassroom : BottomSheetDialogFragment(),KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: CreateClassroomViewModelFactory by instance()
    private val utils: Utils by instance()
    private lateinit var viewModel: CreateClassroomViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_exit_classroom, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(CreateClassroomViewModel::class.java)
        exit_classroom_btn.setOnClickListener {
            exitClassroom()
        }
        exit_classroom_cancel_btn.setOnClickListener {
            dismiss()
        }
    }
    companion object {
        fun newInstance() {

        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    private fun exitClassroom() = viewLifecycleOwner.lifecycle.coroutineScope.launch {
        utils.retrieveCurrentClassroom()?.let { viewModel.exitClassroom(it) }
        viewModel.exitClassroomStatus.observe(viewLifecycleOwner, Observer {
            if(it){
                Toast.makeText(requireActivity(),"Exited Classroom!",Toast.LENGTH_SHORT).show()
                viewModel.switchToClassroomDisplay(true)
                dismiss()
            }else{
                Toast.makeText(requireActivity(),"Error Exiting Classroom!",Toast.LENGTH_SHORT).show()
                dismiss()
            }
        })
    }
}