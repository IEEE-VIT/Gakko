package com.benrostudios.gakko.ui.auth.signin


import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
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
    private var verificationInProgress: Boolean = false
    private val SIGN_IN_FRAG = "SignInFragment"
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private  var verificationToken: String? = null
    private lateinit var phoneNumber: String
    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: SignInViewModelFactory by instance()
    private val utils: Utils by instance()

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
                getAuthStatus()
                initiateSignIn(phoneNumber)
            }
        }
        verification_button.setOnClickListener {
            if(verificationToken!=null) {
                verifyPhoneNumberWithCode(otp_input.text.toString())
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(SignInViewModel::class.java)

        if (savedInstanceState != null) {
            verificationInProgress = savedInstanceState.getBoolean(SIGN_IN_FRAG)
        }


    }

    override fun onStart() {
        super.onStart()
        if (verificationInProgress && validatePhoneNumber()) {
            initiateSignIn(phone_input.text.toString())
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(SIGN_IN_FRAG, verificationInProgress)
    }

    var callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {

            Log.d(SIGN_IN_FRAG, "onVerificationCompleted:$credential")

            signInWithFirebase(credential)

        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w(SIGN_IN_FRAG, "onVerificationFailed", e)

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                // ...
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                // ...
            }

        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d(SIGN_IN_FRAG, "onCodeSent:$verificationId")

            // Save verification ID and resending token so we can use them later
            verificationToken = verificationId
            resendToken = token


        }
    }

    private fun initiateSignIn(phoneNumber: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            60,
            TimeUnit.SECONDS,
            requireActivity(),
            callbacks
        )
        verificationInProgress = true
    }

    private fun signInWithFirebase(credential: PhoneAuthCredential) = launch {
        viewModel.signInWithFirebase(credential)
    }

    private fun getAuthStatus() = launch {
        viewModel.response.observe(viewLifecycleOwner, Observer {
            if (it) {
                getUserResponse()
            } else {
                Toast.makeText(context, "Error", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getUserResponse() = launch {
        viewModel.getUser(phoneNumber)
        viewModel.userResponse.observe(viewLifecycleOwner, Observer {check ->
            if(!check){
                navController.navigate(R.id.action_signIn_to_userSetUp)
                utils.saveMobile(phoneNumber)
            }else{
                //Code To Go to Classroom
                val intent = Intent(context,ClassroomActivity::class.java)
                startActivity(intent)
            }
        })
    }

    private fun verifyPhoneNumberWithCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationToken!!, code)
        signInWithFirebase(credential)
    }

    private fun resendVerificationCode(
        phoneNumber: String,
        token: PhoneAuthProvider.ForceResendingToken?
    ) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            60,
            TimeUnit.SECONDS,
            requireActivity(),
            callbacks,
            token
        )
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
