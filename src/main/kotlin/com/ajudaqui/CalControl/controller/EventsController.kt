package com.ajudaqui.CalControl.controller

import com.ajudaqui.CalControl.dto.EventCreateRequest
import com.ajudaqui.CalControl.response.CalendarEventsResponse
import com.ajudaqui.CalControl.entity.Events
import com.ajudaqui.CalControl.response.EventItem
import com.ajudaqui.CalControl.service.EventsService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/events")
class EventsController(private val googleOAuthService: EventsService) {

  @PostMapping("")
  fun createEvent(
          @RequestHeader("Authorization") email: String,
          @RequestBody @Valid event: EventCreateRequest
  ): ResponseEntity<Events> {
    return ResponseEntity.ok(googleOAuthService.create(email, event))
  }

  // @GetMapping("/events")
  // fun listEvents(
  //         @RequestHeader("Authorization") authorization: String,
  //         @RequestParam(required = false) singleEvents: String?,
  //         @RequestParam(required = false) updatedMin: String?,
  //         @RequestParam(required = false) updatedMax: String?,
  //         @RequestParam(required = false) maxResults: Long?
  // ): CalendarEventsResponse? {
  //   val token = authorization.removePrefix("Bearer ").trim()
  //   val response =
  //           googleOAuthService.listEvents(
  //                   accessToken = token,
  //                   singleEvents = singleEvents,
  //                   updatedMin = updatedMin,
  //                   updatedMax = updatedMax,
  //                   maxResults = maxResults
  //           )
  //   return response
  // }
}
