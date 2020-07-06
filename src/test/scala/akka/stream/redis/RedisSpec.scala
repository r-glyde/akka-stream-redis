package akka.stream.redis

import akka.stream.scaladsl.Source
import base.RedisSpecBase

import scala.jdk.CollectionConverters._

class RedisSpec extends RedisSpecBase {

  val keys  = (1 to 10).map(_.toString)
  val pairs = keys.map(i => i -> s"value-$i").toList

  "Redis Connector" should {
    "publish key:value pairs to redis" in {
      Source(pairs)
        .via(RedisFlow.mset(settings, redisClient.connect()))
        .run()
        .map { _ =>
          getValues(keys: _*) should contain allElementsOf pairs.map(_._2)
        }
    }

    "consume and publish key:value pairs" in {
      val key1 = "key1"
      val key2 = "key2"

      redisClient.connect().sync().hmset(key1, pairs.toMap.asJava)

      RedisSource
        .hgetall(key1, redisClient.connect())
        .via(RedisFlow.hmset(key2, settings, redisClient.connect()))
        .run()
        .map { _ =>
          getAllValues(key2) should contain allElementsOf pairs
        }
    }

  }
}
