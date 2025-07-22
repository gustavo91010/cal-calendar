package com.ajudaqui.CalControl.service

import com.ajudaqui.CalControl.dto.EventCreateRequest
import com.ajudaqui.CalControl.entity.Events
import com.ajudaqui.CalControl.repository.EventsRepository
import org.springframework.stereotype.Service

@Service
class EventsService(
        private val repository: EventsRepository,
        private val usersService: UsersService,
) {

  fun create(email: String, eventCreate: EventCreateRequest) {
    var events = Events.form(eventCreate).apply { users = usersService.findByEmail(email) }
    repository.save(events)
  }
  fun findAll(email: String): List<Events> = repository.findAllByUserEmail(email)


}
