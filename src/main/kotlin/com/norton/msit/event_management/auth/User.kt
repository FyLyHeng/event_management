package com.norton.msit.event_management.auth

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class User (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = 0,
    var username: String? = null,
    var password: String? = null,

    var firstName: String? = null,
    var lastName: String? = null,
    var email: String? = null,
    var phone: String? = null,

    var roles: String? = RoleName.Guest.name,

    var imageUrl: String? = null,

    )

enum class RoleName {
    Guest,
    Admin
}