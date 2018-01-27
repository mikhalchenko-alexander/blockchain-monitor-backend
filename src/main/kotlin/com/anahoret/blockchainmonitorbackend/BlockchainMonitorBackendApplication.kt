package com.anahoret.blockchainmonitorbackend

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
class BlockchainMonitorBackendApplication

fun main(args: Array<String>) {
    SpringApplication.run(BlockchainMonitorBackendApplication::class.java, *args)
}
