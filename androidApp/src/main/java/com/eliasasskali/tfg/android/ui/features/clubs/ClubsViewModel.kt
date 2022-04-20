package com.eliasasskali.tfg.android.ui.features.clubs

import android.app.Activity
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.CountDownTimer
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.eliasasskali.tfg.android.core.ui.RootViewModel
import com.eliasasskali.tfg.android.data.repository.ClubsRepository
import com.eliasasskali.tfg.model.ClubLocation
import com.eliasasskali.tfg.ui.error.ErrorHandler
import com.eliasasskali.tfg.ui.executor.Executor
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.MutableStateFlow
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

class ClubsViewModel(
    private val repository: ClubsRepository,
    executor: Executor,
    errorHandler: ErrorHandler
) : RootViewModel(executor, errorHandler) {

    val state: MutableState<ClubsState> = mutableStateOf(ClubsState())
    var clubs = repository.getClubs(state.value.searchString).cachedIn(viewModelScope)
    val location = MutableStateFlow(getInitialLocation())
    val addressText = mutableStateOf("")
    var isMapEditable = mutableStateOf(true)
    var timer: CountDownTimer? = null

    fun setError(error: String) {
        state.value = state.value.copy(error = error)
    }

    fun setSearchString(searchString: String) {
        state.value = state.value.copy(searchString = searchString)
        clubs = repository.getClubs(state.value.searchString, state.value.sportFilters).cachedIn(viewModelScope)
    }

    fun setSportFilters(sports: List<String>) {
        state.value = state.value.copy(sportFilters = sports)
        clubs = repository.getClubs(state.value.searchString, state.value.sportFilters).cachedIn(viewModelScope)
    }

    fun setStep(step: ClubListSteps) {
        state.value = state.value.copy(step = step)
    }

    fun setIsLoading(isLoading: Boolean) {
        state.value = state.value.copy(isLoading = isLoading)
    }

    fun setFilterLocationRadius(filterLocationRadius: Int) {
        state.value = state.value.copy(filterLocationRadius = filterLocationRadius)
    }

    fun setFilterLocationCity(filterLocationCity: String) {
        state.value = state.value.copy(filterLocationCity = filterLocationCity)
    }

    fun setFilterLocation(filterLocation: Location) {
        state.value = state.value.copy(filterLocation = filterLocation)
    }

    fun getInitialLocation() : Location {
        val initialLocation = Location("")
        initialLocation.latitude = 51.506874
        initialLocation.longitude = -0.139800
        return initialLocation
    }

    fun setUserLocation(context: Activity) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                location?.let {
                    state.value = state.value.copy(userLocation = it)
                }
            }

    }

    fun distanceToClub(clubLocation: ClubLocation, userLocation: Location) : Float {
        val clubLoc = Location("")
        clubLoc.longitude = clubLocation.longitude
        clubLoc.latitude = clubLocation.latitude

        val distance = clubLoc.distanceTo(userLocation)
        val df = DecimalFormat("#.#")
        df.roundingMode = RoundingMode.UP
        return df.format(distance/1000).toFloat()
    }

    fun updateLocation(latitude: Double, longitude: Double){
        if(latitude != location.value.latitude) {
            val location = Location("")
            location.latitude = latitude
            location.longitude = longitude
            setLocation(location)
        }
    }

    fun setLocation(loc: Location) {
        location.value = loc
    }

    fun getAddressFromLocation(context: Context): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        var addresses: List<Address>? = null
        val address: Address?
        var addressText = ""

        try {
            addresses = geocoder.getFromLocation(location.value.latitude, location.value.longitude, 1)
        }catch(ex: Exception){
            ex.printStackTrace()
        }

        address = addresses?.get(0)
        if (address != null) {
            addressText = "${address.locality ?: address.subAdminArea}, ${address.countryName}"
        }


        return addressText
    }

    fun onTextChanged(context: Context, text: String){
        if(text == "")
            return
        timer?.cancel()
        timer = object : CountDownTimer(1000, 1500) {
            override fun onTick(millisUntilFinished: Long) { }
            override fun onFinish() {
                location.value = getLocationFromAddress(context, text)
            }
        }.start()
    }

    fun getLocationFromAddress(context: Context, strAddress: String): Location {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address>?
        val address: Address?

        addresses = geocoder.getFromLocationName(strAddress, 1)

        if (addresses.isNotEmpty()) {
            address = addresses[0]

            var loc = Location("")
            loc.latitude = address.getLatitude()
            loc.longitude = address.getLongitude()
            return loc
        }

        return location.value
    }
}