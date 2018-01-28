package com.anahoret.blockchainmonitorbackend.web.dto

data class AddedNodesDto(
  var nodes: List<NodeDto> = emptyList(),
  var links: List<List<Int>> = emptyList()
)
