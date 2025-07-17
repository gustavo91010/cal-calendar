package com.ajudaqui.CalControl.service

import com.ajudaqui.CalControl.dto.UsersDTO
import com.ajudaqui.CalControl.dto.UsersUpdateDTO
import com.ajudaqui.CalControl.entity.Users
import com.ajudaqui.CalControl.exceprion.custon.MessageException
import com.ajudaqui.CalControl.exceprion.custon.NotFoundException
import com.ajudaqui.CalControl.repository.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class UsersService(
    private val userRepository: UserRepository
) {
    fun create(usersDTO: UsersDTO): Users {
        userRepository.findByEmail(usersDTO.email).ifPresent {
            throw MessageException("Email já cadastrado")
        }
        return save(usersDTO.toUsers())
    }


    fun findByEmail(email: String): Users =
        userRepository.findByEmail(email).orElseThrow { NotFoundException("Usuário não encontrado") }

    fun findById(userId: Long): Users =
        userRepository.findById(userId).orElseThrow { NotFoundException("Usuário não encontrado") }

    fun update(userId: Long, usersUpdateDTO: UsersUpdateDTO): Users {
        val user = findById(userId)
        user.email = usersUpdateDTO.email ?: user.email
        user.refreshToken = usersUpdateDTO.refreshToken ?: user.refreshToken
        user.accessToken = usersUpdateDTO.accessType ?: user.accessToken
        return save(user)
    }

    fun save(user: Users): Users = userRepository.save(user.copy(updatedAt = LocalDateTime.now()))


}