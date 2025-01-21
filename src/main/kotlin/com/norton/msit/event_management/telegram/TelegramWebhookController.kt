package com.norton.msit.event_management.telegram

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/telegram")
class TelegramWebhookController {

    val logger = LoggerFactory.getLogger(TelegramWebhookController::class.java)


    @Autowired
    private lateinit var notificationService : NotificationService


    @PostMapping("/webhook")
    fun handleTelegramWebhook(@RequestBody update: String): ResponseEntity<String> {

        //val data = gson.fromJson(update, Map::class.java)

        logger.info("Webhook received: $update")


        // Process the incoming Telegram update
//        val message = update["message"] as Map<String, *>?
//        if (message != null) {
//
//            val chatId: String = message["chat"].get("id").toString()
//            val userMessage = message["text"].toString()
//
//            notificationService.sendOrderNotification(chatId, "Thanks for your message: $userMessage")
//        }
        return ResponseEntity.ok("Webhook received")
    }

}
