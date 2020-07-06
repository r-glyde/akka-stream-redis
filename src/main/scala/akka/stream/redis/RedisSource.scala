package akka.stream.redis

import akka.stream.redis.commands.{hash, string}


object RedisSource extends string.RedisSource with hash.RedisSource
