package com.akka

import scala.concurrent._
import scala.concurrent.duration._


object Retry {

  /**
    * exponential back off for retry
    */
  def exponentialBackoff(r: Int): Duration = scala.math.pow(2, r).round * 500 milliseconds

  def noIgnore(t: Throwable): Boolean = false


  def retry[T](maxRetry: Int,
               deadline: Option[Deadline] = None,
               backoff: (Int) => Duration = exponentialBackoff,
               ignoreThrowable: Throwable => Boolean = noIgnore)(block: => T)(implicit ctx: ExecutionContext): Future[T] = {

    class TooManyRetriesException extends Exception("too many retries without exception")
    class DeadlineExceededException extends Exception("deadline exceded")

    val p = Promise[T]

    def recursiveRetry(retryCnt: Int, exception: Option[Throwable])(f: () => T): Option[T] = {
      if (maxRetry == retryCnt
        || deadline.isDefined && deadline.get.isOverdue) {
        exception match {
          case Some(t) =>
            p failure t
          case None if deadline.isDefined && deadline.get.isOverdue =>
            p failure (new DeadlineExceededException)
          case None =>
            p failure (new TooManyRetriesException)
        }
        None
      } else {
        val success = try {
          val rez = if (deadline.isDefined) {
            Await.result(future(f()), deadline.get.timeLeft)
          } else {
            f()
          }
          Some(rez)
        } catch {
          case t: Throwable if !ignoreThrowable(t) =>
            blocking {
              val interval = backoff(retryCnt).toMillis
              Thread.sleep(interval)
            }
            recursiveRetry(retryCnt + 1, Some(t))(f)
          case t: Throwable =>
            p failure t
            None
        }
        success match {
          case Some(v) =>
            p success v
            Some(v)
          case None => None
        }
      }
    }

    def doBlock() = block

    Future {
      recursiveRetry(0, None)(doBlock)
    }

    p.future
  }

}