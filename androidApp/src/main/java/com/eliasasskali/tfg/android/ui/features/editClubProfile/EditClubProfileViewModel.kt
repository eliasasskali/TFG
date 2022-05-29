package com.eliasasskali.tfg.android.ui.features.editClubProfile

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.CountDownTimer
import android.provider.MediaStore
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.eliasasskali.tfg.android.core.ui.RootViewModel
import com.eliasasskali.tfg.android.data.repository.ClubAthleteRepository
import com.eliasasskali.tfg.data.preferences.Preferences
import com.eliasasskali.tfg.model.Club
import com.eliasasskali.tfg.model.ClubLocation
import com.eliasasskali.tfg.model.generateKeywords
import com.eliasasskali.tfg.ui.error.ErrorHandler
import com.eliasasskali.tfg.ui.executor.Executor
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import java.io.ByteArrayOutputStream
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.collections.ArrayList

class EditClubProfileViewModel(
    private val repository: ClubAthleteRepository,
    private val preferences: Preferences,
    executor: Executor,
    errorHandler: ErrorHandler
) : RootViewModel(executor, errorHandler) {
    val state: MutableState<EditClubProfileState> = mutableStateOf(EditClubProfileState())
    val location = MutableStateFlow(Location(""))
    val addressText = mutableStateOf("")
    var isMapEditable = mutableStateOf(true)
    private var timer: CountDownTimer? = null

    fun initEditClubDetailScreen(context: Context) {
        val club = Gson().fromJson(preferences.getProfileJson(), Club::class.java)
        state.value = state.value.copy(club = club)
        state.value = state.value.copy(
            name = club.name,
            description = club.description ?: "",
            contactPhone = club.contactPhone ?: "",
            contactEmail = club.contactEmail ?: "",
            services = club.services.toSet(),
            location = club.location,
            images = club.images,
            address = club.address
        )

        // Set initial location
        val loc = Location("")
        loc.latitude = club.location.latitude
        loc.longitude = club.location.longitude

        location.value = loc

        viewModelScope.launch {
            val bitmapImages = ArrayList(club.images.map { url ->
                val loader = ImageLoader(context)
                val request = ImageRequest.Builder(context)
                    .data(URLDecoder.decode(url, StandardCharsets.UTF_8.toString()))
                    .allowHardware(false)
                    .build()

                val result = (loader.execute(request) as SuccessResult).drawable
                (result as BitmapDrawable).bitmap
            })
            state.value = state.value.copy(bitmapImages = bitmapImages)
            state.value = state.value.copy(previousBitmapImages = bitmapImages)
            state.value = state.value.copy(step = EditClubProfileSteps.ShowEditClub)
        }
    }

    fun removeImage(index: Int) {
        val images = state.value.bitmapImages
        val firstPart = images.subList(0, index)
        val secondPart = images.subList(index + 1, images.size)
        state.value = state.value.copy(bitmapImages = ArrayList(firstPart + secondPart))
    }

    fun replaceImageAt(index: Int, image: Bitmap) {
        state.value.bitmapImages[index] = image // TODO: Not working if only images are replaced
    }

    fun appendImages(images: List<Bitmap>) {
        state.value = state.value.copy(bitmapImages = (state.value.bitmapImages + images) as ArrayList<Bitmap>)
    }

    fun setStep(step: EditClubProfileSteps) {
        state.value = state.value.copy(step = step)
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

    fun setServices(services: Set<String>) {
        state.value = state.value.copy(services = services)
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

    fun imagesChanged() : Boolean {
        return state.value.previousBitmapImages != state.value.bitmapImages
    }

    fun uploadNewImages(clubId: String, uriImages: List<Uri>, onUpdateFinished: () -> Unit) {
        viewModelScope.launch {
            execute {
                repository.deleteClubImages(clubId, state.value.club.images.size)
                repository.uploadImages(uriImages)
            }.fold(
                error = {

                },
                success = {
                    updatePreferencesClub(clubId, onUpdateFinished)
                }
            )
        }
    }

    fun getImageUri(context: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(context.contentResolver, inImage,  "IMG_" + Calendar.getInstance().time, null)
        return Uri.parse(path)
    }

    fun updateClub(context: Context, onUpdateFinished: () -> Unit) {
        setStep(EditClubProfileSteps.IsLoading)
        val club = state.value.club
        val clubReference = FirebaseFirestore
            .getInstance()
            .collection("Clubs")
            .document(club.id)

        if (club.name != state.value.name) {
            val keywords = generateKeywords(state.value.name)
            clubReference
                .update(
                    "name", state.value.name,
                    "keywords", keywords
                )
        }
        if (club.contactEmail != state.value.contactEmail) {
            clubReference.update("contactEmail", state.value.contactEmail)
        }
        if (club.contactPhone != state.value.contactPhone) {
            clubReference.update("contactPhone", state.value.contactPhone)
        }
        if (club.description != state.value.description) {
            clubReference.update("description", state.value.description)
        }
        if (club.services != state.value.services) {
            clubReference.update("services", state.value.services.toList())
        }
        if (club.location != state.value.location) {
            clubReference.update(
                "address", state.value.address,
                "location", state.value.location
            )
        }

        if (imagesChanged()) {
            val uriImages = state.value.bitmapImages.map {
                getImageUri(context, it)
            }
            uploadNewImages(state.value.club.id, uriImages, onUpdateFinished)
        }
    }

    private fun updatePreferencesClub(clubId: String, onUpdateFinished: () -> Unit = {}) {
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
}