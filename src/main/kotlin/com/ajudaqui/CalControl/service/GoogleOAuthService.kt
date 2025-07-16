package com.ajudaqui.CalControl.service

import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.util.LinkedMultiValueMap

@Service
class GoogleOAuthService (
   @Value("\${google.client.id}")
  private val clientId: String,

  @Value("\${google.client.secret}")
  private val clientSecret: String,

  @Value("\${google.redirect.uri}")
  private val redirectUri: String
){


    fun exchangeCodeForToken(code: String): Map<String, Any>? {
        val url = "https://oauth2.googleapis.com/token"

        val body = LinkedMultiValueMap<String, String>().apply {
            add("code", code)
            add("client_id", clientId)
            add("client_secret", clientSecret)
            add("redirect_uri", redirectUri)
            add("grant_type", "authorization_code")
        }

        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_FORM_URLENCODED
        }

        val entity = HttpEntity(body, headers)
        val restTemplate = RestTemplate()
        val response = restTemplate.exchange(url, HttpMethod.POST, entity, Map::class.java)

        return response.body as? Map<String, Any>
    }



    fun listEvents(accessToken:String):Map <String, Any>?{
    val url= "https://www.googleapis.com/calendar/v3/calendars/primary/events"
    val headers = HttpHeaders().apply {
      contentType = MediaType.APPLICATION_JSON
      setBearerAuth(accessToken) }

    val entity = HttpEntity<String>( headers)
    val restTemplate = RestTemplate()
    val respnse = restTemplate.exchange(url, HttpMethod.GET, entity, Map::class.java)

    return respnse.body as? Map<String, Any>
  }
}
