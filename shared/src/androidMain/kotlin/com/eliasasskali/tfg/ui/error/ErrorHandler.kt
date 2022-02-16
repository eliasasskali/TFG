package com.eliasasskali.tfg.ui.error

import android.content.Context
import com.eliasasskali.tfg.R
import com.eliasasskali.tfg.model.DomainError

actual class ErrorHandler constructor(private val context: Context) {
    actual fun convert(error: DomainError): String =
        when (error) {
            is DomainError.InvalidCredentials -> context.getString(R.string.invalid_credentials)
            is DomainError.NoInternet -> context.getString(R.string.nointernet_error)
            is DomainError.NotFound -> context.getString(R.string.notfound_error)
            is DomainError.SignInCredentialError -> context.getString(R.string.signinCredential_error)
            is DomainError.SignInError -> context.getString(R.string.signin_error)
            is DomainError.SignInGoogleError -> context.getString(R.string.signinGoogle_error)
            is DomainError.SignOutError -> context.getString(R.string.signout_error)
            is DomainError.SignUpError -> context.getString(R.string.signup_error)
            is DomainError.ErrorNotHandled -> error.message
        }
}
