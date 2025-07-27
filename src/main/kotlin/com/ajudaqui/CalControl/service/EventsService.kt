package com.ajudaqui.CalControl.service

import com.ajudaqui.CalControl.dto.EventCreateRequest
import com.ajudaqui.CalControl.entity.Events
import com.ajudaqui.CalControl.exceprion.custon.NotFoundException
import com.ajudaqui.CalControl.repository.EventsRepository
import com.ajudaqui.CalControl.response.EventItem
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.collections.emptyList
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
    if (eventItem == null) throw NotFoundException("Erro ao criar evento no google agenda")
    return repository.save(
            Events.form(eventCreate).apply {
              this.users = users
              this.eventId = eventItem.id
            }
    )
  }

  fun findAll(email: String, start: LocalDateTime, finish: LocalDateTime): List<EventItem> {
    val user = usersService.findByEmail(email)
    println("user id: ${user.id}")
    // val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    // val startDate = LocalDateTime.parse(start,formatter)
    // val finishDate = LocalDateTime.parse(finish,formatter)
    // println("start $startDate")
    // println("finish $finishDate")
    val eventDB = repository.findAllPeriod(user.id!!, start, finish)

    println("tamanho da lsiat do banco: ${eventDB.size}")
    val idEventIdDb = eventDB.mapNotNull { it.eventId }.toSet()
    var eventGoogle = calensarService.listEvents(user.accessToken!!, null, start.toString(), finish.toString(), 31)
    val itensGoogle = eventGoogle?.items ?: emptyList()

    return itensGoogle.filter { idEventIdDb.contains(it.id) }
  }
}
