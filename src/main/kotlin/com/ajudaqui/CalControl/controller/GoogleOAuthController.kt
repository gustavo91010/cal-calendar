package com.ajudaqui.CalControl.controller

import com.ajudaqui.CalControl.service.GoogleOAuthService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/google/calendar")
class GoogleOAuthController(private val googleOAuthService: GoogleOAuthService) {

  private val logger = LoggerFactory.getLogger(GoogleOAuthController::class.java)
  @GetMapping("/opa") fun opa() = "opa"

  @GetMapping("/events")
  fun listEvents(@RequestHeader("Authorization") authorization: String): Map<String, Any>? {
    logger.info("[GET] |/google/calendar/events | ")
    val token = authorization.removePrefix("Bearer ").trim()
    return googleOAuthService.listEvents(token)
  }

  @PostMapping("/token")
  fun exchangeCodeForToken(@RequestParam code: String): Map<String, Any>? {
    logger.info("[POST] |/google/calendar/token | ")

    return googleOAuthService.exchangeCodeForToken(code)
  }
}
