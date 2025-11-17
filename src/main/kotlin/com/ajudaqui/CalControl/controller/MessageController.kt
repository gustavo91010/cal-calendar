package com.ajudaqui.CalControl.controller

import com.ajudaqui.CalControl.dto.MessageDTO
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/message")
class MessageController {
  fun push(@RequestHeader("Authorization") token: String, data: MessageDTO) {
    println("tokem " + token)
    println("payload: " + data)
  }

  // @RestController
  // @RequestMapping("/v1/message")
  // class MessageController {
  //     fun push(
  //         @RequestHeader("Authorization") token: String
  //         data: MessageDTO
  //  saAD       sadasa
  //         sdadasd

  //     ) {
  //         println("tokem de autenticação " + token)
  //         println("o payload foi: " + data.payload)
  //     }
}
