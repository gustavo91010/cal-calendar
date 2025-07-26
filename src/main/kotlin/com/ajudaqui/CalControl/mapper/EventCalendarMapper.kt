package com.ajudaqui.CalControl.mapper

import com.ajudaqui.CalControl.response.CalendarEventsResponse

object EventCalendarMapper {
  fun mapCalendarResponse(resp: CalendarEventsResponse): Map<String, Any?> {
    return mapOf(
            "summary" to resp.summary,
            "description" to resp.description,
            "nextPageToken" to resp.nextPageToken,
            "items" to
                    resp.items?.map { item ->
                      mapOf(
                              "id" to item.id,
                              "status" to item.status,
                              "updated" to item.updated,
                              "summary" to item.summary,
                              "description" to item.description,
                              "start" to item.start?.dateTime,
                              "end" to item.end?.dateTime,
                              "sequence" to item.sequence,
                              // "source" to
                              //         item.source?.let {
                              //           mapOf("url" to it.url, "title" to it.title)
                              //         }
                              )
                    }
    )
  }
}
