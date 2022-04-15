package com.eliasasskali.tfg.android.ui.features.completeProfile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.rememberNavController
import com.eliasasskali.tfg.android.data.repository.AuthRepository
import com.eliasasskali.tfg.android.navigation.CompleteProfileNavigation
import com.eliasasskali.tfg.android.ui.theme.AppTheme
import org.koin.androidx.compose.get

class CompleteProfileActivity: AppCompatActivity() {
    companion object {
        fun intent(context: Context): Intent = Intent(context, CompleteProfileActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val navController = rememberNavController()
                val authRepository : AuthRepository = get()
                CompleteProfileNavigation(this, authRepository, navController)
            }
        }
    }

    private val REQUEST_CODE = 200

    private fun openGalleryForImages() {
        var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        ActivityCompat.startActivityForResult(this, intent, REQUEST_CODE, null)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){

            // if multiple images are selected
            if (data?.clipData != null) {
                var count = data.clipData!!.itemCount

                for (i in 0 until count) {
                    var imageUri: Uri = data.clipData!!.getItemAt(i).uri
                    // TODO: iv_image.setImageURI(imageUri) Here you can assign your Image URI to the ImageViews
                }

            } else if (data?.data != null) {
                // if single image is selected

                var imageUri: Uri = data.data!!
                // TODO: iv_image.setImageURI(imageUri) Here you can assign the picked image uri to your imageview

            }
        }
    }
}