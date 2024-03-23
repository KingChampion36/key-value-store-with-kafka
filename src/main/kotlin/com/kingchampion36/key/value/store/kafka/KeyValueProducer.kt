package com.kingchampion36.key.value.store.kafka

import com.kingchampion36.key.value.store.config.KafkaConfig
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG
import org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG
import org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.stereotype.Component

@Component
class KeyValueProducer(private val kafkaConfig: KafkaConfig) {

  private val producerConfig = mapOf(
    BOOTSTRAP_SERVERS_CONFIG to listOf(kafkaConfig.bootstrapServer),
    KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.qualifiedName,
    VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.qualifiedName
  )

  private val kafkaProducer = KafkaProducer<String, String>(producerConfig)

  fun send(key: String, value: String?) {
    kafkaProducer.send(ProducerRecord(kafkaConfig.topicName, key, value))
  }
}
