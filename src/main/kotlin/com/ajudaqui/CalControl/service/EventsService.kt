package com.ajudaqui.CalControl.service

import com.ajudaqui.CalControl.dto.EventCreateRequest
import com.ajudaqui.CalControl.entity.Events
import com.ajudaqui.CalControl.exceprion.custon.NotFoundException
import com.ajudaqui.CalControl.repository.EventsRepository
import java.time.LocalDate
import org.springframework.cglib.core.Local
import org.springframework.stereotype.Service

@Service
class EventsService(
        private val repository: EventsRepository,
        private val usersService: UsersService,
        private val calensarService: GoogleCalendarService,
) {

  fun create(email: String, eventCreate: EventCreateRequest): Events {
    var users = usersService.findByEmail(email)
    val eventItem = calensarService.createEvent(users.accessToken ?: "", "primary", eventCreate)
    if (eventItem == null) throw NotFoundException("deu erradao!")
    return repository.save(
            Events.form(eventCreate).apply {
              this.users = users
              this.eventId = eventItem.id
            }
    )
  }

  // fun findAll(email: String): List<Events> = repository.findAllByUserEmail(email)
  fun findAll(email: String, start: LocalDate, finish: Local) {
    val user = usersService.findByEmail(email)
    calensarService.listEvents(email, null, start.toString(), finish.toString(), 100)
    // repository.findAllByUserEmail(email)
  }
}
