#  akka-stream-redis

### Todo
* Fill out tests
* Passthrough
* Response/error handling
* Retries?
* Additional APIs/commands

### Example
Read all keys and values stored in a hash and write to a new hash:
```scala
import akka.actor.ActorSystem
import akka.stream.redis.{RedisFlow, RedisSettings, RedisSource}
import io.lettuce.core.RedisClient

object Main extends App {
  implicit val system: ActorSystem = ActorSystem()
  
  val redisClient: RedisClient = RedisClient.create("redis://localhost:6379")
  val settings: RedisSettings  = RedisSettings(maxBatch = 1, parallelism = 1)
  
  RedisSource
    .hgetall("key1", redisClient.connect())
    .via(RedisFlow.hmset("key2", settings, redisClient.connect()))
    .run()
}
```