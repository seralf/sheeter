package it.seralf.tabold

import java.util.HashMap

//import scala.collection.immutable.HashMap

object Table {

  type FIELD_NAME = String
  type FIELD_TYPE = Class[Object]
  type FIELD_VALUE = Object

  class Header extends HashMap[FIELD_NAME, FIELD_TYPE]

  case class Row(row_num: Int, fields: Map[FIELD_NAME, FIELD_VALUE]) {
    override def toString = s"ROW[$row_num] (${fields.map(_._2).mkString(", ")})"
  }

}

class Table(header: Table.Header, rows: Seq[Table.Row]) {}