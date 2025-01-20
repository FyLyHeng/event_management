package com.norton.msit.event_management.file

import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile


@Service
class CloudinaryService {

    @Autowired
    private lateinit var cloudinary : Cloudinary


    fun uploadFile(file: MultipartFile): String {

        val uploadResult = cloudinary.uploader().upload(file.bytes, ObjectUtils.emptyMap())
        return uploadResult["url"].toString()
    }
}