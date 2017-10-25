name := "AkkaRest"

version := "0.1"

scalaVersion := "2.11.10"



libraryDependencies += "com.typesafe.akka" % "akka-http_2.11" % "10.0.10"  exclude("log4j", "log4j") exclude("org.slf4j","slf4j-log4j12")


libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"

libraryDependencies += "org.apache.kafka" % "kafka-streams" % "0.11.0.0" exclude("log4j", "log4j") exclude("org.slf4j","slf4j-log4j12")
// https://mvnrepository.com/artifact/org.apache.kafka/connect-api
//libraryDependencies += "org.apache.kafka" % "connect-api" % "0.11.0.1"
// https://mvnrepository.com/artifact/org.apache.kafka/kafka_2.11
libraryDependencies += "org.apache.kafka" % "kafka_2.11" % "0.10.1.0" exclude("org.slf4j", "slf4j-simple")


// https://mvnrepository.com/artifact/com.ibm/couchdb-scala_2.11
libraryDependencies += "com.ibm" % "couchdb-scala_2.11" % "0.7.2"  exclude("log4j", "log4j") exclude("org.slf4j","slf4j-log4j12")


// https://mvnrepository.com/artifact/org.apache.httpcomponents/httpclient
libraryDependencies += "org.apache.httpcomponents" % "httpclient" % "4.1.1"  exclude("log4j", "log4j") exclude("org.slf4j","slf4j-log4j12")

// https://mvnrepository.com/artifact/com.google.code.gson/gson
libraryDependencies += "com.google.code.gson" % "gson" % "2.8.2"  exclude("log4j", "log4j") exclude("org.slf4j","slf4j-log4j12")


libraryDependencies +="com.couchbase.client" % "couchbase-client" % "1.4.9" exclude("org.slf4j", "slf4j-simple")


libraryDependencies +="com.couchbase.client" % "java-client" % "2.1.3" exclude("org.slf4j", "slf4j-simple")

libraryDependencies ++= Seq(
  "org.rocksdb"         % "rocksdbjni" % "5.6.1" exclude("org.slf4j", "slf4j-simple"),
  "org.apache.commons"  % "commons-lang3" % "3.6" exclude("org.slf4j", "slf4j-simple"),
  "org.skinny-framework.com.fasterxml.jackson.module" % "jackson-module-scala_2.12" % "2.8.4" exclude("org.slf4j", "slf4j-simple"),
  "com.typesafe.akka"   %% "akka-http-spray-json" % "10.0.9" exclude("org.slf4j", "slf4j-simple"),
  "com.typesafe.akka"   %% "akka-http" % "10.0.9" exclude("org.slf4j", "slf4j-simple")
)



