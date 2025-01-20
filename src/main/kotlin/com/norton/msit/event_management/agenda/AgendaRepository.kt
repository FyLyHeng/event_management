package com.norton.msit.event_management.agenda

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AgendaRepository : JpaRepository<Agenda, Long> {

    override fun findAll() : MutableList<Agenda>
}