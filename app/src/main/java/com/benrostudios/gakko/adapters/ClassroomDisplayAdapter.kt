package com.benrostudios.gakko.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.benrostudios.gakko.R
import com.benrostudios.gakko.data.models.Classroom
import com.benrostudios.gakko.internal.Utils
import kotlinx.android.synthetic.main.clasroom_display_item.view.*

class ClassroomDisplayAdapter(private val classrooms: List<Classroom>):
    RecyclerView.Adapter<ClassroomDisplayAdapter.ClassroomDisplayViewHolder>() {

    private lateinit var utils: Utils
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassroomDisplayViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.clasroom_display_item,parent,false)
        utils = Utils(parent.context)
        return ClassroomDisplayViewHolder(view)
    }

    override fun getItemCount(): Int = classrooms.size

    override fun onBindViewHolder(holder: ClassroomDisplayViewHolder, position: Int) {
        holder.title.text = classrooms[position].name
        holder.courseCode.text = classrooms[position].courseCode
        holder.background.setImageResource(randomizeImage())
        holder.cardContainer.setOnClickListener {
            utils.saveCurrentClassroom(classrooms[position].classroomID)
        }
    }

    fun randomizeImage(): Int{
        val image = (1..2).random()
        return if(image == 1){
            R.drawable.clasroom_display_bg_1
        }else{
            R.drawable.clasroom_display_bg_2
        }
    }

    class ClassroomDisplayViewHolder(view: View): RecyclerView.ViewHolder(view){
        val title = view.classroom_display_item_title
        val cardContainer = view.classroom_display_item_card
        val courseCode = view.classroom_display_item_course_code
        val background = view.clasroom_display_item_bg

    }
}