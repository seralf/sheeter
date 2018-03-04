package check

import scala.io.Source
import java.io.File
import it.seralf.tabold.csv.CSV
import scala.concurrent.Future

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object TestingWithUniqueKeys extends App {

  val artworks_path = "src/main/resources/tate/artwork_data.csv"

  val csv = new CSV(artworks_path)
  csv.start()

  val headers = csv.headers()
  println("#### HEADERS")

  println(s"KEYS: ${csv.keys.mkString(" | ")}")

  val num_rows = csv.size()
  println(s"NUM ROWS = ${num_rows}")

  
  // NOTA: aggiungiamo sempre un ROW_NUM sintetico: se c'è un campo che copre tutte le righe usiamo quello, sennò il ROW_NUM 
  
  // TESTING UNIQUE KEYS..........................
  val futures = csv.headers().par.map { k =>
    Future { (k, csv.table().slice(0, 200).par.map { row => row.filter(_._1._1.equalsIgnoreCase(k._1)) }.par.distinct.size) }
  }.toStream

  val stats = Await.result(Future.sequence(futures), Duration.Inf)
  stats
    .sortBy(_._2)(Ordering.Int.reverse)
    .foreach { p =>
      println(s"${p._1._1}: ${p._2} [${p._1._2.asInstanceOf[Class[_]].getSimpleName}]")
    }

  // TESTING UNIQUE KEYS..........................

  csv.stop()

}