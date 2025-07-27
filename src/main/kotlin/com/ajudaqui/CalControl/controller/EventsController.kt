package com.ajudaqui.CalControl.controller

import com.ajudaqui.CalControl.dto.EventCreateRequest
import com.ajudaqui.CalControl.entity.Events
import com.ajudaqui.CalControl.service.EventsService
import jakarta.validation.Valid
import jakarta.validation.constraints.*
import java.time.LocalDateTime
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

// const val REGEX_TIME = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"
const val REGEX_TIME = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z"

@RestController
@RequestMapping("/v1/events")
class EventsController(private val eventsService: EventsService) {
  // const val regexTime = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"

  @PostMapping("")
  fun createEvent(
          @RequestHeader("Authorization") email: String,
          @RequestBody @Valid event: EventCreateRequest
  ): ResponseEntity<Events> {
    return ResponseEntity.ok(eventsService.create(email, event))
  }

  @GetMapping("/period")
  fun findEventPerid(
          @RequestHeader("Authorization") email: String,
          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) start: LocalDateTime,
          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) finish: LocalDateTime
  ): ResponseEntity<Any> {

    return ResponseEntity.ok(eventsService.findAll(email, start, finish))
  }
}
