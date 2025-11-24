package com.ajudaqui.CalControl.controller

import com.ajudaqui.CalControl.dto.MessageDTO
import com.ajudaqui.CalControl.dto.TemplateDTO
import com.ajudaqui.CalControl.service.EventApplicationService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/message")
class EventApplicationContoller(private val messageService: EventApplicationService) {

  @PostMapping("/template")
  fun registerTemplate(@RequestBody templateDTO: TemplateDTO): Map<String, String> {

    messageService.registerTemplate(templateDTO)

    return mapOf(
            "message" to
                    "Template registrado para aplicação ${templateDTO.application} no evento: ${templateDTO.eventType}"
    )
  }

  @PostMapping("")
  fun push(@RequestBody @Valid data: MessageDTO): Map<String, String> {
    messageService.createGoogleEvent(data)

    return mapOf("message" to "Mensagem recebida com sucesso.")
  }
}
