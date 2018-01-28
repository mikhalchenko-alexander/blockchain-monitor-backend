package com.anahoret.blockchainmonitorbackend.web.dto

data class AddNodeDto(
  var id: Int = -1,
  var name: String = "",
  var url: String = ""
)
