package com.eliasasskali.tfg.android.core.di

import android.content.Context
import androidx.paging.PagingConfig
import com.eliasasskali.tfg.android.data.repository.*
import com.eliasasskali.tfg.android.data.repository.AuthRepository
import com.eliasasskali.tfg.android.data.repository.AuthRepositoryImp
import com.eliasasskali.tfg.android.data.repository.ClubAthleteRepository
import com.eliasasskali.tfg.android.ui.features.clubDetail.ClubDetailViewModel
import com.eliasasskali.tfg.android.ui.features.clubProfile.ClubProfileViewModel
import com.eliasasskali.tfg.android.ui.features.completeProfile.CompleteProfileViewModel
import com.eliasasskali.tfg.android.ui.features.completeProfile.MapViewModel
import com.eliasasskali.tfg.android.ui.features.loginSignup.LoginSignupViewModel
import com.eliasasskali.tfg.android.ui.features.splash.SplashViewModel
import com.eliasasskali.tfg.android.ui.features.clubs.ClubsViewModel
import com.eliasasskali.tfg.android.ui.features.editClubProfile.EditClubProfileViewModel
import com.eliasasskali.tfg.android.ui.features.post.PostViewModel
import com.eliasasskali.tfg.android.ui.features.posts.PostsViewModel
import com.eliasasskali.tfg.data.preferences.CommonPreferences
import com.eliasasskali.tfg.data.preferences.Preferences
import com.eliasasskali.tfg.ui.error.ErrorHandler
import com.eliasasskali.tfg.ui.executor.Executor
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import com.russhwolf.settings.AndroidSettings

val appModule = module {
    single { Executor() }
    single { ErrorHandler(get()) }
    viewModel { LoginSignupViewModel(get(), get(), get()) }
    viewModel { CompleteProfileViewModel(get(), get(), get()) }
    viewModel { ClubsViewModel(get(), get(), get()) }
    viewModel { ClubDetailViewModel(get(), get()) }
    viewModel { EditClubProfileViewModel(get(), get(), get()) }
    viewModel { MapViewModel() }
    viewModel { SplashViewModel(get(), get(), get(), get()) }
    viewModel { ClubProfileViewModel(get(), get(), get(), get())}
    viewModel { PostViewModel(get(), get(), get(), get()) }
    viewModel { PostsViewModel(get(), get(), get()) }
}

fun dataModule(context: Context) = module {
    single<AuthRepository> {
        AuthRepositoryImp(
            auth = get()
        )
    }

    single {
        ClubAthleteRepository(
            FirebaseFirestore.getInstance().collection("Clubs")
        )
    }

    single<ClubsRepository> {
        ClubsRepositoryImpl(
            config = PagingConfig(
                pageSize = 10
            )
        )
    }

    single<PostsRepository> {
        PostsRepositoryImpl(
            config = PagingConfig(
                pageSize = 5
            )
        )
    }

    single<Preferences> {
        CommonPreferences(
            settings = AndroidSettings(
                context.getSharedPreferences(
                    "bd",
                    Context.MODE_PRIVATE
                )
            )
        )
    }
}

val remoteModule = module {
    single { Firebase.auth }
}