package org.app.library.clients

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(
   value =  "STUDENTS"
)
interface FeignClient {

    @GetMapping("api/students")
    fun getStudents()

}