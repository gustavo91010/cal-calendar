package com.ajudaqui.CalControl.response

data class GoogleTokenResponse(
    val access_token: String,
    val expires_in: Long,
    val scope: String,
    val token_type: String
)
