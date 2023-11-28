package com.example.studentregistrationapp.usecase

import android.os.Message

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)
