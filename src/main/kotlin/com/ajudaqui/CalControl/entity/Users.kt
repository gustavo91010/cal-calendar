package com.ajudaqui.CalControl.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.hibernate.annotations.Columns
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

@Entity
data class Users(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    var email: String,
    @Column(name= "access_token")
    var accessToken: String?=null,
    @Column(name= "refresh_token")
    var refreshToken: String?=null,
    @CreatedDate
    var createdAt: LocalDateTime = LocalDateTime.now(),
    @LastModifiedDate
    var updatedAt: LocalDateTime = LocalDateTime.now(),
){
    constructor() : this(email = "")
}