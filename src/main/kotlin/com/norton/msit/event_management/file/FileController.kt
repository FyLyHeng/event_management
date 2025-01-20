package com.norton.msit.event_management.file

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.IOException


@RestController
@RequestMapping("/api/v1/file")
class FileController {

    @Autowired
    private lateinit var cloudinaryService: CloudinaryService

    @PostMapping("/upload")
    fun uploadFile(@RequestParam("file") file: MultipartFile?): ResponseEntity<Map<String,String>> {

        try {
            val url = cloudinaryService.uploadFile(file!!)
            return ResponseEntity(mapOf("url" to url), HttpStatus.OK)


        } catch (e: IOException) {
            e.printStackTrace()
            return ResponseEntity(mapOf("error" to "Error uploading file: " + e.message), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}