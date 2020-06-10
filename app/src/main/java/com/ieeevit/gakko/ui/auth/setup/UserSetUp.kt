package com.ieeevit.gakko.ui.auth.setup

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.ieeevit.gakko.R
import com.ieeevit.gakko.data.models.User
import com.ieeevit.gakko.internal.Utils
import com.ieeevit.gakko.ui.base.ScopedFragment
import com.ieeevit.gakko.ui.home.HomeActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.user_set_up_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.io.File

class UserSetUp : ScopedFragment(),KodeinAware {
    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: UserSetUpViewModelFactory by instance()
    private val utils: Utils by instance()
    private lateinit var uri: Uri
    private var imageName: String? = null
    private var uriString: String? = null
    companion object {
        fun newInstance() = UserSetUp()
        const val PICK_IMG_CODE = 3
    }

    private lateinit var viewModel: UserSetUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.user_set_up_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this,viewModelFactory).get(UserSetUpViewModel::class.java)
        var options: RequestOptions = RequestOptions()
            .error(R.drawable.ic_defualt_profile_pic)
            .placeholder(R.drawable.ic_defualt_profile_pic)
            .circleCrop()

        Glide.with(this)
            .load(utils.retrieveProfilePic())
            .apply(options)
            .placeholder(R.drawable.ic_defualt_profile_pic)
            .into(usr_profile_image)

        user_register_button.setOnClickListener {
            validate()
        }
        usr_profile_image.setOnClickListener {
            getImage()
        }


    }

    private fun validate(){
        var validation = true
        var name = user_name_input.text.toString()
        var displayName = user_display_name_input.text.toString()
        if(name.isEmpty()){
            user_name_input.error = "Please enter a valid name!"
            validation = false
        }
        if(displayName.isEmpty()){
            user_display_name_input.error = "Please enter a valid display name!"
            validation = false
        }
        if(validation){
            setup_progress.visibility = View.VISIBLE
            utils.saveCurrentUserName(name)
            if(!uriString.isNullOrEmpty()){
                uploadProfilePicture(uri)
                createUserObject(name , displayName,true)
            }else{
                createUserObject(name , displayName,false)
            }
        }
    }

    private fun createUserObject(name: String , displayName: String , profilePicSetUp: Boolean) = launch{
        if(profilePicSetUp) {
            viewModel.profilePictureUrl.observe(viewLifecycleOwner, Observer {
                if (it.isNotEmpty()) {
                    val user =
                        User(null, displayName, utils.retrieveMobile()!!, name, true, it ?: "null")
                    isSuccessfulListener()
                    registerUser(user)
                }
            })
        }else{
            val user =
                User(null, displayName, utils.retrieveMobile()!!, name, true, "null")
            isSuccessfulListener()
            registerUser(user)
        }

    }


    private fun registerUser(user : User) = launch {
        viewModel.registerUser(user)
    }

    private fun uploadProfilePicture(uri: Uri) = launch{
        viewModel.uploadProfilePic(uri, utils.retrieveMobile()?:"")
    }

    private fun isSuccessfulListener() = launch {
        viewModel.response.observe(viewLifecycleOwner, Observer {
            if(!it){
                val i = Intent(requireActivity(),HomeActivity::class.java)
                startActivity(i)
                activity?.finish()
            }else{
                Toast.makeText(context, "Unable to register!", Toast.LENGTH_SHORT).show()
            }
        })
    }


    @SuppressLint("ObsoleteSdkInt")
    private fun getImage() {
        val intent = Intent()
        intent.type = "image/jpeg"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Profile Picture"), Companion.PICK_IMG_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMG_CODE && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            if (data.data != null) {
                uri = data.data!!
                uriString = uri.toString()
                val myFile = File(uriString!!)
                imageName = null

                if (uriString!!.startsWith("content://")) {
                    var cursor: Cursor? = null
                    try {
                        cursor = activity?.contentResolver?.query(uri, null, null, null, null)
                        if (cursor != null && cursor.moveToFirst()) {
                            imageName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                        }
                    } finally {
                        cursor?.close();
                    }
                }
                else if (uriString!!.startsWith("file://")) {
                    imageName = myFile.name;
                }
                Glide.with(this)
                    .load(uri)
                    .apply(RequestOptions.circleCropTransform())
                    .into(usr_profile_image)
            }
        }
        else {
            if(imageName.isNullOrEmpty())
            Toast.makeText(requireContext(), "No file chosen", Toast.LENGTH_LONG).show()
        }
    }
}
