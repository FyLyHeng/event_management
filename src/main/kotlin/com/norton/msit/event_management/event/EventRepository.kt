package com.norton.msit.event_management.event

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface EventRepository : JpaRepository<Event, Long> {

    override fun findAll() : MutableList<Event>

    fun findFirstById(id:Long?) : Optional<Event>

    @Query("SELECT COUNT(e) FROM event e WHERE e.created_by = ?1", nativeQuery = true)
    fun countEvents(ownerId: Long): Int?

    @Query("SELECT COUNT(e) FROM Event e WHERE e.created_by = ?1 AND e.event_date >= CURRENT_DATE", nativeQuery = true)
    fun countUpcomingEvents( ownerId: Long): Int?
}