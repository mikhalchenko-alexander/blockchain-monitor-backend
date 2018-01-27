package com.anahoret.blockchainmonitorbackend.web

import com.anahoret.blockchainmonitorbackend.service.StateService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@RestController
class StateController(val stateService: StateService) {

  private val restTemplate = RestTemplate()

  @GetMapping("initial")
  fun initial(): InitialStateDto {
    val nodes = stateService.states.values.map { NodeDto(it.id, it.name) }
    val links = stateService.states.values.flatMap { it.links }.distinct()
    return InitialStateDto(nodes, links)
  }

  @GetMapping("get_updates")
  fun updates(): UpdateStateDto {
    stateService.states.keys.forEach { id ->
      val url = stateService.states[id]?.url
      val stateDto = restTemplate.getForObject("http://$url/management/state", StateService.StateDto::class.java)

      stateService.states[id] = stateDto
    }

    return UpdateStateDto()
  }

  data class InitialStateDto(
    var nodes: List<NodeDto> = emptyList(),
    var links: List<NodeLink> = emptyList()
  )

  data class UpdateStateDto(
    var added: AddedNodesDto = AddedNodesDto(),
    var removed: RemovedNodesDto = RemovedNodesDto()
  )

  data class RemovedNodesDto(
    var nodes: List<String> = emptyList(),
    var links: List<NodeLink> = emptyList()
  )

  data class AddedNodesDto(
    var nodes: List<NodeDto> = emptyList(),
    var links: List<NodeLink> = emptyList()
  )

  data class NodeDto(
    var id: String = "",
    var name: String = ""
  )

  data class NodeLink(
    var id1: String = "",
    var id2: String = ""
  ) {

    override fun equals(other: Any?): Boolean {
      val n2 = other as NodeLink
      val l1 = listOf(id1.toInt(), id2.toInt()).sorted()
      val l2 = listOf(n2.id1.toInt(), n2.id2.toInt()).sorted()
      return l1[0] == l2[0] && l1[1] == l2[1]
    }

    override fun hashCode(): Int {
      return listOf(id1.toInt(), id2.toInt()).sorted().hashCode()
    }

  }

}
