package com.ajudaqui.CalControl.controller

import com.ajudaqui.CalControl.dto.MessageDTO
import com.ajudaqui.CalControl.dto.TemplateDTO
import com.ajudaqui.CalControl.service.TemplateService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/message")
class MessageController(private val messageService: TemplateService) {

  @PostMapping("/template")
  fun registerTemplate(@RequestBody templateDTO: TemplateDTO): Map<String, String> {

    messageService.create(templateDTO)

    return mapOf(
            "message" to
                    "Template registrado para aplicação ${templateDTO.application} no evento: ${templateDTO.eventType}"
    )
  }

  @PostMapping("")
  fun push(@RequestBody @Valid data: MessageDTO): Map<String, String> {
    messageService.registerMessage(data)

    return mapOf("message" to "Mensagem recebida com sucesso.")
  }
}
