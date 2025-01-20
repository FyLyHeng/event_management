package com.norton.msit.event_management.event

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EventRepository : JpaRepository<Event, Long> {

    override fun findAll() : MutableList<Event>
}