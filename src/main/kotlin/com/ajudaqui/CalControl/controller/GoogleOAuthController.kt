package com.ajudaqui.CalControl.controller

import com.ajudaqui.CalControl.service.GoogleOAuthService
import com.ajudaqui.CalControl.response.CalendarEventsResponse
import com.ajudaqui.CalControl.response.EventItem
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PostMapping

@RestController
@RequestMapping("/google")
class GoogleOAuthController(private val googleOAuthService: GoogleOAuthService) {

  private val logger = LoggerFactory.getLogger(GoogleOAuthController::class.java)
  @PostMapping("/calendar/events")
    fun createEvent(
        @RequestHeader("Authorization") authorization: String,
        // @RequestBody event: EventItem
    ): ResponseEntity<EventItem> {
        val token = authorization.removePrefix("Bearer ").trim()
        return ResponseEntity.ok(googleOAuthService.createEvent(token))
    }

  @GetMapping("/calendar/events")
  fun listEvents(
          @RequestHeader("Authorization") authorization: String,
          @RequestParam accessToken: String?,
          @RequestParam(required = false) singleEvents: String?,
          @RequestParam(required = false) updatedMin: String?,
          @RequestParam(required = false) updatedMax: String?,
          @RequestParam(required = false) maxResults: Long?
  ): CalendarEventsResponse?{
    logger.info("[GET] |/google/calendar/events | ")
    val token = authorization.removePrefix("Bearer ").trim()
    val response =
            googleOAuthService.listEvents(
                    accessToken = token,
                    singleEvents = singleEvents,
                    updatedMin = updatedMin,
                    updatedMax = updatedMax,
                    maxResults = maxResults
            )
    // print(response?.keys)
    return response
  }

  // Abre a pagina de autenticação com a uri fornecida
  @GetMapping("/authentication")
  fun redirecgGoogle(): ResponseEntity<Void> =
          ResponseEntity(
                  HttpHeaders().apply { location = googleOAuthService.authorizedUri() },
                  HttpStatus.FOUND
          )

  // Se a autenticação der certo, ele chama aqui, já autenticado
  @GetMapping("/authenticated")
  fun autorizado(@RequestParam code: String): ResponseEntity<Any> =
          ResponseEntity.ok(googleOAuthService.validToken(code))
}
