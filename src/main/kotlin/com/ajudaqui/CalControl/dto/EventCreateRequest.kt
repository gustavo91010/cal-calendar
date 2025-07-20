package com.ajudaqui.CalControl.dto


data class EventCreateRequest(
    val summary: String,
    val description: String? = null,
    val location: String? = null,
    val start: EventCreateDateTime,
    val end: EventCreateDateTime
)

data class EventCreateDateTime(
    val date: String? = null,
    val dateTime: String? = null,
    val timeZone: String? = null
)


