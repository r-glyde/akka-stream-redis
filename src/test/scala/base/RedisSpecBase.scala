package base

import akka.actor.ActorSystem
import akka.stream.redis.RedisSettings
import io.lettuce.core.RedisClient
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}

import scala.jdk.CollectionConverters._

trait RedisSpecBase extends AsyncWordSpec with Matchers with BeforeAndAfterAll with BeforeAndAfterEach {

  implicit val system: ActorSystem = ActorSystem()

  val redisClient: RedisClient = RedisClient.create("redis://localhost:6379")
  val settings: RedisSettings  = RedisSettings(1, 1)

  override def afterAll(): Unit = redisClient.shutdown()

  override def afterEach(): Unit = redisClient.connect().sync().flushall()

  def getValues(keys: String*): List[String] =
    redisClient.connect().sync().mget(keys: _*).asScala.map(_.getValue).toList

  def getAllValues(key: String): List[(String, String)] =
    redisClient.connect().sync().hgetall(key).asScala.toList

}
