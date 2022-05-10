package com.eliasasskali.tfg.android.navigation

sealed class SplashRoutes(val routeName: String) {
    object Splash : SplashRoutes("splash")
}

sealed class CompleteProfileRoutes(val routeName: String) {
    object ChooseUserType : CompleteProfileRoutes("chooseUserType")
    object AthleteCompleteProfile : CompleteProfileRoutes("athleteCompleteProfile")
    object ClubCompleteProfileFirst : CompleteProfileRoutes("clubCompleteProfileFirst")
    object ClubCompleteProfileSecond : CompleteProfileRoutes("clubCompleteProfileSecond")
    object ClubCompleteProfileMap: CompleteProfileRoutes("clubCompleteProfileMap")
}

sealed class LoginSignUpRoutes(val routeName: String) {
    object Login : LoginSignUpRoutes("login")
    object EmailLogin : LoginSignUpRoutes("emailLogin")
    object SignUp : LoginSignUpRoutes("signUp")
    object Welcome : LoginSignUpRoutes("welcome")
}

sealed class HomeRoutesClub(val routeName: String) {
    object Home : HomeRoutesClub("home")
    object Notifications : HomeRoutesClub("notifications")
    object Chats : HomeRoutesClub("chats")
    object Profile : HomeRoutesClub("profile")
    object Post : HomeRoutesClub("post")
    object ClubDetail: HomeRoutesClub("clubDetail")
    object PostDetail: HomeRoutesClub("postDetail")
    object EditClubProfile: HomeRoutesClub("editClubProfile")

    companion object {
        const val JSON_POST = "jsonPost"
        const val JSON_CLUB = "jsonClub"
        const val EDIT_JSON_CLUB = "editJsonClub"
        const val DISTANCE_TO_CLUB = "distanceToClub"
    }
}

sealed class HomeRoutesAthlete(val routeName: String) {
    object Home : HomeRoutesAthlete("home")
    object Clubs : HomeRoutesAthlete("clubs")
    object Chats : HomeRoutesAthlete("chats")
    object Profile : HomeRoutesAthlete("profile")
}