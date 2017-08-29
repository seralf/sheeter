package it.seralf.tabold

import scala.collection.mutable.HashMap
import java.io.InputStream
import java.io.FileInputStream
import scala.util.Success
import scala.util.Failure
import scala.util.Try
import scala.io.Source
import java.net.URI
import java.io.ByteArrayInputStream
import scala.util.Random
import scala.concurrent.Future
import java.lang.Long
import java.lang.Double

object MainCSVParser extends App {

  val artworks_path = "src/main/resources/tate/artwork_data.csv"

  val sampled = CSVParser.sample(new FileInputStream(artworks_path), 42, 77).get
  CSVParser.parse(sampled)

}