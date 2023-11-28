package com.example.studentregistrationapp.usecase

class ValidateLocation {
    fun execute(location: String): ValidationResult {
        if(location.isBlank()) {
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