package com.eliasasskali.tfg.model

sealed class DomainError {
    object NoInternet : DomainError()
    object NotFound : DomainError()

    object InvalidCredentials : DomainError()
    object SignUpError : DomainError()
    object SignUpUserExistsError : DomainError()
    object SignInError : DomainError()
    object SignInCredentialError : DomainError()
    object SignInGoogleError : DomainError()
    object SignOutError : DomainError()
    object SignUpPasswordsDoNotMatch : DomainError()

    object LoadClubsError : DomainError()

    object CreatePostError : DomainError()

    object ErrorPostingReview : DomainError()

    data class ErrorNotHandled(val message: String) : DomainError()
}

object Success