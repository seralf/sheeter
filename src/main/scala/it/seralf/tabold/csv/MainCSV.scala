package it.seralf.tabold.csv

object MainCSV extends App {

  val artworks_path = "src/main/resources/tate/artwork_data.csv"
  val artists_path = "src/main/resources/tate/artist_data.csv"

  //  val csv = new CSV(artists_path)
  val csv = new CSV(artworks_path)
  csv.start()

  val headers = csv.headers()
  println("#### HEADERS")
  println(headers.mkString(" | "))

  println("#### ROWS")
  csv.rows()
    //    .slice(11, 14)
    .foreach {
      row =>
        println(row.size, row)
    }

  csv.stop()

}