package com.benrostudios.gakko.ui.home.todo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.benrostudios.gakko.R
import com.benrostudios.gakko.data.models.Material
import com.benrostudios.gakko.internal.Utils
import com.benrostudios.gakko.ui.base.ScopedFragment
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.text.SimpleDateFormat
import java.util.*

class TodoFragment : ScopedFragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: TodoViewModelFactory by instance()
    private lateinit var viewModel: TodoViewModel
    private val utils: Utils by instance()
    private var classIdList = mutableListOf<String>()
    private var todoIdList = mutableListOf<String>()
    private var todoList = mutableListOf<Material>()

    companion object {
        fun newInstance() = TodoFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.todo_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(TodoViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getCurrentUser(utils.retrieveMobile()!!)
    }

    private fun getCurrentUser(userId: String) = launch {
        viewModel.getUser(userId)
        viewModel.user.observe(viewLifecycleOwner, Observer {
            if(!it.classrooms.isNullOrEmpty()) {
                classIdList = it.classrooms as MutableList<String>
                getClassrooms(classIdList)
            }
        })
    }

    private fun getClassrooms(classIdList: MutableList<String>) = launch {
        var counter = 0
        for(classId: String in classIdList) {
            viewModel.getClassroom(classId)
            viewModel.classroom.observe(viewLifecycleOwner, Observer {
                if(it.createdBy != utils.retrieveMobile()!!) {
                    todoIdList.add(classId)
                    counter++
                }
                if(classIdList.size == counter) {
                    getTodos(todoIdList)
                }
            })
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getTodos(todoIdList: MutableList<String>) = launch {
        var counter = 0
        for(todoId: String in todoIdList) {
            viewModel.getTodo(todoId)
            viewModel.todo.observe(viewLifecycleOwner, Observer {
                counter++
                val sdf: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
                val currentDateTime: Date = Calendar.getInstance().time
                val currentDateString: String = sdf.format(currentDateTime)
                val currentDate: Date = sdf.parse(currentDateString)!!
                val dueDate: Date = sdf.parse(sdf.format(Date(it.uploadedOn)))

                if(dueDate.after(currentDate)) {
                    todoList.add(it)
                }

                if(todoIdList.size == counter) {
                    updateUI(todoList)
                }
            })
        }
    }

    private fun updateUI(todoList: MutableList<Material>) {

    }
}
