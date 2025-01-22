package com.norton.msit.event_management.telegram

import com.norton.msit.event_management.attendee.AttendeeRepository
import com.norton.msit.event_management.auth.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.telegram.telegrambots.meta.api.methods.ParseMode
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.exceptions.TelegramApiException


@RestController
@RequestMapping("/telegram")
class TelegramWebhookController {

    val logger = LoggerFactory.getLogger(TelegramWebhookController::class.java)

    @Autowired
    private lateinit var myTelegramBot: MyTelegramBot
    @Autowired
    private lateinit var notificationService: NotificationService
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var eventGuestRepository: AttendeeRepository

    @Value("\${public.url}")
    private lateinit var publicUrl: String


    @PostMapping("/webhook")
    fun handleUpdate(@RequestBody update: Update) {

        logger.info("telegram received : $update")

        if (update.hasMessage() && update.message.text == "/start") {

            val registerUrl = "$publicUrl/api/v1/auth/register"
            notificationService.sendStartInfo(update.message.chatId.toString(), registerUrl)
        }

        if (update.hasMessage() && update.message.text == "/id") {

            val chatId = update.message.chatId.toString()
            val message = SendMessage(chatId, "Your ID is : $chatId")
            notificationService.send(message)
        }


        if (update.hasCallbackQuery()) {

            val callbackData = update.callbackQuery.data
            val chatId = update.callbackQuery.message.chatId.toString()

            try {

                if (callbackData.startsWith("confirm_signup")) {

                    val user = userRepository.findFirstByTelegramId(chatId)
                    if (user.isPresent)
                    {
                        user.get().status = true
                        userRepository.save(user.get())
                        myTelegramBot.execute(SendMessage(chatId, "Your sign-up is confirmed. Welcome!"))
                        return
                    }
                    else {
                        myTelegramBot.execute(SendMessage(chatId, "The User ID Not Match. Please Try Again."))
                        return
                    }
                }

                if (callbackData.startsWith("confirm_event_register:")) {

                    val eventId = callbackData.split(":")[1].toLong()
                    val user = userRepository.findFirstByTelegramId(chatId)
                    if (user.isEmpty) {
                        myTelegramBot.execute(SendMessage(chatId, "The User ID Not Match. Please Try Again."))
                        return
                    }

                    val eventGuest = eventGuestRepository.findFirstByEventIdAndUserTelegramId(eventId, chatId)
                    if (eventGuest.isEmpty) {
                        myTelegramBot.execute(SendMessage(chatId, "The Event Not Match. Please Try Again."))
                        return
                    }

                    val ticketNumber = eventGuest.get().ticketNumber
                    if (ticketNumber == null) {
                        eventGuest.get().ticketNumber = generateTicketNumber(eventId,chatId.toLong())
                    }

                    eventGuest.get().userConfirmed = true
                    eventGuestRepository.save(eventGuest.get())
                    notificationService.sendTicketNumber(chatId, eventGuest.get().eventName!!, eventGuest.get().ticketNumber!!)
                    return
                }

            } catch (e: TelegramApiException) {
                e.printStackTrace()
            }
        }
    }

    fun generateTicketNumber(eventId: Long, userId: Long): String {
        val randomComponent = (1000..9999).random() // Random 4-digit number
        return "TICKET-${eventId}-$userId-${randomComponent}"
    }

}
