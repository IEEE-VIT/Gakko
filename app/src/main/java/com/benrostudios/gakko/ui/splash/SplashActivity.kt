package com.benrostudios.gakko.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.benrostudios.gakko.R
import com.benrostudios.gakko.data.repository.FirebaseRepository
import com.benrostudios.gakko.internal.Utils
import com.benrostudios.gakko.ui.auth.AuthActivity
import com.benrostudios.gakko.ui.classroom.ClassroomActivity
import com.google.firebase.auth.FirebaseAuth
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class SplashActivity : AppCompatActivity(), KodeinAware {
    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: SplashViewModelFactory by instance()
    private lateinit var viewModel: SplashViewModel
    private val utils: Utils by instance()
    private lateinit var firebaseAuth: FirebaseAuth


    private val SPLASH_TIME_OUT = 1000L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SplashViewModel::class.java)
        initialize()
        firebaseAuth = FirebaseAuth.getInstance()

    }

    private fun initialize() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_splash)
        Handler().postDelayed(
            {
                userCheck()
            }, SPLASH_TIME_OUT
        )
    }

    private fun userCheck() {
        val phone = utils.retrieveMobile()
        if (phone == null) {
            unAutenticatedUser()
        } else {
            viewModel.getUser(phone)
            viewModel.userResponse.observe(this, Observer {
                if (it) {
                    authenticatedUser()
                } else {
                    firebaseAuth.signOut()
                    unAutenticatedUser()
                }
            })
        }
    }

    private fun unAutenticatedUser() {
        val i = Intent(this, AuthActivity::class.java)
        startActivity(i)
        finish()
    }

    private fun authenticatedUser() {
        val i = Intent(this, ClassroomActivity::class.java)
        startActivity(i)
        finish()
    }

}
