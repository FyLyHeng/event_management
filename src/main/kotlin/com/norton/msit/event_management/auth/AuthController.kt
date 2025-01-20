package com.norton.msit.event_management.auth

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

        repository.findFirstByUsername(user.username).ifPresent {
            throw BadRequestException("Username already exists")
        }

        repository.save(user)
        return ResponseEntity.ok().body(user)
    }
}