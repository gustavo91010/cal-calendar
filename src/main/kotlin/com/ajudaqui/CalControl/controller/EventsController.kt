package com.ajudaqui.CalControl.controller

import com.ajudaqui.CalControl.dto.EventCreateRequest
import com.ajudaqui.CalControl.entity.Events
import com.ajudaqui.CalControl.service.EventsService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/v1/events")
class EventsController(private val eventsService: EventsService) {

  @PostMapping("")
  fun createEvent(
          @RequestHeader("Authorization") email: String,
          @RequestBody @Valid event: EventCreateRequest
  ): ResponseEntity<Events> {
    return ResponseEntity.ok(eventsService.create(email, event))
  }


  // fun findEventPerid(
  //   @RequestHeader("Authorization") email: String,
  //   @RequestParam start: LocalDate,
  //   @RequestParam finish:LocalDate
  // ):ResponseEntity<?>{
// eventsService.findAll(email, start, finish)

  //   return ResponseEntity.ok("haha")
  // }
}
