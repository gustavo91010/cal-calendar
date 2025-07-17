package com.ajudaqui.CalControl.repository

import com.ajudaqui.CalControl.entity.Users
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface UserRepository : JpaRepository<Users, Long>{
    fun findByEmail(email: String): Optional<Users>
}