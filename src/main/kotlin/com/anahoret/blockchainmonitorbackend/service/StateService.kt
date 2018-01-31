package com.anahoret.blockchainmonitorbackend.service

import com.anahoret.blockchainmonitorbackend.web.dto.NodeDto
import com.anahoret.blockchainmonitorbackend.web.dto.StateDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class StateService(val redisTemplate: StringRedisTemplate) {

  companion object {
    const val NODES_STATE_KEY = "nodes.state"
  }
  
  private val mapper = ObjectMapper()
  private val addedNodes = mutableListOf<NodeDto>()

  private lateinit var states: MutableMap<Int, StateDto>

  fun updateState(id: Int, stateDto: StateDto) {
    synchronized(addedNodes) {
      if (!states.containsKey(id)) {
        addedNodes.add(NodeDto(stateDto.id, stateDto.name, stateDto.last_hash, stateDto.url))
      }
    }
    states[id] = stateDto
    persist()
  }

  fun removeNode(id: Int) {
//    states.remove(id)
//    persist()
  }

  fun getStates(): Map<Int, StateDto> = states.toMap()

  private fun persist() {
    redisTemplate.opsForValue().set(NODES_STATE_KEY, mapper.writeValueAsString(states.values.toList()))
  }

  @PostConstruct
  fun init() {
    states = if (redisTemplate.hasKey(NODES_STATE_KEY)) {
      val stateStr = redisTemplate.opsForValue().get(NODES_STATE_KEY)
      mapper.readValue<Array<StateDto>>(stateStr, Array<StateDto>::class.java)
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
