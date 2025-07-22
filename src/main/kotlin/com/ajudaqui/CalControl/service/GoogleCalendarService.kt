package com.ajudaqui.CalControl.service

import com.ajudaqui.CalControl.dto.EventCreateDateTime
import com.ajudaqui.CalControl.dto.EventCreateRequest
import com.ajudaqui.CalControl.exceprion.custon.MessageException
import com.ajudaqui.CalControl.response.CalendarEventsResponse
import com.ajudaqui.CalControl.response.Creator
import com.ajudaqui.CalControl.response.EventDateTime
import com.ajudaqui.CalControl.response.EventItem
import com.ajudaqui.CalControl.response.Organizer
import com.ajudaqui.CalControl.response.Reminders
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

@Service
class GoogleCalendarService() {

  fun createEvent(
          accessToken: String,
          calendarId: String? = "primary",
          event: EventCreateRequest?
          ): EventItem? {
    val url = "https://www.googleapis.com/calendar/v3/calendars/$calendarId/events"
println( "Objero no ggoogle calendar")
println(event)
    val headers =
            HttpHeaders().apply {
              contentType = MediaType.APPLICATION_JSON
              setBearerAuth(accessToken)
            }
    return try {
      val mapper = ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL)
      println(mapper.writeValueAsString(event))
      val response =
              RestTemplate().postForEntity(url, HttpEntity(event, headers), EventItem::class.java)
      response.body
    } catch (e: HttpClientErrorException) {
      e.printStackTrace()
      throw MessageException(handlerErrorGoogle(e))
    }
  }

  fun listEvents(
          accessToken: String,
          singleEvents: String?,
          updatedMin: String?,
          updatedMax: String?,
          maxResults: Long?
  ): CalendarEventsResponse? {
    val minOrNo =
            if (updatedMin != null) "&updatedMin=$updatedMin"
            else "&updatedMin=2025-01-01T00:00:00Z"
    val maxOrNo =
            if (updatedMax != null) "&updatedMax=$updatedMax"
            else "&updatedMax=2025-11-01T00:00:00Z"
    val resultOrNo = if (maxResults != null) "&maxResults=$maxResults" else "&maxResults=10"
    val eventOrNo =
            if (singleEvents != null) "&singleEvents=$singleEvents" else "&singleEvents=false"

    val url =
            "https://www.googleapis.com/calendar/v3/calendars/primary/events?$eventOrNo$minOrNo$maxOrNo$resultOrNo&showDeleted=false"

    val headers =
            HttpHeaders().apply {
              contentType = MediaType.APPLICATION_JSON
              setBearerAuth(accessToken)
            }

    val entity = HttpEntity<String>(headers)
    val restTemplate = RestTemplate()

    return try {
      val response =
              restTemplate.exchange(url, HttpMethod.GET, entity, CalendarEventsResponse::class.java)
      response.body
    } catch (e: HttpClientErrorException.Unauthorized) {
      throw MessageException(handlerErrorGoogle(e))
    }
  }

  private fun handlerErrorGoogle(e: HttpClientErrorException): String {
    val body = e.responseBodyAsString
    val mapper = jacksonObjectMapper()
    val googleError =
            try {
              mapper.readTree(body).path("error").path("message").asText()
            } catch (_: Exception) {
              null
            }
    return googleError ?: "Problema na comunicação com a APi da Google"
  }

  private fun factoryEvemtRequest(): EventCreateRequest =
          EventCreateRequest(
                  summary = "Reunião de Teste",
                  // description = "Descrição do evento de teste",
                  description =
                          """
    Reunião para revisar o projeto.

    Agenda:
    - Item 1
    - Item 2

    Mais informações: https://meusite.com/reuniao
""".trimIndent(),
                  location = "Recife",
                  start = EventCreateDateTime(dateTime = "2025-08-01T10:00:00-03:00"),
                  end = EventCreateDateTime(dateTime = "2025-08-01T11:00:00-03:00")
          )

  private fun factorEvent(): EventItem =
          EventItem(
                  kind = "calendar#event",
                  etag = "",
                  id = "",
                  status = "confirmed",
                  htmlLink = "",
                  created = "",
                  updated = "",
                  summary = "Reunião de Teste",
                  description = "Descrição do evento de teste",
                  location = "Recife",
                  creator = Creator(email = "gustavo910@gmail.com", self = true),
                  organizer = Organizer(email = "gustavo910@gmail.com", self = true),
                  start = EventDateTime(dateTime = "2025-08-01T10:00:00-03:00"),
                  end = EventDateTime(dateTime = "2025-08-01T11:00:00-03:00"),
                  transparency = null,
                  visibility = null,
                  iCalUID = "",
                  sequence = 0,
                  attendees = null,
                  reminders = Reminders(useDefault = true, overrides = null),
                  eventType = "default"
          )
}
