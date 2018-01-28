package com.anahoret.blockchainmonitorbackend

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate

@SpringBootApplication
class BlockchainMonitorBackendApplication {

  @Bean
  fun restTemplate(): RestTemplate = RestTemplate()

}

fun main(args: Array<String>) {
    SpringApplication.run(BlockchainMonitorBackendApplication::class.java, *args)
}
