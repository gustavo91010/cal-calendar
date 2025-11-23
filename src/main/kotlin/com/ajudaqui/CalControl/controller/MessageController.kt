package com.ajudaqui.CalControl.controller

import com.ajudaqui.CalControl.dto.MessageDTO
import com.ajudaqui.CalControl.dto.TemplateDTO
import com.ajudaqui.CalControl.service.TemplateService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/message")
class MessageController(private val usersService: TemplateService) {

  @PostMapping("/template")
  fun registerTemplate(
          @RequestHeader("Authorization") token: String,
          @RequestBody templateDTO: TemplateDTO
  ): Map<String, String> {

    usersService.create(templateDTO)

    return mapOf("message" to "Mensagem recebida com sucesso.")
  }
  @PostMapping("")
  fun push(
          @RequestHeader("Authorization") token: String,
          @RequestBody @Valid data: MessageDTO
  ): Map<String, String> {
    println("payload: " + data)

    return mapOf("message" to "Mensagem recebida com sucesso.")
  }
}
