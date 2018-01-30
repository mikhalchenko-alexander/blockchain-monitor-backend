package com.anahoret.blockchainmonitorbackend.service

import com.anahoret.blockchainmonitorbackend.web.dto.StateDto
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class RestNodeService(val restTemplate: RestTemplate) {

  fun getNodeState(url: String): StateDto {
    return restTemplate.getForObject("$url/management/status", StateDto::class.java)
  }

}
