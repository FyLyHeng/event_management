package com.norton.msit.event_management.auth

import com.norton.msit.event_management.telegram.NotificationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController {

    @Autowired
    private lateinit var repository: UserRepository
    @Autowired
    private lateinit var notificationService: NotificationService


    @PostMapping("/login")
    fun login(@RequestBody user: User): ResponseEntity<User> {

         val data = repository.findFirstByUsername(user.username).orElseThrow {
             NotFoundException("Username not found")
         }

        if (data.password != user.password) {
            throw BadRequestException("Password is incorrect")
        }

        return ResponseEntity.ok().body(data)
    }


    @PostMapping("/register")
    fun singUp(@RequestBody user: User): ResponseEntity<User> {

        if (user.telegramId == null){
            throw BadRequestException("Telegram ID require")
        }

        var exist = repository.findFirstByTelegramId(user.telegramId)

        if (exist.isPresent){

        }

        repository.findFirstByUsername(user.username).ifPresent {
            throw BadRequestException("Username already exists")
        }

        user.status = false
        repository.save(user)
        notificationService.sendSignupConfirmation(user.telegramId!!)

        return ResponseEntity.ok().body(user)
    }
}