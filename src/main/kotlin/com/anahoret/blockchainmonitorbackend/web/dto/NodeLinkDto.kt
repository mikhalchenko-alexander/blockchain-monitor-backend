package com.anahoret.blockchainmonitorbackend.web.dto

import java.lang.Math.max
import java.lang.Math.min

data class NodeLinkDto(
  var id1: Int = -1,
  var id2: Int = -1
) {

  override fun equals(other: Any?): Boolean {
    return when (other) {
      is NodeLinkDto ->
        min(id1, id2) == min(other.id1, other.id2) &&
        max(id1, id2) == max(other.id1, other.id2)

      else -> false
    }
  }

  override fun hashCode(): Int {
    return min(id1, id2).hashCode() + max(id1, id2).hashCode()
  }

}
