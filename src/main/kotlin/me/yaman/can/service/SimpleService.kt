package me.yaman.can.service

import me.yaman.can.model.SimpleModel
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.concurrent.atomic.AtomicInteger

@Service
class SimpleService() {
    private val counter = AtomicInteger()

    fun generate(): SimpleModel {
        return SimpleModel("Simple Service Generator", Instant.now(), getCounter())
    }

    fun incrementAndGet(): Int {
        return counter.incrementAndGet()
    }

    fun getCounter(): Int {
        return counter.get()
    }
}