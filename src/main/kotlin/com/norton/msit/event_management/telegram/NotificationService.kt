package com.norton.msit.event_management.telegram

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.exceptions.TelegramApiException


@Service
class NotificationService {

    @Autowired
    private lateinit var myTelegramBot: MyTelegramBot

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

    fun sendStartInfo(chatId: String) {

    }

    fun sendSignupConfirmation(chatId: String) {

        val message = SendMessage()
        message.chatId = chatId
        message.text = "Hi! Please confirm your sign-up by clicking the button below."

        // Create InlineKeyboardMarkup with a button
        val confirmButton = InlineKeyboardButton()
        confirmButton.text = "Confirm Sign-Up"
        confirmButton.callbackData = "confirm_signup"

        val keyboardMarkup = InlineKeyboardMarkup()
        keyboardMarkup.keyboard = listOf(listOf(confirmButton))
        message.replyMarkup = keyboardMarkup

        try {
            myTelegramBot.execute(message)
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
    }
}
