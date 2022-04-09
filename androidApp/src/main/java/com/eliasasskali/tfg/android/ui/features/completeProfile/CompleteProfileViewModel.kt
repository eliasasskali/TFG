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
import com.eliasasskali.tfg.android.core.ui.RootViewModel
import com.eliasasskali.tfg.android.data.repository.ClubAthleteRepository
import com.eliasasskali.tfg.model.Club
import com.eliasasskali.tfg.model.AthleteDto
import com.eliasasskali.tfg.ui.error.ErrorHandler
import com.eliasasskali.tfg.ui.executor.Executor
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*

private const val TAG = "RegisterViewModel"

class CompleteProfileViewModel(
    private val repository: ClubAthleteRepository,
    executor: Executor,
    errorHandler: ErrorHandler
) : RootViewModel(executor, errorHandler) {

    val state: MutableState<CompleteProfileState> = mutableStateOf(CompleteProfileState())
    val location = MutableStateFlow(getInitialLocation())
    val addressText = mutableStateOf("")
    var isMapEditable = mutableStateOf(true)
    var timer: CountDownTimer? = null

    // Setters
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

    fun setServices(services: Set<String>) {
        state.value = state.value.copy(services = services)
    }

    fun setClubImages(images: List<Uri>) {
        state.value = state.value.copy(clubImages = images)
    }

    fun isValidName(): Boolean {
        if (state.value.name.isBlank()) {
            return false
        }
        return true
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
        state.value = state.value.copy(location = loc)
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
                location.value = getLocationFromAddress(context, text)
                state.value = state.value.copy(
                    location = getLocationFromAddress(context, text),
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
        try {
            state.value = state.value.copy(error = "")
            val uid = Firebase.auth.currentUser?.uid
            val db = Firebase.firestore
            if (state.value.isClub) {
                val club = Club(
                    state.value.name,
                    state.value.contactEmail,
                    state.value.contactPhone,
                    state.value.description,
                    state.value.address,
                    state.value.location,
                    state.value.services.toList()
                )
                uid?.let {
                    db.collection("Clubs").document(uid).set(club.toModel()).addOnCompleteListener {
                        if (it.isSuccessful) {
                            onCompleteProfileSuccess()
                            Log.d(TAG, "Club Profile Completed")
                        } else {
                            Log.d(TAG, "Could not complete club profile")
                        }
                    }
                    uploadImages(state.value.clubImages)
                }
            } else {
                val user = AthleteDto(
                    state.value.name,
                    state.value.services.toList()
                )
                uid?.let {
                    db.collection("Users").document(uid).set(user).addOnCompleteListener {
                        if (it.isSuccessful) {
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

    private fun uploadImages(clubImages: List<Uri>) {
        repository.uploadImages(clubImages)
    }
}