package com.ajudaqui.CalControl.controller

import com.ajudaqui.CalControl.response.CalendarEventsResponse
import com.ajudaqui.CalControl.response.EventItem
import com.ajudaqui.CalControl.service.GoogleOAuthService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/google/calendar")
class GoogleCalendarController(private val googleOAuthService: GoogleOAuthService) {

  // private val logger = LoggerFactory.getLogger(GoogleOAuthController::class.java)
  @PostMapping("/events")
  fun createEvent(
          @RequestHeader("Authorization") authorization: String,
  ): ResponseEntity<EventItem> {
    val token = authorization.removePrefix("Bearer ").trim()
    return ResponseEntity.ok(googleOAuthService.createEvent(token))
  }

  @GetMapping("/events")
  fun listEvents(
          @RequestHeader("Authorization") authorization: String,
          // @RequestParam accessToken: String?,
          @RequestParam(required = false) singleEvents: String?,
          @RequestParam(required = false) updatedMin: String?,
          @RequestParam(required = false) updatedMax: String?,
          @RequestParam(required = false) maxResults: Long?
  ): CalendarEventsResponse? {
    // logger.info("[GET] |/google/calendar/events | ")
    val token = authorization.removePrefix("Bearer ").trim()
    val response =
            googleOAuthService.listEvents(
                    accessToken = token,
                    singleEvents = singleEvents,
                    updatedMin = updatedMin,
                    updatedMax = updatedMax,
                    maxResults = maxResults
            )
    return response
  }
}
