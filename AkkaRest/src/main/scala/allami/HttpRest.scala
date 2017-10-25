package allami

import com.ibm.couchdb._
import org.slf4j.LoggerFactory

import scalaz._
import scalaz.concurrent.Task
import java.net.URI

import com.couchbase.client._
import scala.collection._
import scala.collection.JavaConverters._

import scala.compat.Platform

object HttpRest extends App {


  def add_to_couchdb(bucket:String,server :String,password:String,data:String,key:String)={

    val bucketName = bucket
    //"allami123"
    val nodesList =Seq(server)// Seq("127.0.0.1")
      .map(ip => URI.create(s"http://$ip:8091"))
      .toBuffer.asJava
    val client = new CouchbaseClient(nodesList, bucketName, password)

    val startTime = Platform.currentTime
    val keyc = s"$key-$startTime"
    client.set(keyc, data).get()
  }
}