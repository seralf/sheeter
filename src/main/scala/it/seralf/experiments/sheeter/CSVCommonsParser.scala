package it.seralf.experiments.sheeter

import java.io.FileReader
import java.io.File
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

import org.apache.commons.csv.CSVFormat
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.databind.ObjectMapper

/**
 * this parser is based on apache commons csv library
 */
class CSVCommonsParser(input_file: String) {

  private val logger = LoggerFactory.getLogger(this.getClass)

  private val defaults = """
  csv_parser {
    remove_empty = true
    generate_id = true
    multiple_json = false
  }
  """

  private val conf = ConfigFactory.parseString(defaults).getConfig("csv_parser")

  private val json_mapper = new ObjectMapper
  json_mapper.registerModule(DefaultScalaModule)

  private val file_in = new File(input_file)
  private var base_uri = file_in.toURI()

  def rows() = {

    val in = new FileReader(file_in)
    val parser = CSVFormat.DEFAULT.withHeader()
    val records = parser.parse(in).toStream
    val size = records.foldLeft(0)((x, y) => x + 1)
    val trail = size.toString.size

    var i = 0
    val result = records.map {
      record =>
        i = i + 1

        val map = record.toMap()
          .map { item => (item._1, item._2.trim()) }
          .filterNot { item => conf.getBoolean("remove_empty") && item._2.trim().equals("") }

        if (conf.getBoolean("generate_id")) {
          val _id = s"%0${trail}d".format(i)
          map.put("_row", _id)
          map.put("_id", s"${base_uri}#${_id}")
        }

        map
    }

    in.close()

    result.toStream
  }

  def json_rows() = {
    val json_writer = json_mapper.writerWithDefaultPrettyPrinter()
    rows
      .map {
        record =>
          json_writer.writeValueAsString(record)
      }
  }

  def save_json(out_folder: String) = {

    val json_writer = json_mapper.writerWithDefaultPrettyPrinter()
    val inline_writer = json_mapper.writer()
    val multiple_json = conf.getBoolean("multiple_json")

    rows
      .foreach {
        item =>
          val _row = item.get("_row").get
          logger.info(s"saving row: s{_row}")

          if (multiple_json) {
            val file_out = s"${out_folder}/${file_in.getName}_${_row}.json"
            FileUtils.save_file(file_out, json_writer.writeValueAsString(item))
          } else {
            val file_out = new File(out_folder, file_in.getName.substring(0, file_in.getName.lastIndexOf(".csv") + 1) + ".json")
            FileUtils.save_file(file_out.getCanonicalPath, inline_writer.writeValueAsString(item) + "\n", true)
          }
      }
  }

}