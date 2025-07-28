package com.ajudaqui.CalControl.controller

import com.ajudaqui.CalControl.dto.EventCreateRequest
import com.ajudaqui.CalControl.dto.EventItemUpdateDto
import com.ajudaqui.CalControl.entity.Events
import com.ajudaqui.CalControl.response.MessageResponse
import com.ajudaqui.CalControl.service.EventsService
import jakarta.validation.Valid
import jakarta.validation.constraints.*
import java.time.LocalDateTime
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/events")
class EventsController(private val eventsService: EventsService) {

  @PostMapping("")
  fun createEvent(
          @RequestHeader("Authorization") email: String,
          @RequestBody @Valid event: EventCreateRequest
  ): ResponseEntity<Events> = ResponseEntity.ok(eventsService.create(email, event))

  @GetMapping("/period")
  fun findEventPerid(
          @RequestHeader("Authorization") email: String,
          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) start: LocalDateTime,
          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) finish: LocalDateTime
  ): ResponseEntity<Any> = ResponseEntity.ok(eventsService.findAll(email, start, finish))

  @GetMapping("/id/{eventId}")
  fun findById(
          @RequestHeader("Authorization") email: String,
          @PathVariable eventId: Long
  ): ResponseEntity<Any> = ResponseEntity.ok(eventsService.findById(email, eventId))

  @PutMapping("/id/{eventId}")
  fun update(
          @RequestHeader("Authorization") email: String,
          @PathVariable eventId: Long,
          @RequestBody eventItemDtop: EventItemUpdateDto
  ): ResponseEntity<Any> = ResponseEntity.ok(eventsService.update(email, eventId, eventItemDtop))

  @DeleteMapping("/id/{eventId}")
  fun delete(
          @RequestHeader("Authorization") email: String,
          @PathVariable eventId: Long
  ): ResponseEntity<Any> {
    eventsService.delete(email, eventId)
    return ResponseEntity.ok(MessageResponse("Evento id: $eventId excluido com sucesso."))
  }
}
