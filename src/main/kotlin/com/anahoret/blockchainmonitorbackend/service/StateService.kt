package com.anahoret.blockchainmonitorbackend.service

import com.anahoret.blockchainmonitorbackend.web.StateController
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.stereotype.Service

@Service
class StateService {

  val states = mutableMapOf<String, StateDto>()

  data class StateDto(
    var id: String = "",
    var name: String = "",
    var url: String = "",
    @JsonProperty("last_hash") var lastHash: String = "",
    var neighbours: List<String> = emptyList()
  ) {
    val links by lazy {
      neighbours.map { StateController.NodeLink(id, it) }
    }
  }

}
