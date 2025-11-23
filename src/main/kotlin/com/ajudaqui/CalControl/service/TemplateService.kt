package com.ajudaqui.CalControl.service

import com.ajudaqui.CalControl.dto.EventCreateRequest
import com.ajudaqui.CalControl.dto.MessageDTO
import com.ajudaqui.CalControl.entity.EventTemplate
import com.ajudaqui.CalControl.exceprion.custon.NotFoundException
import com.ajudaqui.CalControl.repository.TemplateRepository
import org.springframework.stereotype.Service

@Service
class TemplateService(
        private val repository: TemplateRepository,
        private val eventService: EventsService
) {

  fun create(template: EventTemplate): EventTemplate {

    return save(template)
  }
  fun registerMessage(message: MessageDTO) {

    val lalala = findByTypeAndApplication(message.type, message.application)
    print(lalala)
    val eventCreate =
            EventCreateRequest(
                    summary = "Contas a pagar",
                    description = lalala.template,
                    day = message.day
            )
    eventService.create(message.email, eventCreate)
  }

  private fun findByTypeAndApplication(type: String, application: String): EventTemplate =
          repository.findByTypeAndApplication(type, application).orElseThrow {
            NotFoundException("Template não registrado")
          }

  private fun save(template: EventTemplate): EventTemplate = repository.save(template)
}

