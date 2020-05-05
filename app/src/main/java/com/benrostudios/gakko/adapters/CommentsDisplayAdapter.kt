package com.benrostudios.gakko.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.benrostudios.gakko.R
import com.benrostudios.gakko.data.models.Comments
import com.benrostudios.gakko.data.models.User
import com.benrostudios.gakko.internal.GlideApp
import kotlinx.android.synthetic.main.specific_comment_list_item.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class CommentsDisplayAdapter(private val comments: List<Comments>, private val map: HashMap<String, User>)
    : RecyclerView.Adapter<CommentsDisplayAdapter.CommentsViewHolder>() {

    private lateinit var context: Context
    @SuppressLint("SimpleDateFormat")
    private var dateFormatter: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.specific_comment_list_item, parent, false)
        context = parent.context
        return CommentsViewHolder(view)
    }

    override fun getItemCount(): Int = comments.size

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        val comment: Comments = comments[position]
        val user: User = map[comment.user.toString()] ?: User(emptyList(), "", "", "", false, "")

        GlideApp.with(context)
            .load(user.profileImage)
            .centerCrop()
            .placeholder(R.drawable.ic_defualt_profile_pic)
            .into(holder.profilePicture)
        holder.userName.text = user.name
        holder.time.text = dateFormatter.format(Date(comment.timestamp))
        holder.comment.text = comment.body
    }

    class CommentsViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var profilePicture: ImageView = view.specific_comment_user_profile_picture
        var userName: TextView = view.specific_comment_user_name
        var time: TextView = view.specific_comment_time
        var comment: TextView = view.specific_comment_body
    }

}