package com.ajudaqui.CalControl.controller

import com.ajudaqui.CalControl.dto.UsersDTO
import com.ajudaqui.CalControl.dto.UsersUpdateDTO
import com.ajudaqui.CalControl.entity.Users
import com.ajudaqui.CalControl.service.UsersService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/v1/users")
class UsersController(private val usersService: UsersService) {
    private val logger = LoggerFactory.getLogger(UsersController::class.java)

    @PostMapping("/")
    fun create(@RequestBody usersDTO: UsersDTO): ResponseEntity<Users> =
        ResponseEntity.ok(usersService.create(usersDTO))

    @GetMapping("/all")
    fun findAll()=
        ResponseEntity.ok().body(usersService.findAll())

    @GetMapping("/email/{email}")
    fun findByEmail(@PathVariable email: String): ResponseEntity<Users> =
        ResponseEntity.ok().body(usersService.findByEmail(email))

    @GetMapping("/id/{userId}")
    fun findById(@PathVariable userId: Long): ResponseEntity<Users> =
        ResponseEntity.ok().body(usersService.findById(userId))

    @PutMapping("/{userId}")
    @Transactional
    fun update(@RequestBody usersDTO: UsersUpdateDTO, @PathVariable userId: Long): ResponseEntity<Users> =
        ResponseEntity.ok().body(usersService.update(userId, usersDTO))
}
