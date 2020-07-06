package akka.stream.redis.commands.hash

import akka.NotUsed
import akka.stream.scaladsl.Source
import io.lettuce.core.api.StatefulRedisConnection

import scala.jdk.CollectionConverters._

trait RedisSource {

  def hgetall[K, V](key: K, connection: StatefulRedisConnection[K, V]): Source[(K, V), NotUsed] =
    Source
      .fromPublisher(connection.reactive().hgetall(key))
      .flatMapConcat(map => Source(map.asScala.toList))

  def hmget[K, V](key: K, keys: Seq[K], connection: StatefulRedisConnection[K, V]): Source[(K, V), NotUsed] =
    Source
      .fromPublisher(connection.reactive().hmget(key, keys: _*))
      .map(kv => kv.getKey -> kv.getValue)

}
