package com.norton.msit.event_management.event


import com.norton.msit.event_management.agenda.AgendaController
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/v1/event")
class EventController {
    val logger: Logger = LoggerFactory.getLogger(EventController::class.java)


    @Autowired
    private lateinit var repository: EventRepository

    @GetMapping("/list")
    fun listEvent() : ResponseEntity<List<Event>> {
        val events = repository.findAll(Sort.by(Sort.Direction.DESC, "id"))
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





}