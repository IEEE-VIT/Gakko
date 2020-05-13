package com.benrostudios.gakko.ui.home.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.benrostudios.gakko.R
import com.benrostudios.gakko.adapters.TodoDisplayAdapter
import com.benrostudios.gakko.data.models.Classroom
import com.benrostudios.gakko.data.models.Material
import com.benrostudios.gakko.internal.AvatarConstants
import com.benrostudios.gakko.internal.AvatarGenerator
import com.benrostudios.gakko.internal.Utils
import com.benrostudios.gakko.ui.base.ScopedFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
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
    var materials = mutableListOf<Material>()
    private var todoIdList = mutableListOf<String>()
    private var todoList = mutableListOf<Material>()
    private var todoMap: HashMap<String, List<Material>> = HashMap()
    private lateinit var adapter: TodoDisplayAdapter

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
        var storageReference: StorageReference = FirebaseStorage.getInstance().reference
        val profilePicUploader = storageReference.child("dp/${utils.retrieveMobile()}/dp.jpg")

        var options: RequestOptions = RequestOptions()
            .error(AvatarGenerator.avatarImage(requireContext(), 200, AvatarConstants.CIRCLE, utils.retrieveCurrentUserName()!!))
            .placeholder(AvatarGenerator.avatarImage(requireContext(), 200, AvatarConstants.CIRCLE, utils.retrieveCurrentUserName()!!))
            .circleCrop()

        if(utils.retrieveProfilePic().isNullOrEmpty()) {
            Glide.with(this)
                .load(profilePicUploader)
                .apply(options)
                .placeholder(AvatarGenerator.avatarImage(requireContext(), 200, AvatarConstants.CIRCLE, utils.retrieveCurrentUserName()!!))
                .into(todo_fragment_profile_picture)
        }else {
            Glide.with(this)
                .load(utils.retrieveProfilePic())
                .apply(options)
                .placeholder(AvatarGenerator.avatarImage(requireContext(), 200, AvatarConstants.CIRCLE, utils.retrieveCurrentUserName()!!))
                .into(todo_fragment_profile_picture)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        todo_fragment_profile_picture.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_todo_to_profileFragment)
        }
        todo_fragment_progress_bar.visibility = View.VISIBLE
        getCurrentUser(utils.retrieveMobile()!!)
    }

    private fun getCurrentUser(userId: String) = launch {
        viewModel.getUser(userId)
        viewModel.user.observe(viewLifecycleOwner, Observer {
            if(!it.classrooms.isNullOrEmpty()) {
                classIdList = it.classrooms as MutableList<String>
                getClassrooms(classIdList)
            } else {
                defaultUI()
            }
        })
    }



   private fun getClassrooms(classIdList: List<String>) = launch {
           viewModel.getClassroom(classIdList)
           viewModel.classroom.observe(viewLifecycleOwner, Observer {
                   getTodos(it)
           })
   }

    private fun getTodos(classrooms: List<Classroom>) = launch {
        todoIdList.clear()
        todoMap.clear()

        for(classroom: Classroom in classrooms) {
            if(classroom.createdBy != utils.retrieveMobile()!!) {
                todoIdList.add(classroom.classroomID)
            }
        }

        if(todoIdList.isNotEmpty()) {
            viewModel.getTodo(todoIdList)
            viewModel.todo.observe(viewLifecycleOwner, Observer {
                if (! it.isNullOrEmpty()) {
                    updateUI(it)
                } else {
                    defaultUI()
                }
            })
        } else {
            defaultUI()
        }
    }

    private fun updateUI(materialList: List<Material>) {
        todoList.clear()

        for (material: Material in materialList) {
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val currentDateTime: Date = Calendar.getInstance().time
            val currentDate = sdf.parse(sdf.format(currentDateTime))
            val dueDate = sdf.parse(sdf.format(Date(material.deadline * 1000)))

            if (dueDate.after(currentDate) && ! todoList.contains(material)) {
                todoList.add(material)
            }
        }

        if(! todoList.isNullOrEmpty()) {
            demo_todo_image_view.visibility = View.GONE
            demo_todo_text_view.visibility = View.GONE
            adapter = TodoDisplayAdapter(todoList)
            todo_recycler_view.adapter = adapter
            todo_recycler_view.layoutManager = LinearLayoutManager(requireContext())
            todo_fragment_progress_bar.visibility = View.GONE
            todo_recycler_view.visibility = View.VISIBLE
        } else {
            defaultUI()
        }
    }

    private fun defaultUI() {
        todo_fragment_progress_bar.visibility = View.GONE
        demo_todo_image_view.visibility = View.VISIBLE
        demo_todo_text_view.visibility = View.VISIBLE
    }
}
