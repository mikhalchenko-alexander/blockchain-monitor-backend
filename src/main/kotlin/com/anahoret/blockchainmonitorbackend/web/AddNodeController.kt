package com.anahoret.blockchainmonitorbackend.web

import com.anahoret.blockchainmonitorbackend.service.StateService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@RestController
class AddNodeController(val stateService: StateService) {

  private val restTemplate = RestTemplate()

  @PostMapping("add_node")
  fun addNode(@RequestBody request: AddNodeRequest): AddNodeRequest {
    val stateDto = restTemplate.getForObject("http://${request.url}/management/state", StateService.StateDto::class.java)
    stateService.states[request.id] = stateDto

    return request
  }

  data class AddNodeRequest(
    var id: String = "",
    var name: String = "",
    var url: String = ""
  )


}
