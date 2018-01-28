package com.anahoret.blockchainmonitorbackend.web.dto

data class UpdateStateDto(
  var added: AddedNodesDto = AddedNodesDto(),
  var removed: RemovedNodesDto = RemovedNodesDto()
)
