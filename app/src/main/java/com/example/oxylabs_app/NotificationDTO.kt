package com.example.oxylabs_app

data class NotificationDTO(
    val id: Int,
    val title: String,
    val description: String,
    val scheduledTime: String
) : java.io.Serializable {
    companion object{
        const val ACTIVITY_EXTRA_NAME = "notification"
    }
}
