package com.norton.msit.event_management.agenda

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDate


@Entity
data class Agenda (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?=0,
    var eventId: Long?=0,

    var title: String? = null,
    val agendaNo : Int?=1,
    var description: String? = null,

    //@JsonFormat(pattern = "yyyy-MM-dd")
    val agendaDate: LocalDate ? = LocalDate.now(),
    val agendaDuration : String?="0 min",


    @JsonIgnore
    var createdBy: Long? = null,
    @JsonIgnore
    var creationDate: LocalDate? = LocalDate.now(),
)