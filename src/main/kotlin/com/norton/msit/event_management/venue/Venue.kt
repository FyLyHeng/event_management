package com.norton.msit.event_management.venue

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDate


@Entity
data class Venue (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?=0,

    var name: String? = null,
    var address: String? = null,
    var description: String? = null,
    @JsonIgnore
    var creationDate: LocalDate? = LocalDate.now()
)