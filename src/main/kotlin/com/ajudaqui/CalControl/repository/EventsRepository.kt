package com.ajudaqui.CalControl.repository

import com.ajudaqui.CalControl.entity.Events
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface EventsRepository : JpaRepository<Events, Long> {
  // @Query(value = "SELECT * FROM users WHERE email =:email", nativeQuery = true) fun
  // findByEmail(email: String): Optional<Users>
  @Query(value = "SELECT e FROM events e JOIN e.users u WHERE u.email= :email", nativeQuery = false)
  fun findAllByUserEmail(@Param("email") email: String): List<Events>

  // @Query(
  //         value =
  //                 "SELECT * FROM events WHERE users_id = :userId AND start_time = :start AND end_time = :finish",
  //         nativeQuery = true
  // )
  @Query(value = "SELECT * FROM events WHERE users_id = :userId AND start_time >= :start AND end_time <= :finish", nativeQuery = true)
  fun findAllPeriod(userId: Long, start: LocalDateTime, finish: LocalDateTime): List<Events>
  // @Query(value = "SELECT * FROM events WHERE start_time= :start AND end_time=:finish",
  // nativeQuery = true)
}
