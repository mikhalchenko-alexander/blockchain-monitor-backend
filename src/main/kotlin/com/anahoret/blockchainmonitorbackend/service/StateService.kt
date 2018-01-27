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
  private val addedNodes = mutableListOf<StateController.NodeDto>()

  private lateinit var states: MutableMap<Int, StateDto>

  fun updateState(id: Int, stateDto: StateDto) {
    synchronized(addedNodes) {
      if (!states.containsKey(id)) {
        addedNodes.add(StateController.NodeDto(stateDto.id, stateDto.name, stateDto.last_hash, stateDto.url))
      }
    }
    states[id] = stateDto
    saveToFile()
  }

  fun removeNode(id: Int) {
    states.remove(id)
    saveToFile()
  }

  fun getStates(): Map<Int, StateDto> = states.toMap()

  private fun saveToFile() {
    file.writeText(mapper.writeValueAsString(states.values.toList()))
  }

  @PostConstruct
  fun init() {
    states = if (file.exists()) {
      mapper.readValue<List<StateDto>>(file.readText(), object: TypeReference<List<StateDto>>() {})
        .map {
          it.id to it
        }.toMap().toMutableMap()
    } else {
      HashMap()
    }
  }

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

  fun getAddedNodes(): List<StateController.NodeDto> {
    synchronized(addedNodes) {
      val result = addedNodes.toList()
      addedNodes.clear()
      return result
    }
  }

}
