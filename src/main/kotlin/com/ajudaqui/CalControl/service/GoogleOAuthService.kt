package com.ajudaqui.CalControl.service

import com.ajudaqui.CalControl.response.GoogleTokenResponse
import java.net.URI
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
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

  fun refreshAccessToken(refreshToken: String): GoogleTokenResponse {
    val url = "https://oauth2.googleapis.com/token"

    val headers = HttpHeaders()
    headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

    val body = LinkedMultiValueMap<String, String>().apply {
        add("client_id", "SEU_CLIENT_ID")
        add("client_secret", "SEU_CLIENT_SECRET")
        add("refresh_token", refreshToken)
        add("grant_type", "refresh_token")
    }

    val request = HttpEntity(body, headers)

    val response = RestTemplate().postForEntity(
        url,
        request,
        GoogleTokenResponse::class.java
    )

    return response.body ?: throw RuntimeException("Erro ao renovar token")
}
}
//POST https://oauth2.googleapis.com/token
//client_id=SEU_CLIENT_ID
//client_secret=SEU_CLIENT_SECRET
//refresh_token=SEU_REFRESH_TOKEN
//grant_type=refresh_token
