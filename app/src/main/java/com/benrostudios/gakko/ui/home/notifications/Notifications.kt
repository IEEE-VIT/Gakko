package com.benrostudios.gakko.ui.home.notifications

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager

import com.benrostudios.gakko.R
import com.benrostudios.gakko.adapters.RequestsAdapter
import com.benrostudios.gakko.internal.Utils
import com.benrostudios.gakko.ui.base.ScopedFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.notifications_fragment.*
import kotlinx.android.synthetic.main.user_set_up_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class Notifications : ScopedFragment(), KodeinAware {
    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: NotificationsViewModelFactory by instance()
    private val utils: Utils by instance()
    private lateinit var navController: NavController

    companion object {
        fun newInstance() = Notifications()
    }

    private lateinit var viewModel: NotificationsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.notifications_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(NotificationsViewModel::class.java)
        var storageReference: StorageReference = FirebaseStorage.getInstance().reference
        val profilePicUploader = storageReference.child("dp/${utils.retrieveMobile()}/dp.jpg")
        request_recycler.layoutManager = LinearLayoutManager(context)
        fetchRequestsList()
        listenRequestsList()

        var options: RequestOptions = RequestOptions()
            .error(R.drawable.ic_defualt_profile_pic)
            .placeholder(R.drawable.ic_defualt_profile_pic)
            .circleCrop()

        if(utils.retrieveProfilePic().isNullOrEmpty()) {
            Glide.with(this)
                .load(profilePicUploader)
                .apply(options)
                .placeholder(R.drawable.ic_defualt_profile_pic)
                .into(usr_image)
        }else {
            Glide.with(this)
                .load(utils.retrieveProfilePic())
                .apply(options)
                .placeholder(R.drawable.ic_defualt_profile_pic)
                .into(usr_image)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        usr_image.setOnClickListener {
            navController.navigate(R.id.action_notifications_to_profileFragment)
        }
    }

    private fun fetchRequestsList() = launch {
        viewModel.fetchRequestList(utils.retrieveTeachersList()?.toList() ?: emptyList())
    }

    private fun listenRequestsList() = launch {
        viewModel.requestList.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()) {
                no_notification_image.visibility = View.VISIBLE
                no_notification_title.visibility = View.VISIBLE
                request_recycler.visibility = View.GONE
            } else {
                request_recycler.visibility = View.VISIBLE
                no_notification_image.visibility = View.GONE
                no_notification_title.visibility = View.GONE
                request_recycler.adapter =
                    RequestsAdapter(it, object : RequestsAdapter.ClickListener {
                        override fun acceptTrigger(posistion: Int) {
                            acceptRequest(it[posistion].classroomId, it[posistion].phone)
                        }

                        override fun declineTrigger(posistion: Int) {
                            declineRequest(it[posistion].classroomId, it[posistion].phone)
                        }
                    })
            }
        })
    }

    private fun declineRequest(classroomId: String, person: String) = launch {
        viewModel.declineRequests(classroomId, person)

    }

    private fun acceptRequest(clasroomId: String, person: String) = launch {
        viewModel.acceptRequest(clasroomId, person)
    }

}
