package com.example.studentregistrationapp.usecase

class ValidateCourse {
    fun execute(course: String): ValidationResult {
        if(course.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "The course can't be empty"
            )
        }
        return ValidationResult(
            successful = true
        )

    }
}