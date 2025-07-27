package com.ajudaqui.CalControl.controller

import com.ajudaqui.CalControl.dto.EventCreateRequest
import com.ajudaqui.CalControl.dto.EventItemUpdateDto
import com.ajudaqui.CalControl.mapper.EventCalendarMapper
import com.ajudaqui.CalControl.mapper.EventItemMapper
import com.ajudaqui.CalControl.response.MessageResponse
import com.ajudaqui.CalControl.service.GoogleCalendarService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.context.annotation.Profile

@RestController
@RequestMapping("/google/calendar")
@Profile("desabilitado")
class GoogleCalendarController(private val googleCalendarService: GoogleCalendarService) {

  @PostMapping("/events")
  fun createEvent(
          @RequestHeader("Authorization") authorization: String,
          @RequestBody @Valid eventRequest: EventCreateRequest
  ): ResponseEntity<Any> {
    val token = authorization.removePrefix("Bearer ").trim()
    val event = googleCalendarService.createEvent(token, "primary", eventRequest)
    return ResponseEntity.ok(EventItemMapper.mapperResponse(event!!))
  }

  @GetMapping("/events")
  fun listEvents(
          @RequestHeader("Authorization") authorization: String,
          @RequestParam(required = false) singleEvents: String?,
          @RequestParam(required = false) timeMin: String?,
          @RequestParam(required = false) timeMax: String?,
          @RequestParam(required = false) maxResults: Long?
  ): Any? {
    val token = authorization.removePrefix("Bearer ").trim()
    val response =
            googleCalendarService.listEvents(
                    accessToken = token,
                    singleEvents = singleEvents,
                    timeMin = timeMin,
                    timeMax = timeMax,
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
    val event = googleCalendarService.getEventById(token, id)
    return ResponseEntity.ok(EventItemMapper.mapperResponse(event!!))
  }

  @PutMapping("/events/id/{eventId}")
  fun updateEventDescriptin(
          @RequestHeader("Authorization") authorization: String,
          @PathVariable eventId: String,
          @RequestBody eventItemDto: EventItemUpdateDto
  ): ResponseEntity<Any> {
    val token = authorization.removePrefix("Bearer ").trim()
    val event = googleCalendarService.updateEventDescriptin(token, eventId, eventItemDto)
    return ResponseEntity.ok(EventItemMapper.mapperResponse(event!!))
  }

  @DeleteMapping("/events/id/{id}")
  fun getById(
          @RequestHeader("Authorization") authorization: String,
          @PathVariable id: String
  ): ResponseEntity<MessageResponse> {
    val token = authorization.removePrefix("Bearer ").trim()
    googleCalendarService.deleteEvent(token, id)
    return ResponseEntity.ok(MessageResponse("Evento id: $id excluido com sucesso."))
  }
}
