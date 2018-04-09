package io.github.microservice.components.apigateway.web.rest

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class HelloResource {

    @GetMapping("/hello")
    fun hello(): String {
        return "hello"
    }
}
