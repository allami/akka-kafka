package allami.kafka.streaming

import java.util.Properties

import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsConfig

object Config {

  val config = {
    val properties = new Properties()
    properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "Logs")
    properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass)
    properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass)
    properties.put("serializer.class", "kafka.serializer.DefaultEncoder")
    properties.put(StreamsConfig.STATE_DIR_CONFIG, "/opt/dir")
    properties.put(StreamsConfig.APPLICATION_SERVER_CONFIG, "localhost:7070")

    properties
  }

  val bucket="data"
  val server ="127.0.0.1"
  val password="allami123"


}
