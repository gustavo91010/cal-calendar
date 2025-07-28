package com.ajudaqui.CalControl.service

import com.ajudaqui.CalControl.entity.Users
import com.ajudaqui.CalControl.repository.EventsRepository
import io.mockk.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class EventsServiceTest {

    private val repository = mockk<EventsRepository>()
    private val usersService = mockk<UsersService>()
    private val calendarService = mockk<GoogleCalendarService>()
    private val service = EventsService(repository, usersService, calendarService)

    @Test
    fun `deve buscar eventos`() {
        every { usersService.findByEmail("teste@email.com") } returns Users(id=1, email="teste@email.com", accessToken="abc")
        every { repository.findAllPeriod(any(), any(), any()) } returns listOf()
        every { calendarService.listEvents(any(), any(), any(), any(), any()) } returns null

        service.findAll("teste@email.com", LocalDateTime.now(), LocalDateTime.now())

        verify { repository.findAllPeriod(any(), any(), any()) }
    }
}
