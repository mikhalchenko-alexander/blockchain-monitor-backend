package com.anahoret.blockchainmonitorbackend.web.dto

import com.anahoret.blockchainmonitorbackend.web.cleanupUrl

class AddNodeDto(
  var id: Int = -1,
  var name: String = ""
) {

  var url: String = ""
    set(value) {
      field = value.cleanupUrl()
    }

  override fun toString(): String {
    return "AddNodeDto(id=$id, name='$name', url='$url')"
  }

}
