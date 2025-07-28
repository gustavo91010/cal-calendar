package com.ajudaqui.CalControl.service

import com.ajudaqui.CalControl.dto.EventCreateRequest
import com.ajudaqui.CalControl.dto.EventItemUpdateDto
import com.ajudaqui.CalControl.entity.Events
import com.ajudaqui.CalControl.exceprion.custon.NotAutorizationException
import com.ajudaqui.CalControl.exceprion.custon.NotFoundException
import com.ajudaqui.CalControl.mapper.EventsMapper
import com.ajudaqui.CalControl.repository.EventsRepository
import com.ajudaqui.CalControl.response.EventItem
import java.time.*
import java.time.format.DateTimeFormatter
import kotlin.collections.emptyList
import org.springframework.stereotype.Service

@Service
class EventsService(
        private val repository: EventsRepository,
        private val usersService: UsersService,
        private val calendarService: GoogleCalendarService,
) {

  fun create(email: String, eventCreate: EventCreateRequest): Events {
    var users = usersService.findByEmail(email)
    val eventItem = calendarService.createEvent(users.accessToken ?: "", "primary", eventCreate)
    if (eventItem == null) throw NotFoundException("Erro ao criar evento no google agenda")
    return save(
            Events.form(eventCreate).apply {
              this.users = users
              this.eventId = eventItem.id
            }
    )
  }

  fun findAll(email: String, start: LocalDateTime, finish: LocalDateTime): List<EventItem> {
    val user = usersService.findByEmail(email)
    val eventDB = repository.findAllPeriod(user.id!!, start, finish)

    val startUtc = start.atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    val finishUtc = finish.atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    val idEventIdDb = eventDB.mapNotNull { it.eventId }.toSet()
    var eventGoogle = calendarService.listEvents(user.accessToken!!, null, startUtc, finishUtc, 31)
    val itensGoogle = eventGoogle?.items ?: emptyList()
    return itensGoogle.filter { idEventIdDb.contains(it.id) }
  }

  fun findById(email: String, eventId: Long): EventItem {
    val accessToken = usersService.findByEmail(email).accessToken!!
    val event =
            repository.findById(eventId).orElseThrow { NotFoundException("Evento não localizado") }
    event.takeIf { it.users?.email == email }
            ?: throw NotAutorizationException("Solicitação não permitida")
    var googleEvent =
            calendarService.getEventById(accessToken, event.eventId!!)
                    ?: throw NotFoundException("ta la no google nÃo")
    validAttEvent(googleEvent, event)
    save(EventsMapper.fromEventItem(event.id!!, googleEvent))
    return googleEvent
  }

  fun update(
          email: String,
          eventId: Long,
          eventItemDtop: EventItemUpdateDto,
  ): EventItem? {
    val accessToken =
            usersService.findByEmail(email).accessToken
                    ?: throw NotAutorizationException("AccessToken não localizad")
    val event =
            repository.findById(eventId).orElseThrow { NotFoundException("Evento não localizado") }
    event.takeIf { it.users?.email == email }
            ?: throw NotAutorizationException("Solicitação não permitida")
    return calendarService.updateEventDescriptin(accessToken, event.eventId!!, eventItemDtop)
            ?.also { save(EventsMapper.fromEventItem(event.id!!, it)) }
  }

  fun delete(email: String, eventId: Long) {
    val accessToken =
            usersService.findByEmail(email).accessToken
                    ?: throw NotAutorizationException("AccessToken não localizad")
    val event =
            repository.findById(eventId).orElseThrow { NotFoundException("Evento não localizado") }
    event.takeIf { it.users?.email == email }
            ?: throw NotAutorizationException("Solicitação não permitida")
    if (calendarService.deleteEvent(accessToken, event.eventId!!)) repository.delete(event)
  }

  private fun validAttEvent(googleEvent: EventItem, event: Events) {
    val updateGogle = parseToLocalDateTime(googleEvent.updated)
    if (updateGogle.isAfter(event.updatedAt))
            save(EventsMapper.fromEventItem(event.id!!, googleEvent))
  }

  private fun save(event: Events): Events =
          repository.save(event.apply { this.updatedAt = LocalDateTime.now() })

  private fun parseToLocalDateTime(dateTime: String?): LocalDateTime =
          OffsetDateTime.parse(dateTime).toLocalDateTime()
}
