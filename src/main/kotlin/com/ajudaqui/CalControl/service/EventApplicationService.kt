package com.ajudaqui.CalControl.service

// import io.github.resilience4j.circuitbreaker.CircuitBreaker
import com.ajudaqui.CalControl.dto.EventCreateRequest
import com.ajudaqui.CalControl.dto.MessageDTO
import com.ajudaqui.CalControl.dto.TemplateDTO
import com.ajudaqui.CalControl.entity.EventTemplate
import com.ajudaqui.CalControl.exceprion.custon.NotFoundException
import com.ajudaqui.CalControl.repository.TemplateRepository
import com.ajudaqui.CalControl.utils.enum.ETemplate
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker
import org.springframework.stereotype.Service

@Service
class EventApplicationService(
        private val repository: TemplateRepository,
        private val mongoBreaker: CircuitBreaker,
        private val eventService: EventsService
) {

  fun registerTemplate(data: TemplateDTO): EventTemplate {

    val template =
            EventTemplate(
                    type = ETemplate.CREATE,
                    application = data.application,
                    template = data.template
            )
    return save(template)
  }

  fun createGoogleEvent(message: MessageDTO) {

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
          mongoBreaker.run(
                  {
                    repository.findFirstByTypeAndApplication(type, application)
                            ?: throw NotFoundException("Template não registrado")
                  },
                  {
                    println("Fallback acionado: Mongo fora do ar")
                    throw IllegalStateException("Mongo fora do ar")
                  }
          )

  // private fun save(template: EventTemplate): EventTemplate = repository.save(template)
  private fun save(template: EventTemplate): EventTemplate =
          mongoBreaker.run(
                  { repository.save(template) },
                  {
                    println("Fallback acionado: Mongo fora do ar")
                    null
                  }
          )
}
