package com.eliasasskali.tfg.android.core.di

import android.content.Context
import com.eliasasskali.tfg.android.data.repository.AuthRepository
import com.eliasasskali.tfg.android.data.repository.AuthRepositoryImp
import com.eliasasskali.tfg.android.ui.features.completeProfile.CompleteProfileViewModel
import com.eliasasskali.tfg.android.ui.features.completeProfile.MapViewModel
import com.eliasasskali.tfg.android.ui.features.loginSignup.LoginViewModel
import com.eliasasskali.tfg.ui.error.ErrorHandler
import com.eliasasskali.tfg.ui.executor.Executor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { Executor() }
    single { ErrorHandler(get()) }
    viewModel { LoginViewModel(get(), get(), get()) }
    viewModel { CompleteProfileViewModel() }
    viewModel { MapViewModel() }
}

fun dataModule(context: Context) = module {
    single<AuthRepository> {
        AuthRepositoryImp(
            auth = get()
        )
    }
}

val remoteModule = module {
    single<FirebaseAuth> { Firebase.auth }
}