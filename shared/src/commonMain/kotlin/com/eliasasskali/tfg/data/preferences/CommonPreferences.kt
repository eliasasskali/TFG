package com.eliasasskali.tfg.data.preferences

import com.russhwolf.settings.Settings

class CommonPreferences(private val settings: Settings) : Preferences {

    companion object {
        private const val PROFILE_JSON_KEY = "PROFILE_JSON_KEY"
        private const val IS_CLUB_KEY = "IS_CLUB_KEY"
        private const val UID_KEY = "UID_KEY"
        private const val EMPTY_STRING = ""
    }

    override fun saveProfileJson(profileJson: String) {
        settings.putString(PROFILE_JSON_KEY, profileJson)
    }

    override fun getProfileJson(): String = settings.getString(PROFILE_JSON_KEY, EMPTY_STRING)

    override fun saveIsClub(isClub: Boolean) {
        settings.putBoolean(IS_CLUB_KEY, isClub)
    }

    override fun isClub(): Boolean = settings.getBoolean(IS_CLUB_KEY)

    override fun saveLoggedUid(uid: String) {
        settings.putString(UID_KEY, uid)
    }

    override fun getLoggedUid(): String = settings.getString(UID_KEY, EMPTY_STRING)

}