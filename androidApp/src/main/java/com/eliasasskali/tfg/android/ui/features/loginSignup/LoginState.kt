package com.eliasasskali.tfg.android.ui.features.loginSignup

data class LoginState(
    val isLoggedIn: Boolean = false,
    val hasCompletedProfile: Boolean = false,
    val error: String = "",
    val userEmail: String = "",
    val password: String = ""
) {
    val isValidEmailAndPassword : Boolean
        get () = !(userEmail.isBlank() || password.isBlank())
}