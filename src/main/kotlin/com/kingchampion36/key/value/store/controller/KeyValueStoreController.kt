package com.kingchampion36.key.value.store.controller

import com.kingchampion36.key.value.store.exception.NotFoundException
import com.kingchampion36.key.value.store.kafka.KeyValueProducer
import com.kingchampion36.key.value.store.model.GenericResponse
import com.kingchampion36.key.value.store.model.KeyValuePair
import com.kingchampion36.key.value.store.repository.KeyValueRepository
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class KeyValueStoreController(
  private val keyValueProducer: KeyValueProducer,
  private val keyValueRepository: KeyValueRepository
) {

  @PostMapping("/put")
  fun put(@RequestBody keyValuePair: KeyValuePair): KeyValuePair {
    if (keyValuePair.value == null) {
      throw IllegalArgumentException("Value can't be null")
    }
    keyValueProducer.send(key = keyValuePair.key, value = keyValuePair.value)
    return keyValuePair
  }

  @DeleteMapping("/keys/{key}")
  fun delete(@PathVariable key: String): GenericResponse {
    if (keyValueRepository.get(key) == null) {
      throw NotFoundException("Key = $key not found")
    }
    keyValueProducer.send(key = key, null)
    return GenericResponse(message = "Deletion of key = $key was successful")
  }

  @GetMapping("/keys/{key}")
  fun get(@PathVariable key: String): KeyValuePair {
    if (keyValueRepository.get(key) == null) {
      throw NotFoundException("Key = $key not found")
    }
    return KeyValuePair(key = key, value = keyValueRepository.get(key))
  }
}
