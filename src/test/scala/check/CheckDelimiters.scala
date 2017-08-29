package check

object CheckDelimiters extends App {

  val D = '"'
  val S = ','
  val rgx_split = s"""${S}(?=([^${D}]*"[^${D}]*${D})*[^${D}]*$$)"""

  val hs = """
  ﻿id,name,gender,dates,yearOfBirth,yearOfDeath,placeOfBirth,placeOfDeath,url  
  """

  val ex = """
  631,"Adzak, Roy",Male,1927–1987,1927,1987,"Reading, United Kingdom","Paris, France",http://www.tate.org.uk/art/artists/roy-adzak-631
  """


  println("using regex: " + rgx_split)
  
  val hs_test = hs.trim().split(rgx_split).map(_.trim()).toList

  val ex_test = ex.trim().split(rgx_split).map(_.trim()).toList

  println(s"headers size: ${hs_test.size}")
  println(hs_test.mkString(" | "))

  println(s"test size: ${ex_test.size}")
  println(ex_test.mkString(" | "))

}