package com.benrostudios.gakko.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.benrostudios.gakko.R
import com.benrostudios.gakko.data.models.Threads
import com.benrostudios.gakko.data.models.User
import com.benrostudios.gakko.internal.GlideApp
import com.benrostudios.gakko.ui.home.HomeActivity
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class ThreadsDisplayAdapter(private val threadsList: List<Threads>,
                            private val teachersList: List<String>,
                            private val map: HashMap<String, User>):
    RecyclerView.Adapter<ThreadsDisplayAdapter.ThreadsDisplayViewHolder>() {

    private lateinit var context: Context
    private lateinit var designation: String
    private lateinit var numberOfComments: String
    @SuppressLint("SimpleDateFormat")
    private var dateFormatter: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThreadsDisplayViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.thread_list_item, parent, false)
        context = parent.context
        return ThreadsDisplayViewHolder(view)
    }

    override fun getItemCount(): Int {
        return threadsList.size
    }

    override fun onBindViewHolder(holder: ThreadsDisplayViewHolder, position: Int) {
        val thread: Threads = threadsList[position]
        val threadUser: User = map[thread.user] ?: User(emptyList(), "", "", "", false, "")

        designation = if(teachersList.contains(threadUser.id)) {
            "Teacher"
        } else {
            "Student"
        }

        numberOfComments = if(thread.comments.isEmpty()) {
            "Add Class Comments"
        } else {
            thread.comments.size.toString() + " Class Comment"
        }

        GlideApp.with(context)
            .load(threadUser.profileImage)
            .centerCrop()
            .placeholder(R.drawable.ic_defualt_profile_pic)
            .into(holder.profilePicture)

        holder.personName.text = threadUser.name
        holder.designation.text = designation
        holder.day.text = dateFormatter.format(Date(thread.timestamp))
        holder.threadBody.text = thread.body
        holder.threadComments.text = numberOfComments
        holder.threadComments.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_homeHostFragment_to_commentFragment)
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