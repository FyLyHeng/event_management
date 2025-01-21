package com.norton.msit.event_management.telegram

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate


@Service
class NotificationService {
    private val restTemplate = RestTemplate()

    @Value("\${telegram.bot.token}")
    private val botToken = "7700193817:AAGEQrzIsy89AJde14gqjRyw2N8Zp0sMNsg"

    fun sendOrderNotification(chatId: String, orderDetails: String) {
        val apiUrl = "https://api.telegram.org/bot$botToken/sendMessage"

        val params: MutableMap<String, String> = HashMap()
        params["chat_id"] = chatId
        params["text"] = "Your order is being processed: $orderDetails"

        restTemplate.postForObject(apiUrl, params, String::class.java)
    }
}
