package it.seralf.experiments.sheeter

import java.io.FileReader
import org.apache.commons.csv.CSVFormat
import scala.collection.JavaConversions._
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import java.io.File
import com.typesafe.config.ConfigFactory
import com.typesafe.config.Config
import java.io.FileOutputStream
import org.slf4j.LoggerFactory

object MainCSV extends App {

  
//  val file_in = new File(args { 0 })

  val file_in =  new File("src/main/resources/tate/artwork_data.csv")
  
  val csv = new CSVCommonsParser(file_in.getCanonicalPath)

//  csv.save_json(args{1})
  
  csv.save_json("target/exported.json")

}


