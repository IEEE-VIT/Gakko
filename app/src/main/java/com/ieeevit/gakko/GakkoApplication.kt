package com.ieeevit.gakko

import android.app.Application
import com.ieeevit.gakko.data.network.service.CreateClassroomService
import com.ieeevit.gakko.data.repository.AuthRepository
import com.ieeevit.gakko.data.repository.AuthRepositoryImpl
import com.ieeevit.gakko.data.repository.ClassroomRepository
import com.ieeevit.gakko.data.repository.ClassroomRepositoryImpl
import com.ieeevit.gakko.internal.Utils
import com.ieeevit.gakko.ui.auth.setup.UserSetUpViewModelFactory
import com.ieeevit.gakko.ui.auth.signin.SignInViewModelFactory
import com.ieeevit.gakko.ui.classroom.classroomdisplay.ClassroomDisplayViewModelFactory
import com.ieeevit.gakko.data.repository.*
import com.ieeevit.gakko.ui.auth.verification.VerificationViewModelFactory
import com.ieeevit.gakko.ui.chat.chatdisplay.ChatInterfaceViewModelFactory
import com.ieeevit.gakko.ui.classroom.createclassroom.CreateClassroomViewModelFactory
import com.ieeevit.gakko.ui.classroom.joinclassroom.JoinClassroomViewModelFactory
import com.ieeevit.gakko.ui.home.comments.CommentViewModelFactory
import com.ieeevit.gakko.ui.home.homehost.HomeHostViewModelFactory
import com.ieeevit.gakko.ui.home.material.MaterialViewModelFactory
import com.ieeevit.gakko.ui.home.members.MembersViewModelFactory
import com.ieeevit.gakko.ui.home.notifications.NotificationsViewModelFactory
import com.ieeevit.gakko.ui.home.pinboard.PinboardViewModelFactory
import com.ieeevit.gakko.ui.home.proflie.ProfileViewModelFactory
import com.ieeevit.gakko.ui.home.threads.ThreadsViewModelFactory
import com.ieeevit.gakko.ui.home.todo.TodoViewModelFactory
import com.ieeevit.gakko.ui.splash.SplashViewModelFactory
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
        bind<AuthRepository>() with singleton { AuthRepositoryImpl(instance()) }
        bind<ThreadsRepository>() with singleton { ThreadsRepositoryImpl() }
        bind<CommentsRepository>() with singleton { CommentsRepositoryImpl() }
        bind<MaterialsRepository>() with singleton { MaterialsRepositoryImpl() }
        bind<PinboardRepository>() with singleton { PinboardRepositoryImpl() }
        bind<ProfileRepository>() with singleton { ProfileRepositoryImpl(instance()) }
        bind<TodoRepository>() with singleton { TodoRepositoryImpl() }
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
        bind() from provider { TodoViewModelFactory(instance()) }
        bind() from provider { PinboardViewModelFactory(instance()) }
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
        bind() from provider { ProfileViewModelFactory(instance())}
        bind() from provider { HomeHostViewModelFactory()}
    }
}