package com.ajudaqui.CalControl.controller

import com.ajudaqui.CalControl.dto.EventCreateRequest
import com.ajudaqui.CalControl.mapper.EventCalendarMapper
import com.ajudaqui.CalControl.response.EventItem
import com.ajudaqui.CalControl.service.GoogleCalendarService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/google/calendar")
class GoogleCalendarController(private val googleOAuthService: GoogleCalendarService) {

  @PostMapping("/events")
  fun createEvent(
          @RequestHeader("Authorization") authorization: String,
          @RequestBody @Valid event: EventCreateRequest
  ): ResponseEntity<EventItem> {
    val token = authorization.removePrefix("Bearer ").trim()
    return ResponseEntity.ok(googleOAuthService.createEvent(token, "primary", event))
  }

  @GetMapping("/events")
  fun listEvents(
          @RequestHeader("Authorization") authorization: String,
          @RequestParam(required = false) singleEvents: String?,
          @RequestParam(required = false) updatedMin: String?,
          @RequestParam(required = false) updatedMax: String?,
          @RequestParam(required = false) maxResults: Long?
          // ): CalendarEventsResponse? {
          ): Any? {
    val token = authorization.removePrefix("Bearer ").trim()
    val response =
            googleOAuthService.listEvents(
                    accessToken = token,
                    singleEvents = singleEvents,
                    updatedMin = updatedMin,
                    updatedMax = updatedMax,
                    maxResults = maxResults
            )
    return EventCalendarMapper.mapCalendarResponse(response!!)
  }
}
