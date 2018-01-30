package com.anahoret.blockchainmonitorbackend.web.dto

import com.anahoret.blockchainmonitorbackend.web.cleanupUrl

data class StateDto(
  var id: Int = -1,
  var name: String = "",
  var last_hash: String = "",
  var neighbours: List<Int> = emptyList()
) {

  var url: String = ""
    set(value) {
      field = value.cleanupUrl()
    }

  val links by lazy {
    neighbours.map { NodeLinkDto(id, it) }
  }

  override fun toString(): String {
    return "StateDto(id=$id, name='$name', last_hash='$last_hash', neighbours=$neighbours, url='$url')"
  }

}
