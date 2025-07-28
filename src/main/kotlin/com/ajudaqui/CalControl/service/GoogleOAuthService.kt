package com.ajudaqui.CalControl.service

import com.ajudaqui.CalControl.entity.Users
import com.ajudaqui.CalControl.response.GoogleTokenResponse
import java.net.URI
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import org.slf4j.LoggerFactory
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

  private val logger = LoggerFactory.getLogger(GoogleOAuthService::class.java)
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

  // fun refreshAccessTokenByAccessToken(accessToken: String): Map<String, String> =
  //         mapOf("accessToken" to refreshTokeyByUser(usersService.findByAccessToken(accessToken)))

  fun refreshAccessTokenByHttp(email: String): Map<String, String> =
          mapOf("accessToken" to refreshTokeyByUser(usersService.findByEmail(email)))

  fun refreshTokeyByUser(user: Users): String {
    val refresh = refreshAccessToken(user.refreshToken ?: "")
    user.accessToken = refresh.access_token
    user.refreshTokenExpiresIn = refresh.expires_in
    usersService.save(user)
    return user.accessToken!!
  }

  private fun refreshAccessToken(refreshToken: String): GoogleTokenResponse {
    val headers = HttpHeaders()
    headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

    val body =
            LinkedMultiValueMap<String, String>().apply {
              add("client_id", clientId)
              add("client_secret", clientSecret)
              add("refresh_token", refreshToken)
              add("grant_type", "refresh_token")
            }

    val url = "https://oauth2.googleapis.com/token"
    val response =
            RestTemplate()
                    .postForEntity(url, HttpEntity(body, headers), GoogleTokenResponse::class.java)

    return response.body ?: throw RuntimeException("Erro ao renovar token")
  }

  fun checkingValidAccessToken(accessToken: String): String {
    val user = usersService.findByAccessToken(accessToken)
    return user
            .takeIf {
              val expirationTime = it.updatedAt.plusSeconds(it.refreshTokenExpiresIn ?: 0)
              expirationTime.isAfter(LocalDateTime.now())
            }
            ?.accessToken
            ?: refreshTokeyByUser(user).also {
              logger.info("AccessToken para email: ${user.email} foi atualizado")
            }
  }
}
