package com.ajudaqui.CalControl.service

import com.ajudaqui.CalControl.dto.UsersDTO
import com.ajudaqui.CalControl.dto.UsersUpdateDTO
import com.ajudaqui.CalControl.entity.Users
import com.ajudaqui.CalControl.exceprion.custon.MessageException
import com.ajudaqui.CalControl.exceprion.custon.NotFoundException
import com.ajudaqui.CalControl.repository.UserRepository
import com.ajudaqui.CalControl.utils.JwtUtils
import java.time.LocalDateTime
import org.springframework.stereotype.Service

@Service
class UsersService(
        private val userRepository: UserRepository,
        private val jwtUtils: JwtUtils,
) {
  fun create(usersDTO: UsersDTO): Users {
    print(usersDTO)
    userRepository.findByEmail(usersDTO.email).ifPresent {
      throw MessageException("Email já cadastrado")
    }
    return save(usersDTO.toUsers())
  }

  fun findAll() = userRepository.findAll()

  fun findByEmail(email: String): Users =
          userRepository.findByEmail(email).orElseThrow {
            NotFoundException("Usuário não encontrado")
          }

  fun findById(userId: Long): Users =
          userRepository.findById(userId).orElseThrow {
            NotFoundException("Usuário não encontrado")
          }

  fun update(userId: Long, usersUpdateDTO: UsersUpdateDTO): Users {
    val user = findById(userId)
    user.email = usersUpdateDTO.email ?: user.email
    user.accessToken = usersUpdateDTO.accessToken ?: user.accessToken
    user.refreshTokenExpiresIn = usersUpdateDTO.refreshTokenEexpiresIn ?: user.refreshTokenExpiresIn
    user.scope = usersUpdateDTO.scope ?: user.scope
    return save(user)
  }

  fun save(user: Users): Users = userRepository.save(user.copy(updatedAt = LocalDateTime.now()))

  fun attUserByAuthGoogle(auth: Map<String, Any>) {
    val idToken = auth["id_token"] as? String ?: ""
    println()
    println()
    println(auth.keys)
    println()
    println()
    var emailByToken = jwtUtils.getEmailFromJwkToken(idToken) ?: ""

    val user =
            userRepository
                    .findByEmail(emailByToken)
                    .map { user ->
                      user.apply {
                        email = emailByToken
                        refreshToken= auth["refresh_token"] as? String
                        accessToken = auth["access_token"] as? String
                        refreshTokenExpiresIn =
                                (auth["refresh_token_expires_in"] as? Number)?.toLong()
                        scope = auth["scope"] as? String
                        tokenType = auth["token_type"] as? String
                      }
                    }
                    .orElseGet { factorUserAuthGoogle(auth) }
    if (user.email.isEmpty()) {
      throw NotFoundException("email vaioz...")
    }
    userRepository.save(user)
  }

  private fun factorUserAuthGoogle(auth: Map<String, Any>): Users {
    val emailForToken = jwtUtils.getEmailFromJwkToken(auth["id_token"] as? String ?: "") ?: ""
    return Users(
            email = emailForToken,
            accessToken = auth["access_token"] as? String,
            refreshToken= auth["refresh_token"] as? String,
            refreshTokenExpiresIn = (auth["refresh_token_expires_in"] as? Number)?.toLong(),
            scope = auth["scope"] as? String,
            tokenType = auth["token_type"] as? String,
    )
  }
}
