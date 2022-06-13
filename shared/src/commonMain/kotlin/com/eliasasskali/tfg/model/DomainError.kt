package com.eliasasskali.tfg.model

sealed class DomainError {
    // DEFAULT ERRORS
    object NoInternet : DomainError()
    object NotFound : DomainError()
    object ServiceError : DomainError()

    // SIGN IN ERRORS
    object InvalidCredentials : DomainError()
    object SignUpError : DomainError()
    object SignUpUserExistsError : DomainError()
    object SignInError : DomainError()
    object SignInCredentialError : DomainError()
    object SignInGoogleError : DomainError()
    object SignOutError : DomainError()
    object SignUpPasswordsDoNotMatch : DomainError()

    // CLUBS ERRORS
    object LoadClubsError : DomainError()
    object UploadImagesError : DomainError()
    object UpdateProfileError : DomainError()
    object LoadProfileError : DomainError()

    // ATHLETE ERRORS
    object FollowError : DomainError()
    object UnfollowError : DomainError()
    object GetFollowedClubsError : DomainError()

    // POSTS ERRORS
    object CreatePostError : DomainError()
    object EditPostError : DomainError()
    object GetPostsError : DomainError()
    object DeletePostError : DomainError()

    // CHATS ERRORS
    object CreateChatError : DomainError()
    object SendMessageError : DomainError()


    object ErrorPostingReview : DomainError()

    data class ErrorNotHandled(val message: String) : DomainError()
}

object Success