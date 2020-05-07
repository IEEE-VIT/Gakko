package com.benrostudios.gakko.ui.home.todo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.benrostudios.gakko.R
import com.benrostudios.gakko.adapters.TodoDisplayAdapter
import com.benrostudios.gakko.data.models.Material
import com.benrostudios.gakko.internal.Utils
import com.benrostudios.gakko.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.todo_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class TodoFragment : ScopedFragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: TodoViewModelFactory by instance()
    private lateinit var viewModel: TodoViewModel
    private val utils: Utils by instance()
    private var classIdList = mutableListOf<String>()
    private var todoIdList = mutableListOf<String>()
    private var todoList = mutableListOf<Material>()
    @SuppressLint("SimpleDateFormat")
    private var simpleDateFormat: SimpleDateFormat = SimpleDateFormat("MM MMMM")
    private lateinit var adapter: TodoDisplayAdapter
    private lateinit var navController: NavController

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
        todo_fragment_profile_picture.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_todo_to_profileFragment)
        }
        //getCurrentUser(utils.retrieveMobile()!!)
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
        todoIdList.clear()
        for(classId: String in classIdList) {
            viewModel.getClassroom(classId)
            viewModel.classroom.observe(viewLifecycleOwner, Observer {
                if(it.createdBy == utils.retrieveMobile()!!) {
                    todoIdList.add(classId)
                }
                counter++
                if(classIdList.size == counter) {
                    getTodos(todoIdList)
                }
            })
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getTodos(todoIdList: MutableList<String>) = launch {
        var counter = 0
        todoList.clear()
        for(todoId: String in todoIdList) {
            viewModel.getTodo(todoId)
            viewModel.todo.observe(viewLifecycleOwner, Observer {
                for(material: Material in it) {
                    counter++
                    val sdf = SimpleDateFormat("dd/MM/yyyy")
                    val currentDateTime: Date = Calendar.getInstance().time
                    val currentDate = sdf.parse(sdf.format(currentDateTime))
                    val dueDate = sdf.parse(sdf.format(Date(material.deadline * 1000)))

                    if (dueDate.after(currentDate)) {
                        todoList.add(material)
                    }

                    if (it.size == counter) {
                        updateUI(todoList)
                    }
                }
            })
        }
    }

    private fun updateUI(todoList: MutableList<Material>) {
        val materials = mutableListOf<Material>()
        val datesList = mutableListOf<String>()
        val hashMap: HashMap<String, List<Material>> = HashMap()

        for(material: Material in todoList) {
            materials.clear()
            for(materialLocal: Material in todoList) {
                if(simpleDateFormat.format(material.deadline * 1000) == simpleDateFormat.format(materialLocal.deadline * 1000)) {
                    materials.add(materialLocal)
                }
            }
            hashMap[simpleDateFormat.format(material.deadline * 1000)] = materials
            if(!datesList.contains(simpleDateFormat.format(material.deadline * 1000))) {
                datesList.add(simpleDateFormat.format(material.deadline * 1000))
            }
        }

        adapter = TodoDisplayAdapter(hashMap, datesList)
        todo_recycler_view.adapter = adapter
        todo_recycler_view.layoutManager = LinearLayoutManager(requireContext())
    }
}
