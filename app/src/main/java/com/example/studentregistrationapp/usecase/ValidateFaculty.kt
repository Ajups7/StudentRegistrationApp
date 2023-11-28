package com.example.studentregistrationapp.usecase

class ValidateFaculty {
    fun execute(faculty: String): ValidationResult {
        if(faculty.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "The faculty can't be empty"
            )
        }
        return ValidationResult(
            successful = true
        )

    }
}