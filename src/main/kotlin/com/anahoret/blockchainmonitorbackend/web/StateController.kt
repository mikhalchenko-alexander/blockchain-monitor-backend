package com.anahoret.blockchainmonitorbackend.web

import com.anahoret.blockchainmonitorbackend.service.RestNodeService
import com.anahoret.blockchainmonitorbackend.service.StateService
import com.anahoret.blockchainmonitorbackend.web.dto.*
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@CrossOrigin(maxAge = 3600)
@RestController
class StateController(val stateService: StateService, val restNodeService: RestNodeService) {

  private val restTemplate = RestTemplate()

  @GetMapping("initial")
  fun initial(): InitialStateDto {
    val states = stateService.getStates()
    val nodes = states.values.map { NodeDto(it.id, it.name, it.last_hash, it.url) }
    val links = states.values.flatMap { it.links }.distinct()
    return InitialStateDto(nodes, links.map { listOf(it.id1, it.id2) })
  }

  @GetMapping("nodes_updates")
  fun updates(): UpdateStateDto {
    val addedNodes = stateService.getAddedNodes()
    val states = stateService.getStates()

    val addedLinks = mutableListOf<List<Int>>()
    val removedNodes = mutableListOf<Int>()
    val removedLinks = mutableListOf<NodeLinkDto>()

    states.values.forEach { currentState ->
      try {
        val newState = restNodeService.getNodeState(currentState.url)
        removedLinks.addAll(currentState.links.filterNot { newState.links.contains(it) })
        addedLinks.addAll(newState.links.filterNot { currentState.links.contains(it) }.map { listOf(it.id1, it.id2) })
        stateService.updateState(currentState.id, newState)
      } catch (t: Throwable) {
        println("Node ${currentState.id} not responding. ${t.message}")
//        stateService.removeNode(currentState.id)
//        removedNodes.add(currentState.id)
      }
    }

    return UpdateStateDto(
      AddedNodesDto(addedNodes, addedLinks),
      RemovedNodesDto(removedNodes, removedLinks)
    )
  }

}
