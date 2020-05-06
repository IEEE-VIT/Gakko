package com.benrostudios.gakko.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.benrostudios.gakko.R
import com.benrostudios.gakko.data.models.Material
import com.benrostudios.gakko.internal.GlideApp
import kotlinx.android.synthetic.main.pinboard_list_item.view.*
import java.text.SimpleDateFormat
import java.util.*


class PinboardDisplayAdapter(private val materialsList: List<Material>): RecyclerView.Adapter<PinboardDisplayAdapter.PinboardViewHolder>(){

    private lateinit var context: Context
    @SuppressLint("SimpleDateFormat")
    private val sdf: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy  hh:mm a")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PinboardViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.pinboard_list_item, parent, false)
        context = parent.context
        return PinboardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return materialsList.size
    }

    override fun onBindViewHolder(holder: PinboardViewHolder, position: Int) {
        val material: Material = materialsList[position]
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
        holder.type.text = material.type + ":"
        holder.title.text = material.name
        holder.datePosted.text = "Posted on " + sdf.format(Date(material.uploadedOn))
        GlideApp.with(context)
            .load(R.drawable.ic_file_download_black_24dp)
            .into(holder.downloadIcon)
        holder.downloadIcon.setOnClickListener {
            val url: String = material.url
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(context, intent, null)
        }
    }

    class PinboardViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        val icon = view.material_icon_list_item
        val type = view.material_type_text_view_list_item
        val title = view.material_title_text_view_list_item
        val datePosted = view.material_posting_date_text_view_list_item
        val downloadIcon = view.material_download_icon_list_item
    }
}