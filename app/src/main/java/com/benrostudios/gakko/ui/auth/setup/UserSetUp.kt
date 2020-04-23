package com.benrostudios.gakko.ui.auth.setup

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.benrostudios.gakko.R
import com.benrostudios.gakko.data.models.User
import com.benrostudios.gakko.internal.Utils
import com.benrostudios.gakko.ui.base.ScopedFragment
import com.benrostudios.gakko.ui.classroom.ClassroomActivity
import kotlinx.android.synthetic.main.user_set_up_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class UserSetUp : ScopedFragment(),KodeinAware {
    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: UserSetUpViewModelFactory by instance()
    private val utils: Utils by instance()
    companion object {
        fun newInstance() = UserSetUp()
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
        user_register_button.setOnClickListener {
            validate()
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
            createUserObject(name , displayName)
        }
    }

    private fun createUserObject(name: String , displayName: String){
        var user = User(null,displayName,utils.retrieveMobile()!!,name,true,"https://linktoimage.com/user.jpg")
        isSuccessfulListener()
        registerUser(user)
    }

    private fun registerUser(user : User) = launch {
        viewModel.registerUser(user)
    }

    private fun isSuccessfulListener() = launch {
        viewModel.response.observe(viewLifecycleOwner, Observer {
            if(!it){
                val i = Intent(requireActivity(),ClassroomActivity::class.java)
                startActivity(i)
                activity?.finish()
            }else{
                Toast.makeText(context, "Unable to register!", Toast.LENGTH_SHORT).show()
            }
        })
    }


}
