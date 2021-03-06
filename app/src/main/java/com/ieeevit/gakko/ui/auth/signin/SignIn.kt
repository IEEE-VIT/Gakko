package com.ieeevit.gakko.ui.auth.signin


import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.ieeevit.gakko.R
import com.ieeevit.gakko.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.sign_in_fragment.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

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
        phone_input.setSelection(3)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(SignInViewModel::class.java)
    }


    private fun validatePhoneNumber(): Boolean {
        phoneNumber = phone_input.text.toString()
        if(phoneNumber.isNotEmpty()){
            val phoneNumeric = phoneNumber.substring(1 until phoneNumber.length)
            Log.d("Numeric","$phoneNumeric , $phoneNumber")
            if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length < 13 || phoneNumeric.toBigIntegerOrNull() == null) {
                phone_input.error = "Invalid phone number."
                return false
            }
            return true
        }else{
            phone_input.error = "Invalid phone number."
            return false
        }
    }


}
