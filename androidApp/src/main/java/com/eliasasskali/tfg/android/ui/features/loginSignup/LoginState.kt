package com.eliasasskali.tfg.android.ui.features.loginSignup

data class LoginState(
    val hasCompletedProfile: Boolean = false,
    val error: String = "",
    val userEmail: String = "",
    val password: String = "",
    val confirmPassword: String = ""
) {
    val isValidEmailAndPassword : Boolean
        get () = !(userEmail.isBlank() || password.isBlank())

    val isValidEmailAndPasswordSignUp : Boolean
        get () = !(userEmail.isBlank() || password.isBlank() || confirmPassword.isBlank())

    val doPasswordsMatch : Boolean
        get() = (password == confirmPassword)
}