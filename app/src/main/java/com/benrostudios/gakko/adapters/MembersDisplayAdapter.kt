package com.benrostudios.gakko.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.benrostudios.gakko.R
import com.benrostudios.gakko.data.models.Members
import com.benrostudios.gakko.internal.Utils
import com.benrostudios.gakko.ui.chat.Chat
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.members_list_item.view.*

class MembersDisplayAdapter(private val membersList: List<Members>) :
    RecyclerView.Adapter<MembersDisplayAdapter.MembersViewHolder>() {


    private lateinit var mContext: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MembersViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.members_list_item, parent, false)
        mContext = parent.context
        return MembersViewHolder(view)
    }

    override fun getItemCount(): Int = membersList.size

    override fun onBindViewHolder(holder: MembersViewHolder, position: Int) {
        holder.name.text = membersList[position].name
        Glide.with(mContext)
            .load(R.drawable.ic_defualt_profile_pic)
            .into(holder.image)

        holder.cardHolder.setOnClickListener {
            val intent = Intent(mContext, Chat::class.java)
            var utils = Utils(mContext)
            utils.saveCurrentChat(membersList[position].phoneNumber)
            mContext.startActivity(intent)
        }
        Log.d("members", membersList[position].name)
    }

    class MembersViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.member_display_name
        val image = view.member_display_image
        val cardHolder = view.card_holder_constraint
    }

}