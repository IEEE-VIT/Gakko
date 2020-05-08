package com.benrostudios.gakko.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.benrostudios.gakko.R
import com.benrostudios.gakko.data.models.Material
import com.benrostudios.gakko.internal.GlideApp
import kotlinx.android.synthetic.main.todo_inner_recycler_view_list_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class TodoDisplayAdapter(private val materialList: List<Material>):
    RecyclerView.Adapter<TodoDisplayAdapter.TodoDisplayViewHolder>() {

    private lateinit var context: Context
    @SuppressLint("SimpleDateFormat")
    private var sdf: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy  hh:mm a")
    @SuppressLint("SimpleDateFormat")
    private var simpleDateFormat: SimpleDateFormat = SimpleDateFormat("MM  MMMM")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoDisplayViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.todo_inner_recycler_view_list_item, parent, false)
        context = parent.context
        return TodoDisplayViewHolder(view)
    }

    override fun getItemCount(): Int {
        return materialList.size
    }

    override fun onBindViewHolder(holder: TodoDisplayViewHolder, position: Int) {
        val material: Material = materialList[position]
        when (material.type) {
            "Assignment" -> {
                GlideApp.with(context)
                    .load(R.drawable.assignment_vector_icon)
                    .into(holder.icon)
            }
            "Material" -> {
                GlideApp.with(context)
                    .load(R.drawable.material_vector_icon)
                    .into(holder.icon)
            }
            else -> {
                GlideApp.with(context)
                    .load(R.drawable.quiz_vector_icon)
                    .into(holder.icon)
            }
        }
        holder.type.text = material.type + " : "
        holder.title.text = material.name
        holder.name.text = material.classroomName
        holder.datePosted.text = "Posted on : " + sdf.format(Date(material.uploadedOn))
        holder.dueDate.text = "Due Date : " + simpleDateFormat.format(Date(material.deadline * 1000))
    }

    class TodoDisplayViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val icon = view.todo_inner_list_item_material_icon
        val type = view.todo_inner_list_item_material_type
        val title = view.todo_inner_list_item_material_name
        val datePosted = view.todo_inner_list_item_material_date
        val name = view.todo_inner_list_item_class_name
        val dueDate = view.todo_due_date_text_view
    }
}