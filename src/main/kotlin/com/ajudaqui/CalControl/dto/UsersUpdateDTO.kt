package com.ajudaqui.CalControl.dto

data class UsersUpdateDTO(
        val email: String? = null,
        val accessToken: String? = null,
        val refreshTokenEexpiresIn: Long? = null,
        val scope: String? = null,
        val tokenType: String? = null,
)
