package com.benrostudios.gakko.ui.chat.chatdisplay

import android.content.Intent
import android.content.Intent.getIntent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RestrictTo
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.benrostudios.gakko.R
import com.benrostudios.gakko.adapters.ChatAdapter
import com.benrostudios.gakko.data.models.ChatMessage
import com.benrostudios.gakko.internal.Utils
import com.benrostudios.gakko.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.chat_interface_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class ChatInterface : ScopedFragment(), KodeinAware {
    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: ChatInterfaceViewModelFactory by instance()
    private val utils: Utils by instance()
    private lateinit var adapter: ChatAdapter

    companion object {
        fun newInstance() = ChatInterface()
    }

    private lateinit var viewModel: ChatInterfaceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.chat_interface_fragment, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this,viewModelFactory).get(ChatInterfaceViewModel::class.java)
        chat_recipient_image.setImageResource(R.drawable.ic_defualt_profile_pic)
        receiveMessages()
        chat_display_recycler.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,true)
        message_edit_text_layout.setEndIconOnClickListener {
            sendMessage()
        }
        Toast.makeText(context,utils.retrieveCurrentChat(),Toast.LENGTH_SHORT).show()
    }
    private fun receiveMessages() = launch {
        viewModel.receiveMessages()
        viewModel.usrChats.observe(viewLifecycleOwner, Observer {
            Log.d("ChatInterface",it.toString())
            adapter = ChatAdapter(it.reversed(),utils.retrieveMobile()?:"")
            chat_display_recycler.adapter = adapter
        })
    }

    private fun sendMessage() = launch {
        val unixTime = System.currentTimeMillis() / 1000L
        val chatMessage = ChatMessage("text","",usr_message_text.text.toString(),utils.retrieveCurrentClassroom()!!,utils.retrieveCurrentChat()!!,false,utils.retrieveMobile()!!,true,unixTime)
        viewModel.sendMessage(chatMessage)
        usr_message_text.setText("")
    }
}
