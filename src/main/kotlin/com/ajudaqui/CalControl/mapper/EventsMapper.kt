package com.ajudaqui.CalControl.mapper

import com.ajudaqui.CalControl.entity.Users
import com.ajudaqui.CalControl.entity.Events
import com.ajudaqui.CalControl.response.EventItem
import java.time.LocalDateTime
import java.time.OffsetDateTime

object EventsMapper {

  private fun parseToLocalDateTime(dateTime: String?): LocalDateTime {
    return OffsetDateTime.parse(dateTime).toLocalDateTime()
  }

  fun fromEventItem(id: Long,users:Users, eventItem: EventItem): Events {
    return Events(
            id = id,
            eventId = eventItem.id,
            summary = eventItem.summary,
            start = parseToLocalDateTime(eventItem.start?.dateTime),
            end = parseToLocalDateTime(eventItem.end?.dateTime),
            status = eventItem.status,
            users= users
    )
  }
}
