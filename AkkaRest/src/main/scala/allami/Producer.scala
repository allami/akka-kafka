package allami

import java.util.{Collections, Date, Properties}

import scala.io.Source
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import java.sql.Timestamp
import java.text.SimpleDateFormat


import scala.util.{Failure, Success, Try}



object Producer extends App {


    val producer = Config.ProducerConf.producer

    try {

      val topic = Config.ProducerConf.topic
      val filename = "/opt/host.log"
      for (line <- Source.fromFile(filename).getLines) {


        val regex = "(\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}).*".r
        val datetime = line match {
          case regex(date) => Some(date)
          case _ => None
        }
        println(datetime.getOrElse(""))
        val timestamp = Producer.getTimestamp(datetime.getOrElse("")).get
        val data = new ProducerRecord[String, String](topic, 0, timestamp, timestamp.toString, line)
        producer.send(data)

      }
    } catch {
      case ioe: InterruptedException =>
        println(ioe)

    } finally {
      producer.close()
    }


  def getTimestamp(s: String) : Option[Long] = s match {
    case "" => None
    case _ => {
      val format = new SimpleDateFormat("yyyy/MM/dd' 'HH:mm:ss")
      Try(format.parse(s).getTime) match {
        case Success(t) => Some(t)
        case Failure(_) => None
      }
    }
  }

}