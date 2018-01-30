package com.anahoret.blockchainmonitorbackend.web

import com.anahoret.blockchainmonitorbackend.service.RestNodeService
import com.anahoret.blockchainmonitorbackend.service.StateService
import com.anahoret.blockchainmonitorbackend.web.dto.AddNodeDto
import com.anahoret.blockchainmonitorbackend.web.dto.AddNodeErrorDto
import com.anahoret.blockchainmonitorbackend.web.dto.AddNodeResultDto
import com.anahoret.blockchainmonitorbackend.web.dto.AddNodeSuccessDto
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AddNodeController(val stateService: StateService, val restNodeService: RestNodeService) {

  @PostMapping("add_node")
  fun addNode(@RequestBody addNodeDto: AddNodeDto): AddNodeResultDto {
    return try {
      val stateDto = restNodeService.getNodeState(addNodeDto.url)
      stateService.updateState(addNodeDto.id, stateDto)
      AddNodeSuccessDto()
    } catch (t: Throwable) {
      println(t.message)
      AddNodeErrorDto("Can't get status from ${addNodeDto.url}/management/status")
    }
  }

}
