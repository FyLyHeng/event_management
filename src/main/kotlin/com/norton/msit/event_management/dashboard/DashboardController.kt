package com.norton.msit.event_management.dashboard

import com.norton.msit.event_management.attendee.AttendeeRepository
import com.norton.msit.event_management.event.EventRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/dashboard")
class DashboardController {

    @Autowired
    private lateinit var eventRepository : EventRepository
    @Autowired
    private lateinit var registrationRepository: AttendeeRepository


    @GetMapping("/summary/{ownerId}")
    fun getMetricsSummary(@PathVariable ownerId : Long): ResponseEntity<Any> {

        val totalEvents = eventRepository.countEvents(ownerId) ?: 0
        val upcomingEvents = eventRepository.countUpcomingEvents(ownerId) ?: 0
        val totalRegistrations = registrationRepository.countTotalRegistrations(ownerId) ?: 0
        val confirmedRegistrations = registrationRepository.countConfirmedRegistrations(ownerId) ?: 0
        val pendingRegistrations = totalRegistrations - confirmedRegistrations

        val data = mapOf(
            "totalEvents" to totalEvents,
            "upcomingEvents" to upcomingEvents,
            "totalRegistrations" to totalRegistrations,
            "confirmedRegistrations" to confirmedRegistrations,
            "pendingRegistrations" to pendingRegistrations
        )
        return ResponseEntity.ok().body(data)
    }


    @GetMapping("/user-register/{ownerId}")
    fun listUserRegistrations(@PathVariable ownerId : Long): ResponseEntity<Any> {

        val data = registrationRepository.listLast10Registrations(ownerId).mapIndexed { i, it ->
            mapOf(
                "activityId" to i+1,
                "guestName" to it.userFullName,
                "action" to "guest register",
                "stage" to it.attendanceStatus,
                "eventName" to it.eventName,
                "registerDate" to it.registerDate

            )
        }
        return ResponseEntity.ok().body(data)
    }



}