package com.eliasasskali.tfg.android.ui.features.drawer

import com.eliasasskali.tfg.android.R
import com.eliasasskali.tfg.android.navigation.HomeRoutesClub


sealed class NavDrawerItem(var route: String, var icon: Int, var title: Int) {
    object ClubListItem: NavDrawerItem(route = HomeRoutesClub.Home.routeName, icon = R.drawable.maps_sv_error_icon, title = R.string.home)
    object ChatsItem: NavDrawerItem(route = "NOT IMPLEMENTED", icon = R.drawable.maps_sv_error_icon, title = R.string.chats)
    object ConfigurationItem: NavDrawerItem(route = "NOT IMPLEMENTED", icon = R.drawable.maps_sv_error_icon, title = R.string.configuration)
    object LogOutItem : NavDrawerItem(route = "LogOut", icon = R.drawable.maps_icon_direction, title = R.string.log_out)
}