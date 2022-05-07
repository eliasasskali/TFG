package com.eliasasskali.tfg.data.preferences

interface Preferences {
    fun saveProfileJson(profileJson: String)
    fun getProfileJson(): String
    fun saveIsClub(isClub: Boolean)
    fun isClub(): Boolean
    fun saveLoggedUid(uid: String)
    fun getLoggedUid(): String
}