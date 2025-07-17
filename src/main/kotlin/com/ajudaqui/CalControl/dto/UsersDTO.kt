package com.ajudaqui.CalControl.dto

import com.ajudaqui.CalControl.entity.Users

data class UsersDTO(val email: String) {
    fun toUsers(): Users {
        return Users(email = email)
    }
}