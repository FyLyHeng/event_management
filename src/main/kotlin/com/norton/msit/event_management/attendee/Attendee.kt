package com.norton.msit.event_management.attendee

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDate


@Entity
data class Attendee(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = 0,


    var eventId: Long? = 0,
    var userId: Long? = 0,

    var eventName: String? = null,
    var eventDate: LocalDate? = LocalDate.now(),

    var userFullName: String? = null,
    val userEmail: String? = null,
    val userPhone: String? = null,
    val userTelegramId: String? = null,

    var attendanceStatus: String? = AttendanceStatus.Pending.name,

    var description: String? = null,

    var userConfirmed : Boolean? = false,


    @JsonIgnore
    var registerDate: LocalDate? = LocalDate.now(),
)

enum class AttendanceStatus {
    Pending,
    Joined
}