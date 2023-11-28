package com.example.studentregistrationapp

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.studentregistrationapp.usecase.ValidatePin
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AdminViewModel(
    private val sharedPref: OnBoardingSharedPref,
    private val validatePin: ValidatePin = ValidatePin()
): ViewModel() {
    private val _state = MutableStateFlow(AdminState())
    var state = _state.asStateFlow()

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()
    fun onEvent(event: AdminEvent) {
        when(event) {
            is AdminEvent.EnterPin -> {
                _state.update { it.copy(
                    pin = event.pin
                ) }


                }
            is AdminEvent.EnterPinButton -> {

                submitData()
            }
            }
        }

    private fun submitData() {
        val pinResult = validatePin.execute(state.value.pin)
        val hasError = listOf(
            pinResult
        ).any { !it.successful }
        Log.d("boyss", "submitdATA")
        when{
            hasError->
            {pinResult.errorMessage?.let{errorMsg->
                _state.update{
                    it.copy(pinError= errorMsg)
                }
            }
        return}

        else-> {
            val initialPin = sharedPref.getSharedPref()
            Log.d("boyss", initialPin.toString())
        if (!initialPin.isNullOrEmpty()) {
            if (initialPin == state.value.pin) {
        viewModelScope.launch {
            validationEventChannel.send(ValidationEvent.Success)}

            }
            else{
                _state.update{
                        it.copy(pinError= "Incorrect Pin, Use Inital Pin")
                }

            }
        } else{
            Log.d("boys", "we in the else block")


                sharedPref.updateSharedPref(state.value.pin)
            viewModelScope.launch {
                validationEventChannel.send(ValidationEvent.Success)}
        }
    }

    }

    }
    sealed class ValidationEvent {
        object Success: ValidationEvent()
    }

}




class AdminViewModelFactory(private val sharedPref: OnBoardingSharedPref): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AdminViewModel(sharedPref) as T
    }
}
