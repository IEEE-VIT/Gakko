package com.benrostudios.gakko.adapters

import android.content.Context
import android.provider.Telephony
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.benrostudios.gakko.R
import com.benrostudios.gakko.data.models.ChatMessage
import com.benrostudios.gakko.internal.Utils
import kotlinx.android.synthetic.main.recieved_message_item.view.*
import kotlinx.android.synthetic.main.sent_message_item.view.*
import kotlinx.android.synthetic.main.verification_fragment.view.*
import java.text.SimpleDateFormat
import java.util.*

class ChatAdapter(private val chats: List<ChatMessage>,
                  private val sender: String) :
    RecyclerView.Adapter<BaseViewHolder<*>>() {


    private lateinit var utils: Utils

    companion object {
        private const val TYPE_RECEIVED = 0
        private const val TYPE_SENT = 1
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        utils = Utils(parent.context)
        return when (viewType) {
            TYPE_RECEIVED -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recieved_message_item, parent, false)
                ReceivedViewHolder(view)
            }
            TYPE_SENT -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.sent_message_item, parent, false)
                SentViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }



    override fun getItemCount(): Int = chats.size

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val adapterElement = chats[position]
        when (holder) {
            is SentViewHolder -> holder.bind(adapterElement)
            is ReceivedViewHolder -> holder.bind(adapterElement)
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemViewType(position: Int): Int {
        val chatMessage = chats[position]
        return if(chatMessage.sender == sender){
            TYPE_SENT
        }else{
            TYPE_RECEIVED
        }
    }

    class SentViewHolder(v: View) : BaseViewHolder<ChatMessage>(v) {
        override fun bind(item: ChatMessage) {
            with(itemView){
                sent_message_body.text = item.body
                sent_message_time.text = dateParser(item.timestamp)
            }
        }
        fun dateParser(inputUnix: Long): String{
            val sdf = SimpleDateFormat("hh:mm")
            val date= Date(inputUnix*1000L)
            return sdf.format(date)
        }
    }

    class ReceivedViewHolder(v: View) : BaseViewHolder<ChatMessage>(v) {
        override fun bind(item: ChatMessage) {
            with(itemView){
                received_message_body.text = item.body
                received_message_time.text = dateParser(item.timestamp)
            }
        }
        fun dateParser(inputUnix: Long): String{
            val sdf = SimpleDateFormat("hh:mm")
            val date= Date(inputUnix*1000L)
            return sdf.format(date)
        }

    }

}

abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: T)
}