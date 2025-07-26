package com.ajudaqui.CalControl.service

// import org.springframework.format.annotation.DateTimeFormat
import com.ajudaqui.CalControl.dto.EventCreateDateTime
import com.ajudaqui.CalControl.dto.EventCreateRequest
import com.ajudaqui.CalControl.exceprion.custon.MessageException
import com.ajudaqui.CalControl.response.*
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.time.*
import java.time.format.DateTimeFormatter
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
    println("Objero no ggoogle calendar")
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
      // e.printStackTrace()
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
    val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    val now = LocalDate.now(ZoneOffset.UTC)
    val startOfDay = now.atStartOfDay().atOffset(ZoneOffset.UTC).format(formatter)
    val endOfDay = now.atTime(LocalTime.MAX).atOffset(ZoneOffset.UTC).format(formatter)

    val minOrNo =
            if (updatedMin != null) "&timeMin=$updatedMin"
            else "&timeMin=$startOfDay"
    val maxOrNo =
            if (updatedMax != null) "&timeMax=$updatedMax"
            else "&timeMax=$endOfDay"
    val resultOrNo = if (maxResults != null) "&maxResults=$maxResults" else "&maxResults=100"
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
