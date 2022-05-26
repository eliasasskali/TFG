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
    object SignUp : LoginSignUpRoutes("signUp")
}

sealed class HomeRoutesClub(val routeName: String) {
    object Home : HomeRoutesClub("homeClub")
    object Notifications : HomeRoutesClub("notifications")
    object Chats : HomeRoutesClub("chatsClub")
    object ChatDetail : HomeRoutesAthlete("chatDetail")
    object Profile : HomeRoutesClub("profileClub")
    object Post : HomeRoutesClub("post")
    object PostDetail: HomeRoutesClub("postDetailClub")
    object EditClubProfile: HomeRoutesClub("editClubProfile")

    companion object {
        const val JSON_POST = "jsonPost"
        const val CHAT_ID = "chatId"
    }
}

sealed class HomeRoutesAthlete(val routeName: String) {
    object Home : HomeRoutesAthlete("homeAthlete")
    object Clubs : HomeRoutesAthlete("clubs")
    object Chats : HomeRoutesAthlete("chatsAthlete")
    object ChatDetail : HomeRoutesAthlete("chatDetail")
    object Profile : HomeRoutesAthlete("profileAthlete")
    object ClubDetail: HomeRoutesAthlete("clubDetail")
    object PostDetail: HomeRoutesAthlete("postDetailAthlete")


    companion object {
        const val CHAT_ID = "chatId"
        const val JSON_POST = "jsonPost"
        const val CLUB_ID = "clubId"
        const val DISTANCE_TO_CLUB = "distanceToClub"
    }
}