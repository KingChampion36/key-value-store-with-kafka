package com.kingchampion36.key.value.store.repository

interface KeyValueRepository {
  fun save(key: String, value: String?)
  fun get(key: String): String?
}
