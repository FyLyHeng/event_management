package com.norton.msit.event_management.venue


import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/venue")
class VenueController {

    @Autowired
    private lateinit var repository: VenueRepository

    val logger: Logger = LoggerFactory.getLogger(VenueController::class.java)


    @GetMapping("/list")
    fun listEvent() : ResponseEntity<List<Venue>> {

        val events = repository.findAll(Sort.by(Sort.Direction.DESC, "id"))
        logger.info("get venue list" + events.count())
        return ResponseEntity.ok().body(events)
    }



    @PostMapping
    fun save(@RequestBody body: Venue) : ResponseEntity<Map<String,String>> {
        repository.save(body)
        return ResponseEntity.ok().body(mapOf("message" to "success"))
    }





}