package com.example.studentregistrationapp.usecase

class ValidateLastName {
    fun execute(lastName: String): ValidationResult {
        if(lastName.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "The lastname can't be empty"
            )
        }
        return ValidationResult(
            successful = true
        )

    }
}