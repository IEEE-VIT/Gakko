package com.benrostudios.gakko.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.benrostudios.gakko.R
import com.benrostudios.gakko.data.models.Comments
import com.benrostudios.gakko.data.models.Material
import com.benrostudios.gakko.data.models.Threads
import com.benrostudios.gakko.data.models.User
import com.benrostudios.gakko.internal.GlideApp
import com.benrostudios.gakko.internal.Utils
import com.benrostudios.gakko.ui.home.HomeActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.todo_recycler_list_item.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class TodoDisplayAdapter(private  val map: HashMap<String, List<Material>>,
                         private val dateList: List<String>):
    RecyclerView.Adapter<TodoDisplayAdapter.TodoDisplayViewHolder>() {

    private lateinit var context: Context
    @SuppressLint("SimpleDateFormat")
    private var simpleDateFormat: SimpleDateFormat = SimpleDateFormat("MM  MMMM")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoDisplayViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.todo_recycler_list_item, parent, false)
        context = parent.context
        return TodoDisplayViewHolder(view)
    }

    override fun getItemCount(): Int {
        return map.size
    }

    override fun onBindViewHolder(holder: TodoDisplayViewHolder, position: Int) {
        holder.date.text = dateList[position]
        holder.recyclerView.adapter = TodoInnerDisplayAdapter(map[dateList[position]]!!)
        holder.recyclerView.layoutManager = LinearLayoutManager(context)
        holder.recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))

    }

    class TodoDisplayViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var date: TextView = view.todo_date_list_item_text_view
        var recyclerView: RecyclerView = view.todo_list_item_recycler_view
    }
}