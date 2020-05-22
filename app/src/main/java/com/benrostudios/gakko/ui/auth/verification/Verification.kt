package com.benrostudios.gakko.ui.auth.verification

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
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
import com.benrostudios.gakko.ui.home.HomeActivity
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.verification_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.util.concurrent.TimeUnit

class Verification : ScopedFragment(), KodeinAware {
    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: VerificationViewModelFactory by instance()
    private val utils: Utils by instance()
    private var verificationInProgress: Boolean = false
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private var verificationToken: String? = null
    private lateinit var phoneNumber: String
    private lateinit var navController: NavController
    private val VERIFICATION_FRAG = "verificationfrag"
    private var lastClickTime: Long = 0L
    private var resendTimer: Handler = Handler()
    private var resendRunnable = Runnable {
        didnt_receive_sms.visibility = View.VISIBLE
    }

    companion object {
        fun newInstance() = Verification()
    }

    private lateinit var viewModel: VerificationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.verification_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(VerificationViewModel::class.java)
        if (savedInstanceState != null) {
            verificationInProgress = savedInstanceState.getBoolean(VERIFICATION_FRAG)
        }
        if(!verificationInProgress) {
            initiateSignIn(phoneNumber)
            getAuthResponse()
            resendTimer.postDelayed(resendRunnable,15000L)
            didnt_receive_sms.setOnClickListener {
                if (SystemClock.elapsedRealtime() - lastClickTime >= 60000) {
                    Toast.makeText(requireContext(), "Check your SMS. We've sent you an OTP!", Toast.LENGTH_SHORT).show()
                    lastClickTime = SystemClock.elapsedRealtime();
                    resendVerificationCode(phoneNumber,resendToken)
                } else {
                    Toast.makeText(requireContext(), "Try after 1 minute.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        verification_button.setOnClickListener {
            if (verificationToken != null) {
                verifyPhoneNumberWithCode(otp_input.text.toString())
                verfication_progress.visibility = View.VISIBLE
            }
        }

    }

    override fun onDestroy() {
        resendTimer.removeCallbacks(resendRunnable)
        super.onDestroy()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        phoneNumber = arguments?.getString("phoneNumber") ?: ""
        verification_phone_display.text = phoneNumber
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(VERIFICATION_FRAG, verificationInProgress)
    }

    private fun verifyPhoneNumberWithCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationToken!!, code)
        signInWithFirebase(credential)
    }

    override fun onStart() {
        super.onStart()
        if (verificationInProgress) {

        }
    }

    private fun signInWithFirebase(credential: PhoneAuthCredential) = launch {
        viewModel.signInWithFirebase(credential)
    }

    private fun getUserResponse() = launch {
        viewModel.getUser(phoneNumber)
        utils.saveMobile(phoneNumber)
        viewModel.userResponse.observe(viewLifecycleOwner, Observer {check ->
            if(!check){
                navController.navigate(R.id.action_verification_to_userSetUp)
                Toast.makeText(context,"You have been authenticated!",Toast.LENGTH_LONG).show()
            }else{
                //Code To Go to Classroom
                Toast.makeText(context,"You have been authenticated!",Toast.LENGTH_LONG).show()
                val intent = Intent(context, HomeActivity::class.java)
                activity?.finish()
                startActivity(intent)

            }
            navController.popBackStack()
        })
    }

    private fun getAuthResponse() = launch {
        viewModel.response.observe(viewLifecycleOwner, Observer {
            if(it){
                getUserResponse()
            }else{
                Toast.makeText(context,"Invalid OTP!",Toast.LENGTH_LONG).show()
                verfication_progress.visibility = View.GONE
            }
        })
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

    var callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {

            Log.d(VERIFICATION_FRAG, "onVerificationCompleted:$credential")

            signInWithFirebase(credential)

        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w(VERIFICATION_FRAG, "onVerificationFailed", e)

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
            Log.d(VERIFICATION_FRAG, "onCodeSent:$verificationId")

            // Save verification ID and resending token so we can use them later
            verificationToken = verificationId
            resendToken = token


        }
    }

}
