package com.kingchampion36.key.value.store.kafka

import com.kingchampion36.key.value.store.config.KafkaConfig
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.stereotype.Component

@Component
class KeyValueProducer(private val kafkaConfig: KafkaConfig) {

  private val producerConfig = mapOf(
    "bootstrap.servers" to listOf(kafkaConfig.bootstrapServer),
    "key.serializer" to StringSerializer::class.qualifiedName,
    "value.serializer" to StringSerializer::class.qualifiedName
  )

  private val kafkaProducer = KafkaProducer<String, String>(producerConfig)

  fun send(key: String, value: String?) {
    kafkaProducer.send(ProducerRecord(kafkaConfig.topicName, key, value))
  }
}
