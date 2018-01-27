package com.anahoret.blockchainmonitorbackend.web

import com.anahoret.blockchainmonitorbackend.service.StateService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@CrossOrigin(maxAge = 3600)
@RestController
class StateController(val stateService: StateService) {

  private val restTemplate = RestTemplate()

  @GetMapping("initial")
  fun initial(): InitialStateDto {
    val states = stateService.getStates()
    val nodes = states.values.map { NodeDto(it.id, it.name, it.last_hash, it.url) }
    val links = states.values.flatMap { it.links }.distinct()
    return InitialStateDto(nodes, links.map { listOf(it.id1, it.id2) })
  }

  @GetMapping("get_updates")
  fun updates(): UpdateStateDto {
    val states = stateService.getStates()
    states.keys.forEach { id ->
      val url = states[id]?.url
      val stateDto = restTemplate.getForObject("http://$url/management/status", StateService.StateDto::class.java)

      stateService.updateState(id, stateDto)
    }

    return UpdateStateDto()
  }

  data class InitialStateDto(
    var nodes: List<NodeDto> = emptyList(),
    var links: List<List<Int>> = emptyList()
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
    var id: Int = -1,
    var name: String = "",
    var last_hash: String = "",
    var url: String = ""
  )

  data class NodeLink(
    var id1: Int = -1,
    var id2: Int = -1
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
