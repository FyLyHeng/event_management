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

            val chatId = update.message.chatId.toString()
/*            val message = SendMessage(chatId,
                """
        👋 Welcome to Event Management System Bot!
        
        Here's what I can do for you:
        ✅ sign up by web [event.mgt.singup.com](${registerUrl})
        ✅ Use Your ID : $chatId for fill in Sign-Up form.
        
        ✅ sign up quickly by clicking the button below.
            """.trimIndent()
            )

            val button = InlineKeyboardButton()
            button.text = "Click here to sign up"
            button.url = registerUrl

            val keyboard = InlineKeyboardMarkup()
            keyboard.keyboard = listOf(listOf(button))
            message.replyMarkup = keyboard

            message.parseMode = ParseMode.MARKDOWN*/

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

        if (update.hasMessage() && update.message.text == "/id") {
            val chatId = update.message.chatId.toString()
            val message = SendMessage(chatId, "Your ID is : $chatId")
            myTelegramBot.execute(message)
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
                    }

                    else {
                        myTelegramBot.execute(SendMessage(chatId, "The User ID Not Match. Please Try Again."))
                    }
                }

                if (callbackData.startsWith("confirm_event_register:")) {

                    val eventId = callbackData.split(":")[1].toLong() ?: 0
                    val user = userRepository.findFirstByTelegramId(chatId)
                    if (user.isEmpty) {
                        myTelegramBot.execute(SendMessage(chatId, "The User ID Not Match. Please Try Again."))
                    }

                    val eventGuest = eventGuestRepository.findFirstByEventIdAndUserTelegramId(eventId, chatId)
                    if (eventGuest.isEmpty) {
                        myTelegramBot.execute(SendMessage(chatId, "The Event Not Match. Please Try Again."))
                    }

                    val message = SendMessage(chatId, """🎉 **Event Registration Completed** 🎉""")
                    message.parseMode = ParseMode.MARKDOWN
                    myTelegramBot.execute(message)
                }

            } catch (e: TelegramApiException) {
                e.printStackTrace()
            }
        }
    }

}
