package akka.stream.redis.commands.string

import akka.NotUsed
import akka.stream.scaladsl.Source
import io.lettuce.core.api.StatefulRedisConnection

trait RedisSource {

  def mget[K, V](keys: Seq[K], connection: StatefulRedisConnection[K, V]): Source[(K, V), NotUsed] =
    Source
      .fromPublisher(connection.reactive().mget(keys: _*))
      .map(kv => kv.getKey -> kv.getValue)

}
