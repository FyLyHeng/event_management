package com.norton.msit.event_management.auth

import com.norton.msit.event_management.telegram.NotificationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.jvm.optionals.getOrElse

@RestController
@RequestMapping("/api/v1/auth")
class AuthController {

    @Autowired
    private lateinit var repository: UserRepository
    @Autowired
    private lateinit var notificationService: NotificationService


    @PostMapping("/login")
    fun login(@RequestBody user: User): ResponseEntity<Any> {

         val data = repository.findFirstByUsername(user.username).getOrElse {

             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                 mapOf(
                     "status" to false,
                     "message" to "Account is not found"
                 )
             )
         }

        if (data.status != true) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                mapOf(
                    "status" to false,
                    "message" to "Account is not active"
                )
            )
        }

        if (data.password != user.password) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                mapOf(
                    "status" to false,
                    "message" to "Password is incorrect"
                )
            )
        }
        return ResponseEntity.ok().body(data)
    }


    @PostMapping("/register")
    fun singUp(@RequestBody user: User): ResponseEntity<Any> {

        if (user.telegramId == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                mapOf(
                    "status" to false,
                    "message" to "telegramId require"
                )
            )
        }

        if (repository.findFirstByTelegramId(user.telegramId).isPresent){

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                mapOf(
                    "status" to false,
                    "message" to "UserTelegramId already exists"
                )
            )
        }

        if (repository.findFirstByUsername(user.username).isPresent){

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                mapOf(
                    "status" to false,
                    "message" to "Username already exists"
                )
            )
        }

        user.status = false
        repository.save(user)
        notificationService.sendSignupConfirmation(user.telegramId!!)

        return ResponseEntity.ok().body(user)
    }
}