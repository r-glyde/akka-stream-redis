package akka.stream.redis.commands.hash

import akka.NotUsed
import akka.stream.redis.RedisSettings
import akka.stream.scaladsl.{Flow, Source}
import akka.stream.redis.commands._
import io.lettuce.core.api.StatefulRedisConnection

import scala.compat.java8.FutureConverters._
import scala.jdk.CollectionConverters._

trait RedisFlow {

  def hmset[K, V](key: K,
                  settings: RedisSettings,
                  connection: StatefulRedisConnection[K, V]): Flow[(K, V), String, NotUsed] =
    batchedPairs[K, V](settings.maxBatch)
      .mapAsync(settings.parallelism)((kvs: Map[K, V]) => connection.async().hmset(key, kvs.asJava).toScala)

  def hget[K, V](key: K, settings: RedisSettings, connection: StatefulRedisConnection[K, V]): Flow[K, V, NotUsed] =
    Flow[K]
      .mapAsync(settings.parallelism)(k => connection.async().hget(key, k).toScala)

  def batchedHGet[K, V](key: K,
                       settings: RedisSettings,
                       connection: StatefulRedisConnection[K, V]): Flow[K, V, NotUsed] =
    batchedKeys[K](settings.maxBatch)
      .mapAsync(settings.parallelism)(keys => connection.async().hmget(key, keys: _*).toScala)
      .flatMapConcat(kvs => Source(kvs.asScala.map(_.getValue).toList))

}
