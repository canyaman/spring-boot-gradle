package me.yaman.can.model

import java.time.Instant

data class SimpleModel(
    val message: String,
    val time: Instant,
    val counter: Int
)