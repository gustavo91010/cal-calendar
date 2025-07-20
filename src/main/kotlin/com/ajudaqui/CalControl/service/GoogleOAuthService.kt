package com.ajudaqui.CalControl.service

import com.ajudaqui.CalControl.dto.CalendarEventsResponse
import com.ajudaqui.CalControl.exceprion.custon.MessageException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.net.URI
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

@Service
class GoogleOAuthService(
        private val usersService: UsersService,
        @Value("\${google.client.id}") private val clientId: String,
        @Value("\${google.client.secret}") private val clientSecret: String,
        @Value("\${google.redirect.uri}") private val redirectUri: String,
) {

  fun exchangeCodeForToken(code: String): Map<String, Any>? {
    val url = "https://oauth2.googleapis.com/token"
    val body =
            LinkedMultiValueMap<String, String>().apply {
              add("code", code)
              add("client_id", clientId)
              add("client_secret", clientSecret)
              add("redirect_uri", redirectUri)
              add("grant_type", "authorization_code")
            }
    val headers = HttpHeaders().apply { contentType = MediaType.APPLICATION_FORM_URLENCODED }
    val entity = HttpEntity(body, headers)
    val restTemplate = RestTemplate()
    val response = restTemplate.exchange(url, HttpMethod.POST, entity, Map::class.java)

    return response.body as? Map<String, Any>
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

  private fun handlerErrorGoogle(e: HttpClientErrorException.Unauthorized): String {
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

  fun authorizedUri(): URI? {
    val permissionCalendar = "https://www.googleapis.com/auth/calendar"
    val permissionEmail = "https://www.googleapis.com/auth/userinfo.email"
    val urlScope = "$permissionCalendar $permissionEmail"
    val uriGoogleAuth = "https://accounts.google.com/o/oauth2/v2/auth"
    val params = "?client_id=$clientId&redirect_uri=$redirectUri"
    val scopeEncode = URLEncoder.encode(urlScope, StandardCharsets.UTF_8.toString())
    val scope = "&scope=$scopeEncode"
    val responseType = "&response_type=code"
    val prompt = "&prompt=consent"
    val accessType = "&access_type=offline"

    return URI.create(uriGoogleAuth + params + scope + responseType + prompt + accessType)
  }

  fun validToken(code: String): String {
    val response = exchangeCodeForToken(code) ?: emptyMap()
    usersService.attUserByAuthGoogle(response)

    return "Autenticação conluida"
  }
}
