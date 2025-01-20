package com.norton.msit.event_management.config

import com.cloudinary.Cloudinary
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class CloudinaryConfig {
    @Bean
    fun cloudinary(): Cloudinary {
        //CLOUDINARY_URL=cloudinary://496347532959363:79Gs4HVb2c30hzg5Su4R3wUjKJk@dy4hdrdvb
        return Cloudinary(
            mapOf(
                "cloud_name" to "dy4hdrdvb",
                "api_key" to "496347532959363",
                "api_secret" to "79Gs4HVb2c30hzg5Su4R3wUjKJk"
            )
        )
    }
}