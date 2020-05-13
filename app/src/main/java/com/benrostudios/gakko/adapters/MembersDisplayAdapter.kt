package com.benrostudios.gakko.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.contentValuesOf
import androidx.recyclerview.widget.RecyclerView
import com.benrostudios.gakko.R
import com.benrostudios.gakko.data.models.Members
import com.benrostudios.gakko.internal.AvatarConstants
import com.benrostudios.gakko.internal.AvatarGenerator
import com.benrostudios.gakko.internal.Utils
import com.benrostudios.gakko.ui.chat.Chat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.members_list_item.view.*

class MembersDisplayAdapter(private val membersList: List<Members>) :
    RecyclerView.Adapter<MembersDisplayAdapter.MembersViewHolder>() {

    private lateinit var mContext: Context
    private lateinit var utils: Utils

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MembersViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.members_list_item, parent, false)
        mContext = parent.context
        utils = Utils(mContext)
        return MembersViewHolder(view)
    }

    override fun getItemCount(): Int = membersList.size

    override fun onBindViewHolder(holder: MembersViewHolder, position: Int) {
        holder.name.text = membersList[position].name

        var options: RequestOptions = RequestOptions()
            .error(AvatarGenerator.avatarImage(mContext, 200, AvatarConstants.CIRCLE, membersList[position].name))
            .placeholder(AvatarGenerator.avatarImage(mContext, 200, AvatarConstants.CIRCLE, membersList[position].name))
            .circleCrop()

        Glide.with(mContext)
            .load(membersList[position].profileImgLink)
            .apply(options)
            .placeholder(AvatarGenerator.avatarImage(mContext, 200, AvatarConstants.CIRCLE, membersList[position].name))
            .into(holder.image)


        holder.cardHolder.setOnClickListener {
            if (membersList[position].phoneNumber != utils.retrieveMobile()) {
                val intent = Intent(mContext, Chat::class.java)
                utils.saveCurrentChat(membersList[position].phoneNumber)
                mContext.startActivity(intent)
            } else {
                Toast.makeText(mContext, "Sorry , you cant chat with yourself!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        Log.d("members", membersList[position].name)
    }

    class MembersViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.member_display_name
        val image = view.member_display_image
        val cardHolder = view.card_holder_constraint
    }

}