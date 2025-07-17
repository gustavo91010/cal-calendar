package com.ajudaqui.CalControl.utils

import com.auth0.jwt.JWT
import org.springframework.stereotype.Component

@Component
class JwtUtils {

  fun getEmailFromJwkToken(token: String): String? {
    return try {

      val jwt = JWT.decode(token)
      jwt.getClaim("email").asString()
    } catch (e: Exception) {
      null
    }
  }
}
