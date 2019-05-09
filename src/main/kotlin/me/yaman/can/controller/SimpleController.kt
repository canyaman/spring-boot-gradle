package me.yaman.can.controller

import me.yaman.can.model.SimpleModel
import me.yaman.can.service.SimpleService
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/test")
class SimpleController(val simpleService: SimpleService) {

    val log = LoggerFactory.getLogger(SimpleController::class.java)

    @GetMapping("/hello")
    fun helloWorld(): String {
        return "Hello World"
    }

    @GetMapping("/time")
    fun getTime(): String {
        return "Current time is ${Instant.now()}"
    }

    @GetMapping("/list")
    fun getList(
        @RequestParam(required = false, defaultValue = "10")
        size: Long
    ): List<String> {
        return (1..size).map { it.toString() }
    }

    @GetMapping("/pageable")
    fun getPageable(@PageableDefault pageable: Pageable): List<String> {
        val from = pageable.offset + 1
        val to = pageable.offset + pageable.pageSize
        return (from..to).map { it.toString() }
    }

    @PostMapping("/counter")
    fun increaseCounter(): String {
        return "Post count is ${simpleService.incrementAndGet()}"
    }

    @GetMapping("/model")
    fun model(request: HttpServletRequest): SimpleModel {
        log.info("Request {}", request)
        log.info("Request headers: {}", request.headerNames.toList().map { it + ":" + request.getHeader(it) })
        return SimpleModel("Simple Model", Instant.now(), simpleService.getCounter())
    }

    @GetMapping("/get")
    fun service(): SimpleModel {
        return simpleService.generate()
    }
}