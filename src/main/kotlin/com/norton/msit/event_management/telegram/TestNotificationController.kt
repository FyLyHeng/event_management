package com.norton.msit.event_management.telegram

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class TestNotificationController {

    private val notificationService = NotificationService()

    @PostMapping("/notification")
    fun testNotification(@RequestParam userChatId: String): ResponseEntity<String> {
        notificationService.sendOrderNotification(userChatId, "Event # 01 Test. Thanks for your registration.")

        return ResponseEntity.ok("Notification sent")
    }
}