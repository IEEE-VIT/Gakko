package com.benrostudios.gakko.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.benrostudios.gakko.R
import com.benrostudios.gakko.data.models.Classroom
import com.benrostudios.gakko.data.models.Threads
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ThreadsDisplayAdapter(private val threadsList: List<Threads>,
                            private val threadClassroom: Classroom):
    RecyclerView.Adapter<ThreadsDisplayAdapter.ThreadsDisplayViewHolder>() {

    private val classCommentsTitle: String = "Add class comments"
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
    private val studentInClassroom: List<String> = threadClassroom.students
    private val teachersInClassroom: List<String> = threadClassroom.teachers

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThreadsDisplayViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.thread_list_item, parent, false)
        return ThreadsDisplayViewHolder(view)
    }

    override fun getItemCount(): Int {
        return threadsList.size
    }

    override fun onBindViewHolder(holder: ThreadsDisplayViewHolder, position: Int) {
        val thread: Threads = threadsList[position]

        holder.threadBody.text = thread.body
        if(thread.comments.isEmpty()) {
            holder.threadComments.text = classCommentsTitle
        }
        else {
            holder.threadComments.text = thread.comments.size.toString() + " class comment"
        }

        if(studentInClassroom.contains(thread.user.toString())) {
            holder.designation.text = "Student"
        }
        else {
            holder.designation.text = "Teacher"
        }

    }



    class ThreadsDisplayViewHolder(view: View): RecyclerView.ViewHolder(view){
        val profilePicture: ImageView = view.findViewById(R.id.threads_person_image_view)
        val personName: TextView = view.findViewById(R.id.threads_name_text_view)
        val designation: TextView = view.findViewById(R.id.threads_designation_text_view)
        val day: TextView = view.findViewById(R.id.threads_day_text_view)
        val threadBody: TextView = view.findViewById(R.id.threads_body_text_view)
        val threadComments: TextView = view.findViewById(R.id.threads_comments_text_view)
    }
}