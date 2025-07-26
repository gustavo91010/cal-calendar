package com.ajudaqui.CalControl.controller

import com.ajudaqui.CalControl.dto.EventCreateRequest
import com.ajudaqui.CalControl.mapper.EventCalendarMapper
import com.ajudaqui.CalControl.mapper.EventItemMapper
import com.ajudaqui.CalControl.response.EventItem
import com.ajudaqui.CalControl.response.MessageResponse
import com.ajudaqui.CalControl.service.GoogleCalendarService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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

  @GetMapping("/events/id/{id}")
  fun getEventById(
          @RequestHeader("Authorization") authorization: String,
          @PathVariable id: String
  ): ResponseEntity<Any> {
    val token = authorization.removePrefix("Bearer ").trim()
    val event = googleOAuthService.getEventById(token, id)
    return ResponseEntity.ok(EventItemMapper.mapperResponse(event!!))
  }

  @DeleteMapping("/events/id/{id}")
  fun getById(
          @RequestHeader("Authorization") authorization: String,
          @PathVariable id: String
  ): ResponseEntity<MessageResponse> {
    val token = authorization.removePrefix("Bearer ").trim()
    googleOAuthService.deleteEvent(token, id)
    return ResponseEntity.ok(MessageResponse("Evento id: $id excluido com sucesso."))
  }
}
