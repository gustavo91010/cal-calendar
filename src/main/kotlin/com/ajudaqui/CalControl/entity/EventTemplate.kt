package com.ajudaqui.CalControl.entity

import com.ajudaqui.CalControl.utils.enum.ETemplate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("event_template")
open class EventTemplate(
        @Id val id: String? = null,
        @CreatedDate var createdAt: LocalDateTime = LocalDateTime.now(),
        val type: ETemplate,
        val application: String,
        val template: String
) {}
