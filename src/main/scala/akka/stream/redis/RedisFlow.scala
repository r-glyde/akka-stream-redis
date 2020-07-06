package akka.stream.redis

import akka.stream.redis.commands.{hash, string}

object RedisFlow extends string.RedisFlow with hash.RedisFlow
