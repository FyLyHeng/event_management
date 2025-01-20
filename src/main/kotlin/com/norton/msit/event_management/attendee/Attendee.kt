package com.norton.msit.event_management.attendee

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDate


@Entity
data class Attendee (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?=0,
    var eventId: Long?=0,

    var firstname: String? = null,
    var lastName: String? = null,
    val email : String?=null,
    val phone : String?=null,

    var description: String? = null,

    @JsonIgnore
    var createdBy: Long? = null,
    @JsonIgnore
    var creationDate: LocalDate? = LocalDate.now(),
)