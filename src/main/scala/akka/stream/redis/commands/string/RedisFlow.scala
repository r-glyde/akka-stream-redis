package akka.stream.redis.commands.string

import akka.NotUsed
import akka.stream.redis.RedisSettings
import akka.stream.scaladsl.{Flow, Source}
import akka.stream.redis.commands._
import io.lettuce.core.api.StatefulRedisConnection

import scala.compat.java8.FutureConverters._
import scala.jdk.CollectionConverters._

trait RedisFlow {

  def mset[K, V](settings: RedisSettings, connection: StatefulRedisConnection[K, V]): Flow[(K, V), String, NotUsed] =
    batchedPairs[K, V](settings.maxBatch)
      .mapAsync(settings.parallelism)((kvs: Map[K, V]) => connection.async().mset(kvs.asJava).toScala)

  def get[K, V](key: K, settings: RedisSettings, connection: StatefulRedisConnection[K, V]): Flow[K, V, NotUsed] =
    Flow[K]
      .mapAsync(settings.parallelism)(key => connection.async().get(key).toScala)

  def batchedGet[K, V](key: K,
                       settings: RedisSettings,
                       connection: StatefulRedisConnection[K, V]): Flow[K, V, NotUsed] =
    batchedKeys[K](settings.maxBatch)
      .mapAsync(settings.parallelism)(keys => connection.async().mget(keys: _*).toScala)
      .flatMapConcat(kvs => Source(kvs.asScala.map(_.getValue).toList))



}
