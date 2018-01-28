package com.anahoret.blockchainmonitorbackend.service

import com.anahoret.blockchainmonitorbackend.web.dto.NodeDto
import com.anahoret.blockchainmonitorbackend.web.dto.StateDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import java.io.*
import javax.annotation.PostConstruct

@Service
class StateService {

  private val mapper = ObjectMapper()
  private val file = File("store.dat")
  private val addedNodes = mutableListOf<NodeDto>()

  private lateinit var states: MutableMap<Int, StateDto>

  fun updateState(id: Int, stateDto: StateDto) {
    synchronized(addedNodes) {
      if (!states.containsKey(id)) {
        addedNodes.add(NodeDto(stateDto.id, stateDto.name, stateDto.last_hash, stateDto.url))
      }
    }
    states[id] = stateDto
    saveToFile()
  }

  fun removeNode(id: Int) {
//    states.remove(id)
//    saveToFile()
  }

  fun getStates(): Map<Int, StateDto> = states.toMap()

  private fun saveToFile() {
    file.writeText(mapper.writeValueAsString(states.values.toList()))
  }

  @PostConstruct
  fun init() {
    states = if (file.exists()) {
      mapper.readValue<Array<StateDto>>(file, Array<StateDto>::class.java)
        .associateBy(StateDto::id)
        .toMutableMap()
    } else {
      HashMap()
    }
  }

  fun getAddedNodes(): List<NodeDto> {
    synchronized(addedNodes) {
      val result = addedNodes.toList()
      addedNodes.clear()
      return result
    }
  }

}
