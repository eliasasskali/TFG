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
            is DomainError.SignUpPasswordsDoNotMatch -> context.getString(R.string.passwordsDoNotMatch_error)
            is DomainError.SignUpUserExistsError -> context.getString(R.string.signup_user_exists_error)
            is DomainError.LoadClubsError -> context.getString(R.string.clubs_load_error)
            is DomainError.CreatePostError -> context.getString(R.string.create_post_error)
            is DomainError.ErrorPostingReview -> context.getString(R.string.error_creating_review)
            is DomainError.DeletePostError -> context.getString(R.string.delete_post_error)
            is DomainError.EditPostError -> context.getString(R.string.edit_post_error)
            is DomainError.CreateChatError -> context.getString(R.string.create_chat_error)
            is DomainError.FollowError -> context.getString(R.string.follow_error)
            is DomainError.UnfollowError -> context.getString(R.string.unfollow_error)
            is DomainError.GetFollowedClubsError -> context.getString(R.string.get_followed_clubs_error)
            is DomainError.GetPostError -> context.getString(R.string.get_posts_error)
            is DomainError.SendMessageError -> context.getString(R.string.send_message_error)
            is DomainError.ServiceError -> context.getString(R.string.service_error)
            is DomainError.UpdateProfileError -> context.getString(R.string.update_profile_error)
            is DomainError.UploadImagesError -> context.getString(R.string.upload_images_error)
            is DomainError.ErrorNotHandled -> error.message
        }
}
