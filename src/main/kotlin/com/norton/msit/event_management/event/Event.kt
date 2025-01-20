package com.norton.msit.event_management.event

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDate


@Entity
data class Event (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?=0,

    var title: String? = null,
    var description: String? = null,

    @JsonIgnore
    var creationDate: LocalDate? = LocalDate.now(),

    @JsonFormat(pattern = "yyyy-MM-dd")
    val eventDate: LocalDate ? = LocalDate.now(),

    val totalAttendees : Long?=0,

    var eventVenueName: String? = null,
    var eventVenueAddress: String? = null,

    
    var createdBy: Long? = null,
)