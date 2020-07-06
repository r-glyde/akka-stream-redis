package akka.stream.redis

import akka.NotUsed
import akka.stream.scaladsl.Flow

package object commands {

  private[commands] def batchedPairs[K, V](maxBatch: Int): Flow[(K, V), Map[K, V], NotUsed] =
    Flow[(K, V)].batch(maxBatch, Map(_))(_ + _)

  private[commands] def batchedKeys[K](maxBatch: Int): Flow[K, Vector[K], NotUsed] =
    Flow[K].batch(maxBatch, Vector(_))(_ :+ _)

}
