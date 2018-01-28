package com.anahoret.blockchainmonitorbackend.web.dto

data class NodeDto(
  var id: Int = -1,
  var name: String = "",
  var last_hash: String = "",
  var url: String = ""
)
