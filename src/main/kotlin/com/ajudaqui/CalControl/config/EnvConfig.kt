// package com.ajudaqui.CalControl.config


// import io.github.cdimascio.dotenv.Dotenv
// import org.springframework.context.annotation.Bean
// import org.springframework.context.annotation.Configuration

// @Configuration
// class EnvConfig {

//     private val dotenv = Dotenv.load()

//     @Bean
//     fun googleClientId(): String = dotenv["GOOGLE_CLIENT_ID"]
//         ?: throw IllegalStateException("GOOGLE_CLIENT_ID não definido no .env")

//     @Bean
//     fun googleClientSecret(): String = dotenv["GOOGLE_CLIENT_SECRET"]
//         ?: throw IllegalStateException("GOOGLE_CLIENT_SECRET não definido no .env")

//     @Bean
//     fun googleRedirectUri(): String = dotenv["GOOGLE_REDIRECT_URI"]
//         ?: throw IllegalStateException("GOOGLE_REDIRECT_URI não definido no .env")

//     @Bean
//     fun springAppName(): String = dotenv["SPRING_APPLICATION_NAME"]
//         ?: throw IllegalStateException("SPRING_APPLICATION_NAME não definido no .env")

//     @Bean
//     fun serverPort(): String = dotenv["SERVER_PORT"]
//         ?: throw IllegalStateException("SERVER_PORT não definido no .env")
// }
