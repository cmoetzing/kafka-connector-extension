package com.exasol.cloudetl.kafka.deserialization

import java.lang.{Long => JLong}
import java.lang.{Integer => JInteger}
import java.lang.{String => JString}

import org.scalatest.funsuite.AnyFunSuite

class FieldConverterTest extends AnyFunSuite {

  test("must convert string when target column type is Long") {
    val converter = new FieldConverter(Seq(classOf[JLong]))
    assert(converter.convert(classOf[JLong], "123") === JLong.valueOf(123L))
  }

  test("must convert string when target column type is Integer") {
    val converter = new FieldConverter(Seq(classOf[JInteger]))
    assert(converter.convert(classOf[JInteger], "1234") === JInteger.valueOf(1234))
  }

  test("must not convert string when type matches") {
    val converter = new FieldConverter(Seq(classOf[JString]))
    assert(converter.convert(classOf[JString], "1234") === new JString("1234"))
  }
}
