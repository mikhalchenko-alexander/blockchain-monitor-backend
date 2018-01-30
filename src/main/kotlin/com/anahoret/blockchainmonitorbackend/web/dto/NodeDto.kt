package com.anahoret.blockchainmonitorbackend.web.dto

import com.anahoret.blockchainmonitorbackend.web.cleanupUrl

class NodeDto(
  var id: Int = -1,
  var name: String = "",
  var last_hash: String = "",
  url: String
) {

  var url: String = url
    set(value) {
      field = value.cleanupUrl()
    }

  override fun toString(): String {
    return "NodeDto(id=$id, name='$name', last_hash='$last_hash', url='$url')"
  }

}
