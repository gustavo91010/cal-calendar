package com.ajudaqui.CalControl.repository

import com.ajudaqui.CalControl.entity.Users
import java.util.Optional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserRepository : JpaRepository<Users, Long> {
  @Query(value = "SELECT * FROM users WHERE email =:email", nativeQuery = true)
  fun findByEmail(email: String): Optional<Users>

  @Query(value = "SELECT * FROM users WHERE access_token =:accessToken", nativeQuery = true)
  fun findByAccessToken(accessToken: String): Optional<Users>
}
