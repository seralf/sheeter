package it.seralf.tabold.csv

import java.io.File
import org.slf4j.LoggerFactory
import java.nio.file.Paths
import scala.io.Source
import scala.util.Try
import java.net.URI
import scala.io.Codec.string2codec

class CSV(file_name: String, separator: Char = ',', delimiter: Char = '"', validation: Boolean = false, comment: String = "#") {

  private val logger = LoggerFactory.getLogger(this.getClass)

  val D = '"'
  val S = ','
  val rgx_split = s"""${S}(?=([^${D}]*"[^${D}]*${D})*[^${D}]*$$)"""

  val file = Paths.get(file_name).toAbsolutePath().toFile()

  //  val _headers: Map[String, Class[_ <: Object]] = Map.empty

  //  def headers(): Map[String, Class[_ <: Object]] = _headers

  def headers() = {

    val random_line = parse_line(parse_lines(file).toList.toStream(1))
    val _types = guess_types(random_line)

    val first_line = parse_lines(file).toList.toStream.head
    parse_line(first_line)
      .zip(_types)

  }

  def guess_types(line: List[String]) = {

    import scala.util.control.Exception.allCatch

    // check Long or check Double or check URI or String
    def isLong(s: String): Boolean = (allCatch opt s.toLong).isDefined
    def isDouble(s: String): Boolean = (allCatch opt s.toDouble).isDefined
    def isURI(s: String): Boolean =
      s != null && !s.trim().equals("") &&
        s.contains(":") &&
        s.size > 3 &&
        Try { new URI(s) }.isSuccess

    line.map {
      item =>

        //        println()
        //        println(s"${item} is Long? ${isLong(item)}")
        //        println(s"${item} is Double? ${isDouble(item)}")
        //        println(s"${item} is URI? ${isURI(item)}")

        if (isLong(item)) {
          classOf[Long]
        } else if (isDouble(item)) {
          classOf[Double]
        } else if (isURI(item)) {
          classOf[URI]
        } else {
          classOf[String]
        }

    }

  }

  //  def rows(): Stream[Map[String, Object]] = {
  def rows() = {
    parse_lines(file)
      .toStream
      .tail
      .map {
        line =>
          parse_line(line)
      }
      .map {
        row =>
          guess_types(row)

          if (validation)
            assert(row.size == headers.size)
          row
      }
  }

  def start() {
    logger.info(s"#### START")
  }

  def stop() {
    logger.info(s"#### STOP")
  }

  // -----------------------------------------------

  def parse_lines(file: File): List[String] = {
    val src = Source.fromFile(file)("UTF-8")
    val lines = src.getLines()
      .filterNot(_.startsWith(comment))
      .filterNot(_.trim().equals(""))
      .toList
    src.close()
    lines
  }

  def parse_line(line: String) = {
    line.trim().split(rgx_split).map(_.trim()).toList
  }

  // -----------------------------------------------
}

