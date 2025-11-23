package com.ajudaqui.CalControl.service

import com.ajudaqui.CalControl.dto.EventCreateRequest
import com.ajudaqui.CalControl.dto.MessageDTO
import com.ajudaqui.CalControl.dto.TemplateDTO
import com.ajudaqui.CalControl.entity.EventTemplate
import com.ajudaqui.CalControl.exceprion.custon.NotFoundException
import com.ajudaqui.CalControl.repository.TemplateRepository
import com.ajudaqui.CalControl.utils.enum.ETemplate
import org.springframework.stereotype.Service

@Service
class TemplateService(
        private val repository: TemplateRepository,
        private val eventService: EventsService
) {

  fun create(data: TemplateDTO): EventTemplate {

    val template =
            EventTemplate(
                    type = ETemplate.CREATE,
                    application = data.application,
                    template = data.template
            )
    return save(template)
  }

  fun registerMessage(message: MessageDTO) {

    val template = findByTypeAndApplication(message.type, message.application)

    val eventCreate =
            EventCreateRequest(
                    summary = "Contas a pagar",
                    description = template.template,
                    day = message.day
            )

    eventService.create(message.email, eventCreate)
  }

  private fun findByTypeAndApplication(type: String, application: String): EventTemplate =
          repository.findFirstByTypeAndApplication(type, application)
                  ?: throw NotFoundException("Template não registrado")

  private fun save(template: EventTemplate): EventTemplate = repository.save(template)
}
