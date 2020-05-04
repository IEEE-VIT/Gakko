package com.benrostudios.gakko

import android.app.Application
import com.benrostudios.gakko.data.network.service.CreateClassroomService
import com.benrostudios.gakko.data.repository.AuthRepository
import com.benrostudios.gakko.data.repository.AuthRepositoryImpl
import com.benrostudios.gakko.data.repository.ClassroomRepository
import com.benrostudios.gakko.data.repository.ClassroomRepositoryImpl
import com.benrostudios.gakko.internal.Utils
import com.benrostudios.gakko.ui.auth.setup.UserSetUpViewModelFactory
import com.benrostudios.gakko.ui.auth.signin.SignInViewModelFactory
import com.benrostudios.gakko.ui.classroom.classroomdisplay.ClassroomDisplayViewModelFactory
import com.benrostudios.gakko.data.repository.*
import com.benrostudios.gakko.ui.auth.verification.VerificationViewModelFactory
import com.benrostudios.gakko.ui.chat.chatdisplay.ChatInterfaceViewModelFactory
import com.benrostudios.gakko.ui.classroom.createclassroom.CreateClassroomViewModelFactory
import com.benrostudios.gakko.ui.classroom.joinclassroom.JoinClassroomViewModelFactory
import com.benrostudios.gakko.ui.home.comments.CommentViewModelFactory
import com.benrostudios.gakko.ui.home.material.MaterialViewModelFactory
import com.benrostudios.gakko.ui.home.members.MembersViewModelFactory
import com.benrostudios.gakko.ui.home.notifications.NotificationsViewModelFactory
import com.benrostudios.gakko.ui.home.threads.ThreadsViewModelFactory
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
        bind<ThreadsRepository>() with singleton { ThreadsRepositoryImpl() }
        bind<CommentsRepository>() with singleton { CommentsRepositoryImpl() }
        bind<MaterialsRepository>() with singleton { MaterialsRepositoryImpl() }
        bind() from singleton { Utils(instance()) }
        bind() from singleton { CreateClassroomService() }
        bind<ClassroomRepository>() with singleton {
            ClassroomRepositoryImpl(
                instance(),
                instance()
            )
        }
        bind<MembersRepository>() with singleton { MembersRepositoryImpl() }
        bind<ChatRepository>() with singleton { ChatRepositoryImpl(instance()) }
        bind<NotificationsRepo>() with singleton { NotificationsRepoImpl() }
        bind() from provider { SplashViewModelFactory(instance()) }
        bind() from provider { VerificationViewModelFactory(instance()) }
        bind() from provider { SignInViewModelFactory(instance()) }
        bind() from provider { UserSetUpViewModelFactory(instance()) }
        bind() from provider { ClassroomDisplayViewModelFactory(instance()) }
        bind() from provider { ThreadsViewModelFactory(instance()) }
        bind() from provider { CreateClassroomViewModelFactory(instance()) }
        bind() from provider { JoinClassroomViewModelFactory(instance()) }
        bind() from provider { MembersViewModelFactory(instance(), instance()) }
        bind() from provider { ChatInterfaceViewModelFactory(instance()) }
        bind() from provider { CommentViewModelFactory(instance(), instance())}
        bind() from provider { NotificationsViewModelFactory(instance()) }
        bind() from provider { MaterialViewModelFactory(instance())}
    }
}