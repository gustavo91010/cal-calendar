package com.ajudaqui.CalControl.repository

import com.ajudaqui.CalControl.entity.Events
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface EventsRepository : JpaRepository<Events, Long> {
  // @Query(value = "SELECT * FROM users WHERE email =:email", nativeQuery = true) fun
  // findByEmail(email: String): Optional<Users>
  @Query(value = "SELECT e FROM Events e JOIN e.users u WHERE u.email= :email", nativeQuery = false)
  fun findAllByUserEmail(@Param("email") email: String): List<Events>
}
