package com.eliasasskali.tfg.android.ui.features.bottomNavBar

import com.eliasasskali.tfg.android.R
import com.eliasasskali.tfg.android.navigation.HomeRoutesAthlete
import com.eliasasskali.tfg.android.navigation.HomeRoutesClub

sealed class BottomNavItem(var title: String, var icon: Int, var screen_route: String) {
    object HomeClub : BottomNavItem("Home", R.drawable.ic_home, HomeRoutesClub.Home.routeName)
    object AddPost: BottomNavItem("Post",R.drawable.ic_add, HomeRoutesClub.Post.routeName)
    object ChatsClub: BottomNavItem("Chats",R.drawable.ic_chat, HomeRoutesClub.Chats.routeName)
    object ProfileClub: BottomNavItem("Profile",R.drawable.ic_person, HomeRoutesClub.Profile.routeName)

    object HomeAthlete : BottomNavItem("Home", R.drawable.ic_home, HomeRoutesAthlete.Home.routeName)
    object Clubs: BottomNavItem("Clubs",R.drawable.ic_group, HomeRoutesAthlete.Clubs.routeName)
    object ChatsAthlete: BottomNavItem("Chats",R.drawable.ic_chat, HomeRoutesAthlete.Chats.routeName)
    object ProfileAthlete: BottomNavItem("Profile",R.drawable.ic_person, HomeRoutesAthlete.Profile.routeName)
}
