package com.norton.msit.event_management.agenda


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
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/v1/agenda")
class AgendaController {

    val logger: Logger = LoggerFactory.getLogger(AgendaController::class.java)

    @Autowired
    private lateinit var repository: AgendaRepository

    @GetMapping("/event_id/{eventId}")
    fun listEvent(@PathVariable eventId:Long) : ResponseEntity<List<Agenda>> {
        val agendas = repository.findAll(Sort.by(Sort.Direction.DESC, "id"))
            .filter { it.eventId == eventId }

        logger.info("${agendas.size} agendas found")
        return ResponseEntity.ok().body(agendas)
    }

    @GetMapping("{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<Agenda> {
        return ResponseEntity.ok().body(repository.findById(id).orElseThrow())
    }

    @PutMapping("{id}")
    fun update(@PathVariable id: Long, @RequestBody body: Agenda) : ResponseEntity<Map<String,String>> {

        repository.save(body)
        return ResponseEntity.ok().body(mapOf("message" to "success"))
    }

    @PostMapping
    fun save(@RequestBody body: Agenda) : ResponseEntity<Map<String,String>> {
        repository.save(body)
        return ResponseEntity.ok().body(mapOf("message" to "success"))
    }
}