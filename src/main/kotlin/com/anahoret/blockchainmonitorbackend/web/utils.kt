package com.anahoret.blockchainmonitorbackend.web

fun String.cleanupUrl(): String {
  return when {
    startsWith("http://") || startsWith("https://") -> this
    startsWith("/") -> "http:/$this"
    else -> "http://$this"
  }
}
