package com.anahoret.blockchainmonitorbackend.web.dto

data class StateDto(
  var id: Int = -1,
  var name: String = "",
  var url: String = "",
  var last_hash: String = "",
  var neighbours: List<Int> = emptyList()
) {
  val links by lazy {
    neighbours.map { NodeLinkDto(id, it) }
  }
}
