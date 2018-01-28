package com.anahoret.blockchainmonitorbackend.web.dto

data class InitialStateDto(
  var nodes: List<NodeDto> = emptyList(),
  var links: List<List<Int>> = emptyList()
)
