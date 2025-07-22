package com.ajudaqui.CalControl.controller

import com.ajudaqui.CalControl.service.GoogleOAuthService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/google")
class GoogleOAuthController(private val googleOAuthService: GoogleOAuthService) {

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
