package com.eliasasskali.tfg.android.ui.features.bottomNavBar

import com.eliasasskali.tfg.android.R
import com.eliasasskali.tfg.android.navigation.HomeRoutesClub

sealed class BottomNavItem(var title: String, var icon: Int, var screen_route: String) {
    object Home : BottomNavItem("Home", R.drawable.ic_home, HomeRoutesClub.Home.routeName)
    object AddPost: BottomNavItem("Post",R.drawable.ic_add, HomeRoutesClub.Post.routeName)
    object Notifications: BottomNavItem("Notification",R.drawable.ic_notifications, HomeRoutesClub.Notifications.routeName)
    object Chats: BottomNavItem("Chats",R.drawable.ic_chat, HomeRoutesClub.Chats.routeName)
    object Profile: BottomNavItem("Profile",R.drawable.ic_person, HomeRoutesClub.Profile.routeName)
}
