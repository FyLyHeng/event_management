package com.norton.msit.event_management.attendee

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface AttendeeRepository : JpaRepository<EventAttendee, Long> {

    override fun findAll() : MutableList<EventAttendee>

    fun findAllByEventId(eventId:Long) : MutableList<EventAttendee>
    fun findFirstByEventIdAndUserTelegramId(eventId:Long?, chatId:String) : Optional<EventAttendee>

    @Query("SELECT COUNT(r) FROM attendee r WHERE r.event_owner_id =?1", nativeQuery = true)
    fun countTotalRegistrations( ownerId : Long): Int?

    @Query("SELECT COUNT(r) FROM attendee r WHERE  r.event_owner_id =?1 and r.user_confirmed = true", nativeQuery = true)
    fun countConfirmedRegistrations(ownerId : Long): Int?

    @Query("SELECT * FROM attendee r WHERE r.event_owner_id = ?1 ORDER BY r.register_date DESC LIMIT 10", nativeQuery = true)
    fun listLast10Registrations(ownerId : Long): MutableList<EventAttendee>

}