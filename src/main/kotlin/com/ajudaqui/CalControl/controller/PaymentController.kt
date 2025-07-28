package com.ajudaqui.CalControl.controller

import com.ajudaqui.CalControl.dto.PaymentDto
import com.ajudaqui.CalControl.entity.Events
import com.ajudaqui.CalControl.service.PaymentService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/payment")
class PaymentController(private val paymentService: PaymentService) {

  @PostMapping("")
  fun createEvent(
          @RequestHeader("Authorization") email: String,
          @RequestBody @Valid payment: PaymentDto
  ): ResponseEntity<Events> = ResponseEntity.ok(paymentService.create(email, payment))

  fun updateEvent() {}
}
