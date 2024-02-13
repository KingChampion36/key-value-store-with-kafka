package com.kingchampion36.key.value.store.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "kafka")
data class KafkaConfig(
  val bootstrapServer: String,
  val topicName: String
)
