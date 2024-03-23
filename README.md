# Key Value Store With Kafka

If number of unique keys is in the range of few thousands, log compacted kafka topic can be used as a key-value store or an external cache.

## What is a log compacted kafka topic?

In Kafka, topics are typically structured as an append-only log of records, where each record consists of a key-value pair. However, in a log-compacted topic, Kafka will maintain the log in such a way that only the latest value for each key is retained, discarding older values with the same key when compaction happens. More details about log compacted topic can be found in confluent's official documentation [here](https://docs.confluent.io/kafka/design/log_compaction.html).

## What should be the partition count of log compacted topic to use it as a key-value store?

It is recommended to have a partition count of 1 and each consumer instance reads from the beginning of the topic when the application starts up.

## Run the app locally

### 1. Download confluent platform in local and start the kafka server

Download confluent platform from confluent's official website [here](https://www.confluent.io/get-started/?product=self-managed).

### 2. Go to the directory where Confluent Platform is downloaded

```shell
cd directory/to/confluent/platform
```

### 3. Start confluent services in local

```shell
bin/confluent local services start
```

### 4. Verify that Kafka is running in local

This can be verified by either using cuRL or telnet:

```shell
telnet localhost 9092
```

### 5. Create log compacted kafka topic

```shell
bin/kafka-topics \
  --create \
  --bootstrap-server localhost:9092 \
  --topic key_value_store_topic \
  --config cleanup.policy=compact
```

### 6. Build

Go to the app directory and run this command

```shell
mvn clean install
```

### 7. Run

Go to the app directory and run this command

```shell
mvn spring-boot:run
```

## How this app acts as a key value store?

During the application startup, the kafka consumer starts reading from the beginning of the topic to the end of the topic and saves each record in local cache.
When a tombstone record (record with value = null), the record is removed from the cache. See [KeyValueConsumer](src/main/kotlin/com/kingchampion36/key/value/store/kafka/KeyValueConsumer.kt)
for more details.

The production of new record into the kafka topic is taken care by [KeyValueProducer](src/main/kotlin/com/kingchampion36/key/value/store/kafka/KeyValueProducer.kt).
The produced record is then consumed by [KeyValueConsumer](src/main/kotlin/com/kingchampion36/key/value/store/kafka/KeyValueConsumer.kt) and saved in the local cache.
