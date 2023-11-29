package com.example.studentregistrationapp

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.studentregistrationapp.AdminViewModel
import com.example.studentregistrationapp.AdminEvent
import com.example.studentregistrationapp.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminLoginScreen(
    navController: NavController,
    viewModel: AdminViewModel
) {
    val adminScreenState by viewModel.state.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(key1 = context) {
        viewModel.validationEvents.collect {event ->
            when(event) {
                is AdminViewModel.ValidationEvent.Success -> {
                    navController.navigate(Screens.StudentRegistrationScreen.route)
                    viewModel.onEvent(AdminEvent.EnterPin(""))
                    Toast.makeText(
                        context,
                        "successful registration",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }


    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ) {
        Text(modifier = Modifier.align(Alignment.CenterHorizontally)
            .padding(6.dp),
            text = "Are You an Admin?",
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleLarge
        )
        
        Spacer(modifier = Modifier.height(15.dp))
        TextField(
            value = adminScreenState.pin,
            onValueChange = {
                viewModel.onEvent(AdminEvent.EnterPin(it))

            },
            modifier = Modifier.fillMaxWidth()
                .padding(6.dp),
            isError = !adminScreenState.pinError.isNullOrEmpty(),
            placeholder = {
            Text(
                text = "Enter Pin"
            )
        }
        )
        if(!adminScreenState.pinError.isNullOrEmpty()) {
            Text(
                text = adminScreenState.pinError,
                color = MaterialTheme.colorScheme.error
            )
        }

        Button(
            modifier = Modifier.fillMaxWidth() ,
            onClick = {
                viewModel.onEvent(AdminEvent.EnterPinButton)
            })
        {
            Text(text = "Enter")

        }
    }

}



