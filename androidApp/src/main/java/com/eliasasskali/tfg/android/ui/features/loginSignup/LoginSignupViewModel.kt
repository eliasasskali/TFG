package com.eliasasskali.tfg.android.ui.features.loginSignup

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.eliasasskali.tfg.android.core.ui.RootViewModel
import com.eliasasskali.tfg.android.data.repository.AuthRepository
import com.eliasasskali.tfg.model.DomainError
import com.eliasasskali.tfg.ui.error.ErrorHandler
import com.eliasasskali.tfg.ui.executor.Executor
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.launch

private const val TAG = "LoginViewModel"

class LoginSignupViewModel constructor(
    private val authRepository: AuthRepository,
    executor: Executor,
    errorHandler: ErrorHandler,
) : RootViewModel(executor, errorHandler) {

    val state: MutableState<LoginState> = mutableStateOf(LoginState())

    // Setters
    fun setUserEmail(email: String) {
        state.value = state.value.copy(userEmail = email)
    }

    fun setPassword(password: String) {
        state.value = state.value.copy(password = password)
    }

    fun setHasCompletedProfile(hasCompletedProfile: Boolean) {
        state.value = state.value.copy(hasCompletedProfile = hasCompletedProfile)
    }

    fun setError(error: String) {
        state.value = state.value.copy(error = error)
    }

    fun setIsLoggedIn(isLoggedIn: Boolean) {
        state.value = state.value.copy(isLoggedIn = isLoggedIn)
    }

    init {
        setIsLoggedIn(isLoggedIn())
    }

    fun onTriggerEvent(event: LoginSignUpEvent) {
        when (event) {
            is LoginSignUpEvent.OnEmailLoginButtonClicked -> signInWithEmailAndPassword(event.onUserLogged)
            is LoginSignUpEvent.OnGoogleLoginButtonClicked -> signInWithGoogleToken(event.token, event.onUserLogged)
            is LoginSignUpEvent.OnSignUpEmailButtonClicked -> createUserWithEmailAndPassword(event.onUserLogged)
        }
    }

    private fun createUserWithEmailAndPassword(onUserLogged: () -> Unit) = viewModelScope.launch {
        execute {
            authRepository.createUserWithEmailAndPassword(
                state.value.userEmail,
                state.value.password
            )
        }.fold(
            error = {
                setError(errorHandler.convert(it))
            },
            success = {
                it.addOnCompleteListener { task ->
                    signInCompleteTask(task, onUserLogged)
                }
            }
        )
    }

    private fun signInWithEmailAndPassword(onUserLogged: () -> Unit) = viewModelScope.launch {
        execute {
            authRepository.signInWithEmailAndPassword(
                state.value.userEmail,
                state.value.password,
            )
        }.fold(
            error = {
                setError(errorHandler.convert(it))
                Log.d(TAG, state.value.error)
            },
            success = {
                it.addOnCompleteListener { task ->
                    signInCompleteTask(task, onUserLogged)
                }
            }
        )
    }

    fun signInWithGoogleToken(token: String, onUserLogged: () -> Unit) = viewModelScope.launch {
        execute {
            authRepository.signInWithGoogleToken(token)
        }.fold(
            error = {
                setError(errorHandler.convert(it))
                Log.d(TAG, state.value.error)
            },
            success = {
                it.addOnCompleteListener { task ->
                    signInCompleteTask(task, onUserLogged)
                }
            }
        )
    }

    fun signOut() = viewModelScope.launch {
        authRepository.signOut().fold(
            error = {
                setError(errorHandler.convert(it))
                Log.d(TAG, state.value.error)
            },
            success = {
                setIsLoggedIn(false)
            }
        )
    }

    private fun isLoggedIn(): Boolean {
        return authRepository.isUserLoggedIn()
    }

    private fun signInCompleteTask(task: Task<AuthResult>, onUserLogged: () -> Unit) = viewModelScope.launch {
        if (task.isSuccessful) {
            onUserLogged()
        } else {
            // TODO : Change behavior on failure
            setError(errorHandler.convert(DomainError.SignInError))
            setIsLoggedIn(isLoggedIn())
        }
    }
}

sealed class LoginSignUpEvent {
    data class OnEmailLoginButtonClicked(val onUserLogged: () -> Unit) : LoginSignUpEvent()
    data class OnGoogleLoginButtonClicked(val token: String, val onUserLogged: () -> Unit) : LoginSignUpEvent()
    data class OnSignUpEmailButtonClicked(val onUserLogged: () -> Unit) : LoginSignUpEvent()
}