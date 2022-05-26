package com.eliasasskali.tfg.android.ui.features.clubs

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.eliasasskali.tfg.android.navigation.AthleteNavigation
import com.eliasasskali.tfg.android.navigation.ClubNavigation
import com.eliasasskali.tfg.android.ui.theme.AppTheme
import com.eliasasskali.tfg.data.preferences.Preferences
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.koin.android.ext.android.get

class HomeActivity : AppCompatActivity() {
    companion object {
        fun intent(context: Context): Intent = Intent(context, HomeActivity::class.java)
    }
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val preferences: Preferences = get()
                val navController = rememberNavController()
                if (preferences.isClub()) {
                    ClubNavigation(navController, this)
                } else {
                    AthleteNavigation(navController, this)
                }
            }
        }
        if (ContextCompat.checkSelfPermission(this@HomeActivity,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@HomeActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this@HomeActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            } else {
                ActivityCompat.requestPermissions(this@HomeActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }
        } else {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location? ->
                    Toast.makeText(this, "${location?.latitude}, ${location?.longitude}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(this@HomeActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION) ==
                                PackageManager.PERMISSION_GRANTED)) {
                        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                        fusedLocationClient.lastLocation
                            .addOnSuccessListener { location : Location? ->
                                Toast.makeText(this, "${location?.latitude}, ${location?.longitude}", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "${it.localizedMessage}", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }
}