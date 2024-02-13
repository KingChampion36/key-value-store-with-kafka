package com.kingchampion36.key.value.store

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties
@ConfigurationPropertiesScan
class KeyValueStoreApp

fun main(args: Array<String>) {
  runApplication<KeyValueStoreApp>(*args)
}
