package com.ajudaqui.CalControl.dto

import com.ajudaqui.CalControl.response.Attendee
import com.ajudaqui.CalControl.response.Creator
import com.ajudaqui.CalControl.response.EventDateTime
import com.ajudaqui.CalControl.response.Organizer

data class EventItem(
    val id: String,
    val status: String?,
    val summary: String?,
    val description: String?,
    val location: String?,
    val start: EventDateTime,
    val end: EventDateTime,
    val creator: Creator?,
    val organizer: Organizer?,
    val attendees: List<Attendee>?
)

 data class EventDateTime(
     val date: String?,      // para evento o dia todo
     val dateTime: String?   // para evento com hora
 )

 data class Creator(
     val email: String?,
     val displayName: String?
 )

 data class Organizer(
     val email: String?,
     val displayName: String?
 )

 data class Attendee(
     val email: String?,
     val responseStatus: String?
 )
