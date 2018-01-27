package com.anahoret.blockchainmonitorbackend.service

import com.anahoret.blockchainmonitorbackend.web.StateController
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import java.io.*
import javax.annotation.PostConstruct

@Service
class StateService {

  private val mapper = ObjectMapper()
  private val file = File("store.dat")

  private val states =
    if (file.exists()) {
      mapper.readValue<List<StateDto>>(file.readText(), object: TypeReference<List<StateDto>>() {})
        .map {
          val dto = it as StateDto
          dto.id to dto
        }.toMap().toMutableMap()
    } else {
      HashMap()
    }

  fun updateState(id: Int, stateDto: StateDto) {
    states[id] = stateDto

    file.writeText(mapper.writeValueAsString(states.values.toList()))
  }

  @PostConstruct
  fun init() {

  }

  fun getStates(): Map<Int, StateDto> = states.toMap()

  data class StateDto(
    var id: Int = -1,
    var name: String = "",
    var url: String = "",
    var last_hash: String = "",
    var neighbours: List<Int> = emptyList()
  ) {
    val links by lazy {
      neighbours.map { StateController.NodeLink(id, it) }
    }
  }

}
