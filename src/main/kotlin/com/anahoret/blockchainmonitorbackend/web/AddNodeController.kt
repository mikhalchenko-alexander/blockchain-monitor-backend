package com.anahoret.blockchainmonitorbackend.web

import com.anahoret.blockchainmonitorbackend.service.RestNodeService
import com.anahoret.blockchainmonitorbackend.service.StateService
import com.anahoret.blockchainmonitorbackend.web.dto.AddNodeDto
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@RestController
class AddNodeController(val stateService: StateService, val restNodeService: RestNodeService) {

  private val restTemplate = RestTemplate()

  @PostMapping("add_node")
  fun addNode(@RequestBody addNodeDto: AddNodeDto): AddNodeDto {
    val stateDto = restNodeService.getNodeState(addNodeDto.url)
    stateService.updateState(addNodeDto.id, stateDto)
    return addNodeDto
  }

}
