package com.ajudaqui.CalControl.service

import com.ajudaqui.CalControl.dto.EventCreateRequest
import com.ajudaqui.CalControl.entity.Events
import com.ajudaqui.CalControl.exceprion.custon.NotFoundException
import com.ajudaqui.CalControl.repository.EventsRepository
import org.springframework.stereotype.Service

@Service
class EventsService(
        private val repository: EventsRepository,
        private val usersService: UsersService,
        private val calensarService: GoogleCalendarService,
) {

  fun create(email: String, eventCreate: EventCreateRequest): Events {
    var users = usersService.findByEmail(email)
    val eventItem = calensarService.createEvent(users.accessToken!!, "primary", eventCreate)
    println(eventItem)
    if (eventItem == null) {
      throw NotFoundException("deu erradao!")
    }
      print("chegoua qui foi???")
      print("chegoua qui foi???")
      print("chegoua qui foi???")
    return repository.save(
            Events.form(eventCreate).apply {
              this.users = users
              this.eventId = eventItem.id
            }
    )
  }

  fun findAll(email: String): List<Events> = repository.findAllByUserEmail(email)
}
