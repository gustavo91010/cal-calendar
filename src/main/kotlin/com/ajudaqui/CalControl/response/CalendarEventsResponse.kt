package com.ajudaqui.CalControl.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class CalendarEventsResponse(
    val kind: String? = null,
    val etag: String? = null,
    val summary: String? = null,
    val description: String? = null,
    val updated: String? = null,
    val timeZone: String? = null,
    val accessRole: String? = null,
    val defaultReminders: List<Reminder>? = null,
    val nextPageToken: String? = null,
    val items: List<EventItem>? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Reminder(
    val method: String? = null,
    val minutes: Int? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class EventItem(
    val kind: String? = null,
    val etag: String? = null,
    val id: String? = null,
    val status: String? = null,
    val htmlLink: String? = null,
    val created: String? = null,
    val updated: String? = null,
    val summary: String? = null,
    val description: String? = null,
    val location: String? = null,
    val creator: Creator? = null,
    val organizer: Organizer? = null,
    val start: EventDateTime? = null,
    val end: EventDateTime? = null,
    val transparency: String? = null,
    val visibility: String? = null,
    val iCalUID: String? = null,
    val sequence: Int? = null,
    val attendees: List<Attendee>? = null,
    val reminders: Reminders? = null,
    val eventType: String? = null,
    val endTimeUnspecified: Boolean? = null,
    val guestsCanInviteOthers: Boolean? = null,
    val source: Source? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Creator(
    val email: String? = null,
    val self: Boolean? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Organizer(
    val email: String? = null,
    val self: Boolean? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class EventDateTime(
    val date: String? = null,
    val dateTime: String? = null,
    val timeZone: String? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Attendee(
    val email: String? = null,
    val organizer: Boolean? = null,
    val self: Boolean? = null,
    val responseStatus: String? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Reminders(
    val useDefault: Boolean? = null,
    val overrides: List<Reminder>? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Source(
    val url: String? = null,
    val title: String? = null
)
