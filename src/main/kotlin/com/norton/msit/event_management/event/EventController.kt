package com.norton.msit.event_management.event

import com.norton.msit.event_management.attendee.AttendanceStatus
import com.norton.msit.event_management.attendee.EventAttendee
import com.norton.msit.event_management.attendee.AttendeeRepository
import com.norton.msit.event_management.auth.NotFoundException
import com.norton.msit.event_management.auth.UserRepository
import com.norton.msit.event_management.telegram.NotificationService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RestController
@RequestMapping("/api/v1/event")
class EventController {
    val logger: Logger = LoggerFactory.getLogger(EventController::class.java)


    @Autowired
    private lateinit var repository: EventRepository
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var attendeeRepository: AttendeeRepository
    @Autowired
    private lateinit var notificationService: NotificationService




    @GetMapping("/list")
    fun listEventForGuest() : ResponseEntity<List<Event>> {
        val events = repository.findAll(Sort.by(Sort.Direction.DESC, "id"))
        return ResponseEntity.ok().body(events)
    }

    @GetMapping("/list-by-owner/{ownerId}")
    fun listEventByOwnerId(@PathVariable ownerId: Long) : ResponseEntity<List<Event>> {
        val events = repository.findAll(Sort.by(Sort.Direction.DESC, "id"))
            .filter { it.createdBy == ownerId }
        return ResponseEntity.ok().body(events)
    }


    @GetMapping("{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<Event> {
        logger.info("Event: $id")
        return ResponseEntity.ok().body(repository.findById(id).orElseThrow())
    }


    @PostMapping
    fun save(@RequestBody body: Event) : ResponseEntity<Map<String,String>> {
        repository.save(body)
        return ResponseEntity.ok().body(mapOf("message" to "success"))
    }


    @PostMapping("/register")
    fun guestRegisterToEvent(@RequestBody body: Map<String, String>) : ResponseEntity<Any> {

        val event = repository.findFirstById(body["eventId"]!!.toLong()).orElseThrow()
        val guest = userRepository.findFirstById(body["guestId"]!!.toLong()).orElseThrow()

        val eventAttendee = EventAttendee(
            eventId = event.id,
            eventName = event.title,
            eventDate = event.eventDate,
            eventVenueName = event.eventVenueName,
            userId = guest.id,
            userFullName = guest.firstName + " " + guest.lastName,
            userPhone = guest.phone,
            userTelegramId = guest.telegramId,
            registerDate = LocalDate.now(),
            userConfirmed = false
        )

        attendeeRepository.save(eventAttendee)

        notificationService.setRegisterConfirmation(
            guest.telegramId!!,
            event.id!!,
            event.title!!,
            event.eventDate!!.format(DateTimeFormatter.ofPattern("MMMM d, yyyy")),
            event.eventVenueName!!
        )
        return ResponseEntity.ok().body(mapOf("message" to "success"))
    }


    @GetMapping("/list-my-joined-event/{userId}")
    fun listMyJoinedEvent(@PathVariable userId : Long) : ResponseEntity<Any> {
        val events = attendeeRepository.findAllByUserId(userId)
        return ResponseEntity.ok().body(events)
    }

    @PostMapping("/confirm-joined")
    fun confirmGuestJoined(@RequestBody body : Map<String,String>) : ResponseEntity<Any> {

        val event = repository.findFirstById(body["eventId"]!!.toLong()).orElseThrow()
        val guest = userRepository.findFirstById(body["guestId"]!!.toLong()).orElseThrow()


        val data = attendeeRepository.findFirstByEventIdAndUserId(event.id!!, guest.id!!).orElseThrow {
            throw NotFoundException("Attendee Not Found")
        }

        data.userConfirmed = true
        data.attendanceStatus = AttendanceStatus.Joined.toString()
        attendeeRepository.save(data)

        return ResponseEntity.ok().body(mapOf("message" to "success"))

    }

    @PostMapping("/reminder-all")
    fun eventOwnerPushReminderAll(@RequestBody body: Map<String, String>) : ResponseEntity<Any> {

        val event = attendeeRepository.findAllByEventId(body["eventId"]!!.toLong())

        event.forEach {
            notificationService.sendEventReminder(it.userTelegramId!!, it)
        }

        return ResponseEntity.ok().body(mapOf("message" to "success"))
    }

    @PostMapping("/reminder-guest")
    fun eventOwnerReminderToGuest(@RequestBody body: Map<String, String>) : ResponseEntity<Any> {

        val guest = userRepository.findFirstById(body["guestId"]!!.toLong()).orElseThrow()
        val event = attendeeRepository.findFirstByEventIdAndUserTelegramId(body["eventId"]!!.toLong(), guest.telegramId!!).orElseThrow()
        notificationService.sendEventReminder(event.userTelegramId!!, event)

        return ResponseEntity.ok().body(mapOf("message" to "success"))
    }

}