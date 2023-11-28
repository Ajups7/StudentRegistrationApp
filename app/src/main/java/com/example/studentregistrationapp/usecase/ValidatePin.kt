package com.example.studentregistrationapp.usecase

import android.util.Patterns
import androidx.core.text.isDigitsOnly

class ValidatePin {
    fun execute(pin: String): ValidationResult {
        if(pin.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "The pin can't be empty"
            )
        }
        if (pin.length > 4) {
            return ValidationResult(
                successful = false,
                errorMessage = "Pin can't be more than 4 numbers"
            )
        }
        val containsOnlyDigits = pin.isDigitsOnly()
        if(!containsOnlyDigits) {
            return ValidationResult(
                successful = false,
                errorMessage = "pin must contain only numbers"
            )
        }
        return ValidationResult(
            successful = true
        )

    }

}