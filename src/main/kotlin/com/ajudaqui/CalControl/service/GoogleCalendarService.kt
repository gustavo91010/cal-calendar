package com.ajudaqui.CalControl.service

// import org.springframework.format.annotation.DateTimeFormat
import com.ajudaqui.CalControl.dto.EventCreateDateTime
import com.ajudaqui.CalControl.dto.EventCreateRequest
import com.ajudaqui.CalControl.dto.EventItemUpdateDto
import com.ajudaqui.CalControl.exceprion.custon.MessageException
import com.ajudaqui.CalControl.response.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.time.*
import java.time.format.DateTimeFormatter
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

@Service
class GoogleCalendarService() {
  private val url: String = "https://www.googleapis.com/calendar/v3/calendars"
  fun createEvent(
          accessToken: String,
          calendarId: String? = "primary",
          event: EventCreateRequest?
  ): EventItem? {
    val url = "$url/$calendarId/events"
    val headers =
            HttpHeaders().apply {
              contentType = MediaType.APPLICATION_JSON
              setBearerAuth(accessToken)
            }
    return try {
      RestTemplate().postForEntity(url, HttpEntity(event, headers), EventItem::class.java).body
    } catch (e: HttpClientErrorException) {
      throw MessageException(handlerErrorGoogle(e))
    }
  }

  fun listEvents(
          accessToken: String,
          singleEvents: String?,
          timeMin: String?,
          timeMax: String?,
          maxResults: Long?
  ): CalendarEventsResponse? {
    val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    val startOfDay = LocalDate.now().atStartOfDay().atOffset(ZoneOffset.UTC).format(formatter)
    val endOfDay = LocalDate.now().atTime(LocalTime.MAX).atOffset(ZoneOffset.UTC).format(formatter)

    val minOrNo = if (timeMin != null) "&timeMin=$timeMin" else "&timeMin=$startOfDay"
    val maxOrNo = if (timeMax != null) "&timeMax=$timeMax" else "&timeMax=$endOfDay"
    val resultOrNo = if (maxResults != null) "&maxResults=$maxResults" else "&maxResults=100"
    val eventOrNo =
            if (singleEvents != null) "&singleEvents=$singleEvents" else "&singleEvents=false"

    val url = "$url/primary/events?$eventOrNo$minOrNo$maxOrNo$resultOrNo&showDeleted=false"
    println(url)
    val headers =
            HttpHeaders().apply {
              contentType = MediaType.APPLICATION_JSON
              setBearerAuth(accessToken)
            }

    val entity = HttpEntity<String>(headers)
    val restTemplate = RestTemplate()
    return try {
      restTemplate.exchange(url, HttpMethod.GET, entity, CalendarEventsResponse::class.java).body
    } catch (e: HttpClientErrorException.Unauthorized) {
      throw MessageException(handlerErrorGoogle(e))
    } catch (e: HttpClientErrorException.Gone) {
      throw MessageException(handlerErrorGoogle(e))
    }
  }

  fun getEventById(accessToken: String, eventId: String): EventItem? {
    val url = "$url/primary/events/$eventId"
    val headers =
            HttpHeaders().apply {
              contentType = MediaType.APPLICATION_JSON
              setBearerAuth(accessToken)
            }
    val entity = HttpEntity<String>(headers)
    return try {
      RestTemplate().exchange(url, HttpMethod.GET, entity, EventItem::class.java).body
    } catch (e: HttpClientErrorException) {
      throw MessageException(handlerErrorGoogle(e))
    }
  }

  private fun getEventByIdAsMap(
          accessToken: String,
          eventId: String,
          calendarId: String = "primary"
  ): MutableMap<String, Any>? {
    val endpoint = "${this.url}/$calendarId/events/$eventId"
    val headers =
            HttpHeaders().apply {
              contentType = MediaType.APPLICATION_JSON
              setBearerAuth(accessToken)
            }
    val entity = HttpEntity<String>(headers)
    val restTemplate = RestTemplate()
    return try {
      restTemplate.exchange(
                      endpoint,
                      HttpMethod.GET,
                      entity,
                      object : ParameterizedTypeReference<MutableMap<String, Any>>() {}
              )
              .body
    } catch (e: HttpClientErrorException) {
      throw MessageException(handlerErrorGoogle(e))
    }
  }

  fun updateEventDescriptin(
          accessToken: String,
          eventId: String,
          eventItemDtop: EventItemUpdateDto,
          calendarId: String = "primary"
  ): EventItem? {
    val currentEvent = getEventByIdAsMap(accessToken, eventId)
    if (currentEvent != null) {
      currentEvent["description"] = eventItemDtop.description
    }
    val url = "$url/$calendarId/events/$eventId"
    val headers =
            HttpHeaders().apply {
              contentType = MediaType.APPLICATION_JSON
              setBearerAuth(accessToken)
            }
    val entity = HttpEntity(currentEvent, headers)
    return try {
      RestTemplate().exchange(url, HttpMethod.PUT, entity, EventItem::class.java).body
    } catch (e: HttpClientErrorException) {
      e.printStackTrace()
      throw MessageException(handlerErrorGoogle(e))
    }
  }

  fun deleteEvent(accessToken: String, eventId: String) {
    val url = "$url/primary/events/$eventId"
    val headers =
            HttpHeaders().apply {
              contentType = MediaType.APPLICATION_JSON
              setBearerAuth(accessToken)
            }
    val entity = HttpEntity<String>(headers)
    try {
      RestTemplate().exchange(url, HttpMethod.DELETE, entity, Void::class.java)
    } catch (e: HttpClientErrorException) {
      throw MessageException(handlerErrorGoogle(e))
    }
  }

  private fun handlerErrorGoogle(e: HttpClientErrorException): String {
    val status = e.statusCode
    return try {
      val node = jacksonObjectMapper().readTree(e.responseBodyAsString).path("error")
      val detail =
              node.path("errors").get(0)?.path("message")?.asText() ?: node.path("message").asText()
      when (status) {
        HttpStatus.UNAUTHORIZED -> "Não autorizado: $detail"
        HttpStatus.GONE -> "Recurso já removido: $detail"
        else -> "Erro Google ($status): $detail"
      }
    } catch (_: Exception) {
      "Problema na comunicação com a API da Google"
    }
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
