package com.norton.msit.event_management.auth


import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/user")
class UserController {

    val logger: Logger = LoggerFactory.getLogger(UserController::class.java)

    @Autowired
    private lateinit var repository: UserRepository


    @GetMapping("/list")
    fun listAll() : ResponseEntity<List<User>> {
        val user = repository.findAll(Sort.by(Sort.Direction.DESC, "id"))
        logger.info("${user.size} user found")
        return ResponseEntity.ok().body(user)
    }

    @GetMapping("list-role")
    fun listRoles() : ResponseEntity<List<RoleName>> {
        val roles = RoleName.entries
        logger.info("${roles.size} roles found")
        return ResponseEntity.ok().body(roles)
    }

    @GetMapping("{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<User> {
        return ResponseEntity.ok().body(repository.findById(id).orElseThrow())
    }


    @PutMapping("{id}")
    fun update(@PathVariable id: Long, @RequestBody body: User) : ResponseEntity<Map<String,String>> {
        repository.save(body)
        return ResponseEntity.ok().body(mapOf("message" to "success"))
    }

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: Long) : ResponseEntity<Map<String,String>> {
        repository.deleteById(id)
        return ResponseEntity.ok().body(mapOf("message" to "success"))
    }

    @PostMapping
    fun save(@RequestBody body: User) : ResponseEntity<Map<String,String>> {
        repository.save(body)
        return ResponseEntity.ok().body(mapOf("message" to "success"))
    }
}