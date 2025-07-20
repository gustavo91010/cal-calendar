package com.ajudaqui.CalControl.response

data class CalendarEventsResponse(
        val kind: String,
        val etag: String,
        val summary: String,
        val description: String?,
        val updated: String,
        val timeZone: String,
        val accessRole: String,
        val defaultReminders: List<Reminder>,
        val nextPageToken: String?,
        val items: List<EventItem>
)

data class Reminder(val method: String, val minutes: Int)

data class EventItem(
        val kind: String,
        val etag: String,
        val id: String,
        val status: String,
        val htmlLink: String,
        val created: String,
        val updated: String,
        val summary: String?,
        val description: String?,
        val location: String?,
        val creator: Creator,
        val organizer: Organizer,
        val start: EventDateTime,
        val end: EventDateTime,
        val transparency: String?,
        val visibility: String?,
        val iCalUID: String,
        val sequence: Int,
        val attendees: List<Attendee>?,
        val reminders: Reminders,
        val eventType: String,
        val endTimeUnspecified: Boolean? = false,
        val guestsCanInviteOthers: Boolean? = true,
        val source: Source? = null
)

data class Creator(val email: String, val self: Boolean)

data class Organizer(val email: String, val self: Boolean)

data class EventDateTime(
        val date: String? = null,
        val dateTime: String? = null,
        val timeZone: String? = null
)

data class Attendee(
        val email: String,
        val organizer: Boolean?,
        val self: Boolean?,
        val responseStatus: String?
)

data class Reminders(val useDefault: Boolean, val overrides: List<Reminder>?)

data class Source(val url: String, val title: String?)
