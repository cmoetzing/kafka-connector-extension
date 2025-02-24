package com.exasol.cloudetl.kafka

import java.lang.{Integer => JInt}
import java.lang.{Long => JLong}

import com.exasol.ExaDataTypeException
import com.exasol.ExaIterationException
import com.exasol.ExaMetadata

import org.mockito.ArgumentMatchers._
import org.mockito.Mockito._

@SuppressWarnings(
  Array("org.wartremover.warts.AsInstanceOf", "org.wartremover.contrib.warts.SymbolicName")
)
class KafkaTopicMetadataReaderIT extends KafkaIntegrationTest {

  override def additionalProperties: Map[String, String] =
    Map("SCHEMA_REGISTRY_URL" -> KafkaTopicDataImporterAvroIT.schemaRegistryUrl)

  // Default case where Exasol table is empty.
  test("run emits default partitionId maxOffset pairs with single topic partition") {
    val iter = mockExasolIterator(properties, Seq(0), Seq(-1))
    KafkaTopicMetadataReader.run(mock[ExaMetadata], iter)
    verify(iter, times(1)).emit(anyInt().asInstanceOf[JInt], anyLong().asInstanceOf[JLong])
    verify(iter, times(1)).emit(JInt.valueOf(0), JLong.valueOf(-1))
  }

  // Default case where Exasol table is empty.
  test("run emits default partitionId maxOffset pairs with more topic partitions") {
    createCustomTopic(topic, partitions = 3)
    val iter = mockExasolIterator(properties, Seq(0), Seq(-1))
    KafkaTopicMetadataReader.run(mock[ExaMetadata], iter)
    verify(iter, times(3)).emit(anyInt().asInstanceOf[JInt], anyLong().asInstanceOf[JLong])
    Seq(0, 1, 2).foreach { partitionId =>
      verify(iter, times(1)).emit(JInt.valueOf(partitionId), JLong.valueOf(-1))
    }
  }

  test("run emits partitionId maxOffset pairs with additional topic partitions") {
    createCustomTopic(topic, partitions = 3)
    val partitions = Seq(0, 1)
    val offsets = Seq(3L, 4L)
    val iter = mockExasolIterator(properties, partitions, offsets)
    KafkaTopicMetadataReader.run(mock[ExaMetadata], iter)

    verify(iter, times(3)).emit(anyInt().asInstanceOf[JInt], anyLong().asInstanceOf[JLong])
    partitions.zip(offsets).foreach { case (partitionId, maxOffset) =>
      verify(iter, times(1)).emit(JInt.valueOf(partitionId), JLong.valueOf(maxOffset))
    }
    verify(iter, times(1)).emit(JInt.valueOf(2), JLong.valueOf(-1))
  }

  // Do not emit partitionId maxOffset pairs if partitionId is not
  // available in topic partitions
  test("run emits partitionId maxOffset pairs with fewer topic partitions") {
    createCustomTopic(topic, partitions = 2)
    val iter = mockExasolIterator(properties, Seq(1, 3), Seq(7, 17))
    KafkaTopicMetadataReader.run(mock[ExaMetadata], iter)

    verify(iter, times(2)).emit(anyInt().asInstanceOf[JInt], anyLong().asInstanceOf[JLong])
    verify(iter, times(1)).emit(JInt.valueOf(0), JLong.valueOf(-1))
    verify(iter, times(1)).emit(JInt.valueOf(1), JLong.valueOf(7))
  }

  test("run throws if it cannot create KafkaConsumer") {
    createCustomTopic(topic)
    val newProperties = properties + ("BOOTSTRAP_SERVERS" -> "kafka01.internal:9092")
    val iter = mockExasolIterator(newProperties, Seq(0), Seq(-1))
    val thrown = intercept[KafkaConnectorException] {
      KafkaTopicMetadataReader.run(mock[ExaMetadata], iter)
    }
    assert(thrown.getMessage().contains("Could not create a Kafka consumer for topic"))
  }

  test("run catches when emit throws an ExaDataTypeException") {
    val thrown = intercept[KafkaConnectorException] {
      emitThrowsAnException(classOf[ExaDataTypeException])
    }
    assert(thrown.getMessage().contains("Error emitting metadata information for topic"))
  }

  test("run catches when emit throws an ExaIterationException") {
    val thrown = intercept[KafkaConnectorException] {
      emitThrowsAnException(classOf[ExaIterationException])
    }
    assert(thrown.getMessage().contains("Error iterating Exasol metadata iterator for topic"))
  }

  def emitThrowsAnException[T <: Throwable](exception: Class[T]): Unit = {
    createCustomTopic(topic, partitions = 2)
    val iter = mockExasolIterator(properties, Seq(1, 3), Seq(7, 17))
    when(iter.emit(JInt.valueOf(1), JLong.valueOf(7))).thenThrow(exception)
    KafkaTopicMetadataReader.run(mock[ExaMetadata], iter)
  }

}
