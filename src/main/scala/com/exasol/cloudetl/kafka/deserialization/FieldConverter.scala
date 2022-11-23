package com.exasol.cloudetl.kafka.deserialization

import java.lang.{Long => JLong}
import java.lang.{Integer => JInteger}
import java.lang.{String => JString}
import java.sql.Timestamp
import java.time.Instant

/**
 * A class that converts a long value to timestamp if required by the Exasol column output type
 */
class FieldConverter(outputColumnTypes: Seq[Class[_]]) {

  final def convertRow(row: Seq[Any]): Seq[Any] =
    row.zipWithIndex.map(x => convert(outputColumnTypes(x._2), x._1))

  final def convert(columnType: Class[_], value: Any): Any = {
    value match {
      case x: JString => fromString(columnType, x)
      case x: JLong if columnType == classOf[Timestamp] => new Timestamp(x)
      case _ => value
    }
  }

  final def fromString(columnType: Class[_], value: JString): Any = {
    if (columnType == classOf[Timestamp]) {
      new Timestamp(Instant.parse(value).toEpochMilli)
    }
    else if (columnType == classOf[JLong]) {
      JLong.parseLong(value)
    }
    else if (columnType == classOf[JInteger]) {
      JInteger.parseInt(value)
    }
    else {
      value
    }
  }

}
