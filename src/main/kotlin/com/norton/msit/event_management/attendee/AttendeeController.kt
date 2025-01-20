package com.norton.msit.event_management.attendee


import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/attendee")
class AttendeeController {

    val logger: Logger = LoggerFactory.getLogger(AttendeeController::class.java)

    @Autowired
    private lateinit var repository: AttendeeRepository

    @GetMapping("/list-by-event_id/{eventId}")
    fun listByEvent(@PathVariable eventId:Long) : ResponseEntity<List<Attendee>> {
        val agendas = repository.findAll(Sort.by(Sort.Direction.DESC, "eventId"))
            .filter { it.eventId == eventId }

        logger.info("${agendas.size} attendee found")
        return ResponseEntity.ok().body(agendas)
    }

    @GetMapping("{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<Attendee> {
        return ResponseEntity.ok().body(repository.findById(id).orElseThrow())
    }

    @PutMapping("{id}")
    fun update(@PathVariable id: Long, @RequestBody body: Attendee) : ResponseEntity<Map<String,String>> {

        repository.save(body)
        return ResponseEntity.ok().body(mapOf("message" to "success"))
    }

    @PostMapping
    fun save(@RequestBody body: Attendee) : ResponseEntity<Map<String,String>> {
        repository.save(body)
        return ResponseEntity.ok().body(mapOf("message" to "success"))
    }
}