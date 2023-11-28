package com.example.studentregistrationapp

sealed interface AdminEvent {
    data class EnterPin(val pin: String): AdminEvent
    object EnterPinButton: AdminEvent

}