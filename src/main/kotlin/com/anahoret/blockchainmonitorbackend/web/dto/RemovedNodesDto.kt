package com.anahoret.blockchainmonitorbackend.web.dto

data class RemovedNodesDto(
  var nodes: List<Int> = emptyList(),
  var links: List<NodeLinkDto> = emptyList()
)
