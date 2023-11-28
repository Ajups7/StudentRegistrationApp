package com.example.studentregistrationapp.usecase

import androidx.core.text.isDigitsOnly

class ValidateFirstname {
    fun execute(firstName: String): ValidationResult {
        if(firstName.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "The firstname can't be empty"
            )
        }
        return ValidationResult(
            successful = true
        )

    }
}