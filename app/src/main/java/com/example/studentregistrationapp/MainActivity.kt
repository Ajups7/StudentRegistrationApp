package com.example.studentregistrationapp

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.studentregistrationapp.StudentDatabase
import com.example.studentregistrationapp.ui.theme.StudentRegistrationAppTheme

class MainActivity : ComponentActivity() {
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            StudentDatabase::class.java,
            "students.db"
        ).build()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudentRegistrationAppTheme {
                Navigation(db)

            }
        }
    }
}

object PermissionHelper {
    val CAMERAX_PERMISSIONS = arrayOf(
        android.Manifest.permission.CAMERA
    )
}

fun hasRequiredPermissions(applicationContext: Context): Boolean {
    return PermissionHelper.CAMERAX_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            applicationContext,
            it
        ) == PackageManager.PERMISSION_GRANTED
    }

}

