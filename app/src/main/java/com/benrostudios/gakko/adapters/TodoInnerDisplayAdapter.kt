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

class TodoInnerDisplayAdapter(private val materialList: List<Material>):
    RecyclerView.Adapter<TodoInnerDisplayAdapter.TodoInnerDisplayViewHolder>() {

    private lateinit var context: Context
    @SuppressLint("SimpleDateFormat")
    private var simpleDateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy  hh:mm a")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoInnerDisplayViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.todo_inner_recycler_view_list_item, parent, false)
        context = parent.context
        return TodoInnerDisplayViewHolder(view)
    }

    override fun getItemCount(): Int {
        return materialList.size
    }

    override fun onBindViewHolder(holder: TodoInnerDisplayViewHolder, position: Int) {
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
        holder.type.text = material.type
        holder.title.text = material.name
        holder.datePosted.text = "Posted on " + simpleDateFormat.format(Date( material.uploadedOn))
        holder.name.text = material.classroomName
    }


    class TodoInnerDisplayViewHolder(view: View): RecyclerView.ViewHolder(view){
        val icon = view.todo_inner_list_item_material_icon
        val type = view.todo_inner_list_item_material_type
        val title = view.todo_inner_list_item_material_name
        val datePosted = view.todo_inner_list_item_material_date
        val name = view.todo_inner_list_item_class_name
    }
}