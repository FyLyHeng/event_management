package com.norton.msit.event_management.attendee

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface AttendeeRepository : JpaRepository<Attendee, Long> {

    override fun findAll() : MutableList<Attendee>

    fun findFirstByEventIdAndUserTelegramId(eventId:Long?, chatId:String) : Optional<Attendee>

}