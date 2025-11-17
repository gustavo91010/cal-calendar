package com.ajudaqui.CalControl.controller

import com.ajudaqui.CalControl.dto.MessageDTO
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid

@RestController
@RequestMapping("/v1/message")
class MessageController {

  @PostMapping("")
  fun push(@RequestHeader("Authorization") token: String, @RequestBody @Valid data: MessageDTO): Map<String, String> {
    println("payload: " + data)


    return mapOf("message" to "Mensagem recebida com sucesso.")

  }
}
