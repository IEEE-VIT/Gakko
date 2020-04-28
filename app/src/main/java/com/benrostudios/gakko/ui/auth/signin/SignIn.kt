package com.benrostudios.gakko.ui.auth.signin


import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.benrostudios.gakko.R
import com.benrostudios.gakko.internal.Utils
import com.benrostudios.gakko.ui.base.ScopedFragment
import com.benrostudios.gakko.ui.classroom.ClassroomActivity
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.sign_in_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.util.concurrent.TimeUnit

class SignIn : ScopedFragment(), KodeinAware {



    private lateinit var phoneNumber: String
    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: SignInViewModelFactory by instance()

    companion object {
        fun newInstance() = SignIn()
    }

    private lateinit var viewModel: SignInViewModel
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sign_in_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        sign_in_button.setOnClickListener {
            if (validatePhoneNumber()) {
                var bundle = bundleOf("phoneNumber" to phoneNumber)
                navController.navigate(R.id.action_signIn_to_verification,bundle)

            }
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(SignInViewModel::class.java)
    }


    private fun validatePhoneNumber(): Boolean {
        phoneNumber = phone_input.text.toString()
        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length < 13) {
            phone_input.error = "Invalid phone number."
            return false
        }
        return true
    }


}
