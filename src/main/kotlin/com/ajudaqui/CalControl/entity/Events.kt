package com.ajudaqui.CalControl.entity

import com.ajudaqui.CalControl.dto.EventCreateRequest
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = "events")
data class Events(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
        @Column(name = "event_id") var eventId: String? = null,
        @Column(name = "summary") var summary: String? = null,
        @Column(name = "start_time") var start: LocalDateTime,
        @Column(name = "end_time") var end: LocalDateTime,
        @Column(name = "status") var status: String? = null,
        @Column(name = "created_at") var createdAt: LocalDateTime = LocalDateTime.now(),
        @Column(name = "updated_at") var updatedAt: LocalDateTime = LocalDateTime.now(),
        @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "users_id") var users: Users? = null
) {
  companion object {
    fun form(dto: EventCreateRequest) =
            Events(
                    summary = dto.summary,
                    start = LocalDateTime.parse(dto.start.dateTime),
                    end = LocalDateTime.parse(dto.end.dateTime),
                    status = "create"
            )
  }
}
