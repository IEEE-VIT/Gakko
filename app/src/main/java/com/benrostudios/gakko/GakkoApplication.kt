package com.benrostudios.gakko

import android.app.Application
import com.benrostudios.gakko.data.repository.AuthRepository
import com.benrostudios.gakko.data.repository.AuthRepositoryImpl
import com.benrostudios.gakko.data.repository.FirebaseRepository
import com.benrostudios.gakko.data.repository.FirebaseRepositoryImpl
import com.benrostudios.gakko.internal.Utils
import com.benrostudios.gakko.ui.auth.setup.UserSetUpViewModelFactory
import com.benrostudios.gakko.ui.auth.signin.SignInViewModelFactory
import com.benrostudios.gakko.ui.splash.SplashActivity
import com.benrostudios.gakko.ui.splash.SplashViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class GakkoApplication : Application(), KodeinAware {

    override val kodein: Kodein = Kodein.lazy {
        import(androidXModule(this@GakkoApplication))
        bind<AuthRepository>() with singleton { AuthRepositoryImpl() }
        bind<FirebaseRepository>() with singleton { FirebaseRepositoryImpl() }
        bind() from singleton { Utils(instance()) }
        bind() from provider { SplashViewModelFactory(instance())}
        bind() from provider { SignInViewModelFactory(instance(),instance()) }
        bind() from provider { UserSetUpViewModelFactory(instance()) }

    }
}