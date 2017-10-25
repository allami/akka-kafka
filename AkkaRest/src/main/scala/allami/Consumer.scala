package allami

import java.util.concurrent._
import java.util.{Collections, Properties}

import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}


import scala.collection.JavaConversions._
import org.apache.kafka.common.serialization._
import org.apache.kafka.streams._
import org.apache.kafka.streams.kstream.{KStream, KStreamBuilder}
import com.typesafe.scalalogging._
import java.util.regex._

object Consumer  extends App   with LazyLogging{

     val consumer = Config.ConsumerConf.consumer

     consumer.subscribe(Collections.singletonList("data"))

     val records = consumer.poll(1000)
     try {
       for (record <- records) {

         logger.info("timestamp :" + record.timestamp())
         logger.info("timestamp :" + record.key())
         logger.info("timestamp :" + record.value())

         contains(record.value(), "error")
         contains(record.value(), "warn")
         contains(record.value(), "info")
        println(record.value())
       }
     }
     catch {
       case ioe: InterruptedException =>
         println(ioe)

     } finally {

       consumer.close()
     }


 def  contains(data:String, token:String ) ={
   if (data.contains(token)){
     sendData(data,token)
   }
 }

  def sendData(line:String,topic :String)={

    val regex = "(\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}).*".r
    val datetime=line match {
      case regex(date) => Some(date)
      case _ => None
    }
    val timestamp=Producer.getTimestamp(datetime.getOrElse("")).get

    val producer =  Config.ProducerConf.producer
    val  data = new ProducerRecord[String,String](topic,0,timestamp,topic+"+"+timestamp.toString, line)

    producer.send(data)

  }


}
