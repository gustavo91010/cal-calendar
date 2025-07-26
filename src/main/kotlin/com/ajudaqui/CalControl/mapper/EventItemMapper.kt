package com.ajudaqui.CalControl.mapper

import com.ajudaqui.CalControl.response.EventItem

object EventItemMapper {
    fun mapperResponse(resp: EventItem): Map<String, Any?> {
        return mapOf(
            "id" to resp.id,
            "status" to resp.status,
            "summary" to resp.summary,
            "description" to resp.description,
            "start" to resp.start?.dateTime,
            "end" to resp.end?.dateTime,
            "sequence" to resp.sequence
        )
    }
}
