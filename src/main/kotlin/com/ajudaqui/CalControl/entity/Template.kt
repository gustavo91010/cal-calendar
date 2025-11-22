package com.ajudaqui.CalControl.entity

import com.ajudaqui.CalControl.utils.enum.ETemplate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("template")
open class Template(
        @Id val id: String? = null,
        @CreatedDate var createdAt: LocalDateTime = LocalDateTime.now(),
        val type: ETemplate,
        val email: String,
        val template: String
) {}
