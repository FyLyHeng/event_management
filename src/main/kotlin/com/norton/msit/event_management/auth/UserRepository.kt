package com.norton.msit.event_management.auth

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Long> {

    override fun findAll() : MutableList<User>

    fun findFirstByTelegramId(terminalId: String?) : Optional<User>

    fun findFirstByUsername(username: String?) : Optional<User>
}