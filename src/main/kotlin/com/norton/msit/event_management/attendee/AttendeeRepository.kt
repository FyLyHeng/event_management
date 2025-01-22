package com.norton.msit.event_management.attendee

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface AttendeeRepository : JpaRepository<EventAttendee, Long> {

    override fun findAll() : MutableList<EventAttendee>

    fun findAllByEventId(eventId:Long) : MutableList<EventAttendee>
    fun findFirstByEventIdAndUserTelegramId(eventId:Long?, chatId:String) : Optional<EventAttendee>

}