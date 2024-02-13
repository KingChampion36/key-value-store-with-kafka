package com.kingchampion36.key.value.store.repository

import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class LocalKeyValueRepository : KeyValueRepository {

  private val cache = ConcurrentHashMap<String, String>()

  override fun save(key: String, value: String?) {
    when (value) {
      null -> cache.remove(key)
      else -> cache[key] = value
    }
  }

  override fun get(key: String) = cache[key]
}
