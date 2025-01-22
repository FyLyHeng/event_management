package com.norton.msit.event_management.telegram

import com.norton.msit.event_management.attendee.EventAttendee
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.telegram.telegrambots.meta.api.methods.ParseMode
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

    fun send(data : SendMessage) {
        myTelegramBot.execute(data)
    }

    fun sendStartInfo(chatId: String, registerUrl: String) {

        val message = "👋 Welcome to Event Management System Bot!\n\n" +
                "Here's what I can do for you:\n" +
                "✅ sign up by web [event.mgt.singup.com]($registerUrl)\n" +
                "✅ Use Your ID : $chatId for fill in Sign-Up form.\n" +
                "\n" +
                "✅ sign up quickly by clicking the button below.\n" +
                "\n" +
                "👉 Click here to sign up"

        val msg = SendMessage(chatId, message)
        msg.parseMode = ParseMode.MARKDOWN
        myTelegramBot.execute(msg)
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

    fun setRegisterConfirmation(chatId: String,eventId: Long, eventName: String, eventDate: String, eventLocation: String) {

        val message = SendMessage()
        message.chatId = chatId
        message.text = """
        🎉 **Event Registration Confirmation** 🎉
        
        You're invited to join:
        📅 **Event:** $eventName  
        🗓️ **Date:** $eventDate  
        📍 **Location:** $eventLocation
        
        Please confirm your registration by clicking the button below. We can't wait to see you there! 😊
    """.trimIndent()
        message.parseMode = ParseMode.MARKDOWN


        // Create InlineKeyboardMarkup with a button
        val confirmButton = InlineKeyboardButton()
        confirmButton.text = "✅ Confirm Registration"
        confirmButton.callbackData = "confirm_event_register:$eventId"

        val keyboardMarkup = InlineKeyboardMarkup()
        keyboardMarkup.keyboard = listOf(listOf(confirmButton))
        message.replyMarkup = keyboardMarkup

        try {

            myTelegramBot.execute(message)

        } catch (e: TelegramApiException) {

            e.printStackTrace()
        }
    }

    fun sendTicketNumber(chatId: String, eventName: String, ticketNumber: String) {
        val message = SendMessage()
        message.chatId = chatId
        message.text = """
        🎟️ **Your Ticket for the Event** 🎟️

        You've successfully registered for the event!
        
        🏷️ **Ticket Number:** $ticketNumber
        
        📅 **Event Name:** $eventName

        Please keep this ticket number safe. We look forward to seeing you at the event!

        If you have any questions, feel free to reach out. 😊
    """.trimIndent()
        message.parseMode = ParseMode.MARKDOWN

        try {
            myTelegramBot.execute(message)
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
    }

    fun sendEventReminder(chatId: String, event: EventAttendee) {
        val message = SendMessage()
        message.chatId = chatId
        message.text = """
        🎉 Reminder: Your event "${event.eventName}" is happening in just 1 days! 🎉

        🗓 Date: ${event.eventDate}
        🕒 Time: ${event.eventDate?.atStartOfDay()}
        📍 Location: ${event.eventVenueName}

        Don't forget to join us for this amazing experience!

        See you there! 😊
    """.trimIndent()

        try {
            myTelegramBot.execute(message)
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
    }

}
