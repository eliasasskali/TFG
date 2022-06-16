package com.eliasasskali.tfg.android.ui.features.completeProfile

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.CountDownTimer
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.eliasasskali.tfg.android.R
import com.eliasasskali.tfg.android.core.ui.RootViewModel
import com.eliasasskali.tfg.android.data.repository.clubAthlete.ClubAthleteRepository
import com.eliasasskali.tfg.data.preferences.Preferences
import com.eliasasskali.tfg.model.Club
import com.eliasasskali.tfg.model.AthleteDto
import com.eliasasskali.tfg.model.ClubLocation
import com.eliasasskali.tfg.ui.error.ErrorHandler
import com.eliasasskali.tfg.ui.executor.Executor
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*

private const val TAG = "RegisterViewModel"

class CompleteProfileViewModel(
    private val repository: ClubAthleteRepository,
    private val preferences: Preferences,
    executor: Executor,
    errorHandler: ErrorHandler,
) : RootViewModel(executor, errorHandler) {

    val state: MutableState<CompleteProfileState> = mutableStateOf(CompleteProfileState())
    val location = MutableStateFlow(getInitialLocation())
    val addressText = mutableStateOf("")
    var isMapEditable = mutableStateOf(true)
    var timer: CountDownTimer? = null

    // Setters
    fun resetState() {
        state.value = CompleteProfileState()
    }

    fun setName(name: String) {
        state.value = state.value.copy(name = name)
    }

    fun setContactEmail(contactEmail: String) {
        state.value = state.value.copy(contactEmail = contactEmail)
    }

    fun setContactPhone(contactPhone: String) {
        state.value = state.value.copy(contactPhone = contactPhone)
    }

    fun setDescription(description: String) {
        state.value = state.value.copy(description = description)
    }

    fun setAddress(address: String) {
        state.value = state.value.copy(address = address)
    }

    fun setError(error: String) {
        state.value = state.value.copy(error = error)
    }

    fun setStep(step: CompleteProfileSteps) {
        state.value = state.value.copy(step = step)
    }

    fun setServices(services: Set<String>) {
        state.value = state.value.copy(services = services)
    }

    fun setClubImages(images: List<Uri>) {
        state.value = state.value.copy(clubImages = images)
    }

    fun isValidName(): Boolean {
        return state.value.name.isNotBlank()
    }

    fun getInitialLocation(): Location {
        val initialLocation = Location("")
        initialLocation.latitude = 51.506874
        initialLocation.longitude = -0.139800
        return initialLocation
    }

    fun updateLocation(latitude: Double, longitude: Double) {
        if (latitude != location.value.latitude) {
            val location = Location("")
            location.latitude = latitude
            location.longitude = longitude
            setLocation(location)
        }
    }

    fun setLocation(loc: Location) {
        location.value = loc
        state.value = state.value.copy(location = ClubLocation(loc.latitude, loc.longitude))
    }

    fun getAddressFromLocation(context: Context): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        var addresses: List<Address>? = null

        try {
            addresses =
                geocoder.getFromLocation(location.value.latitude, location.value.longitude, 1)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        val address: Address? = addresses?.get(0)
        val addressText = address?.getAddressLine(0) ?: ""

        setAddress(address = addressText)
        return addressText
    }

    fun onTextChanged(context: Context, text: String) {
        if (text == "")
            return
        timer?.cancel()
        timer = object : CountDownTimer(1000, 1500) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                val loc = getLocationFromAddress(context, text)
                location.value = loc
                state.value = state.value.copy(
                    location = ClubLocation(loc.latitude, loc.longitude),
                    address = text
                )
            }
        }.start()
    }

    fun getLocationFromAddress(context: Context, strAddress: String): Location {
        val geocoder = Geocoder(context, Locale.getDefault())
        val address: Address?

        val addresses: List<Address> = geocoder.getFromLocationName(strAddress, 1)

        if (addresses.isNotEmpty()) {
            address = addresses[0]

            val loc = Location("")
            loc.latitude = address.latitude
            loc.longitude = address.longitude
            return loc
        }

        return location.value
    }

    fun completeProfile(onCompleteProfileSuccess: () -> Unit) = viewModelScope.launch {
        setStep(CompleteProfileSteps.IsLoading)
        try {
            state.value = state.value.copy(error = "")
            val uid = Firebase.auth.currentUser?.uid
            val db = Firebase.firestore
            if (state.value.isClub) {
                uid?.let {
                    val club = Club(
                        id = uid,
                        name = state.value.name,
                        contactEmail = state.value.contactEmail,
                        contactPhone = state.value.contactPhone,
                        description = state.value.description,
                        address = state.value.address,
                        location = state.value.location,
                        services = state.value.services.toList(),
                        schedule = state.value.schedule
                    )

                    db.collection("Clubs").document(uid).set(club.toModel()).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Log.d(TAG, "Club Profile Completed")
                        } else {
                            Log.d(TAG, "Could not complete club profile")
                        }
                    }
                    uploadImages(state.value.clubImages) {
                        updatePreferencesClub(club.id)
                        preferences.saveIsClub(true)
                        onCompleteProfileSuccess()
                    }
                }
            } else {
                val athlete = AthleteDto(
                    state.value.name,
                    state.value.services.toList()
                )
                uid?.let {
                    db.collection("Athletes").document(uid).set(athlete).addOnCompleteListener {
                        if (it.isSuccessful) {
                            preferences.saveProfileJson(Gson().toJson(athlete))
                            preferences.saveIsClub(true)
                            onCompleteProfileSuccess()
                            Log.d(TAG, "User Profile Completed")
                        } else {
                            Log.d(TAG, "Could not complete profile")
                        }
                    }
                }
            }
        } catch (e: Exception) {
            state.value = state.value.copy(error = e.localizedMessage ?: "Unknown error")
            Log.d(TAG, "Complete profile fail: $e")
        }
    }

    private fun uploadImages(clubImages: List<Uri>, onCompleteProfileSuccess: () -> Unit) {
        viewModelScope.launch {
            execute {
                repository.uploadImages(clubImages)
            }.fold(
                error = {

                },
                success = {
                    onCompleteProfileSuccess()
                }
            )
        }
    }

    fun updatePreferencesClub(clubId: String, onUpdateFinished: () -> Unit = {}) {
        viewModelScope.launch {
            execute {
                repository.getClubById(clubId)
            }.fold(
                error = {},
                success = { club ->
                    club?.let {
                        val jsonClub = Gson().toJson(it)
                        preferences.saveProfileJson(jsonClub)
                        onUpdateFinished()
                    }
                }
            )
        }
    }

    fun setSchedule(day: String, schedule: String) {
        var newSchedule = state.value.schedule
        newSchedule[day] = schedule
        state.value = state.value.copy(
            schedule = newSchedule
        )
        println(state.value.schedule)
    }

    fun convertIntToWeekdayString(weekDayInt: Int, context: Context) : String {
        return when (weekDayInt) {
            0 -> context.getString(R.string.monday)
            1 -> context.getString(R.string.tuesday)
            2 -> context.getString(R.string.wednesday)
            3 -> context.getString(R.string.thursday)
            4 -> context.getString(R.string.friday)
            5 -> context.getString(R.string.saturday)
            6 -> context.getString(R.string.sunday)
            else -> context.getString(R.string.unknown)
        }
    }
}