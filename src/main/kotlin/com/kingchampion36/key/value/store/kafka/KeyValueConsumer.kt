package com.kingchampion36.key.value.store.kafka

import com.kingchampion36.key.value.store.config.KafkaConfig
import com.kingchampion36.key.value.store.repository.KeyValueRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.annotation.PostConstruct
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.concurrent.atomic.AtomicBoolean

@Component
class KeyValueConsumer(
  private val kafkaConfig: KafkaConfig,
  private val keyValueRepository: KeyValueRepository
) {

  private val log = KotlinLogging.logger { }

  private val consumerConfig = mapOf(
    "bootstrap.servers" to listOf(kafkaConfig.bootstrapServer),
    "key.deserializer" to StringDeserializer::class.qualifiedName,
    "value.deserializer" to StringDeserializer::class.qualifiedName,
    "auto.offset.reset" to "earliest"
  )

  private val kafkaConsumer = KafkaConsumer<String, String>(consumerConfig)

  private val running = AtomicBoolean(false)

  @PostConstruct
  fun initializeCache() {
    // Assign the partition to the consumer
    val topicPartition = TopicPartition(kafkaConfig.topicName, 0)
    kafkaConsumer.assign(listOf(topicPartition))

    // Seek to the end of the partition to get the last offset
    kafkaConsumer.seekToEnd(listOf(topicPartition))
    val lastOffset = kafkaConsumer.position(topicPartition)
    log.info { "Last offset is $lastOffset" }

    // Seek back to the beginning of the partition
    kafkaConsumer.seekToBeginning(listOf(topicPartition))

    // Start consuming messages until the last offset
    while (lastOffset > kafkaConsumer.position(topicPartition)) {
      kafkaConsumer.poll(Duration.ofMillis(100)).forEach {
        it.saveInCache()
      }
    }
    log.info { "Cache has been initialized successfully" }
  }

  @Async
  @EventListener(ApplicationReadyEvent::class)
  fun consumeMessages() {
    running.set(true)
    while (running.get()) {
      kafkaConsumer.poll(Duration.ofMillis(1000)).forEach {
        it.saveInCache()
      }
    }
  }

  private fun ConsumerRecord<String, String>.saveInCache() {
    keyValueRepository.save(key = key(), value = value())
  }
}
