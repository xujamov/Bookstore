package com.bookstore.repos

import cats.effect.kernel.MonadCancelThrow
import cats.implicits.toFlatMapOps
import cats.implicits.toFunctorOps
import doobie.implicits._
import doobie.util.transactor.Transactor
import org.typelevel.log4cats.Logger

import com.bookstore.Environment.DBContext
import com.bookstore.domain.Book

trait BookstoreRepository[F[_]] {
  def createBooks(books: List[Book]): F[Unit]
  def createBook(books: Book): F[Unit]
  def fetchAll: F[List[Book]]
}

object BookstoreRepository {
  def make[F[_]: MonadCancelThrow: Logger](
      implicit
      ctx: DBContext,
      xa: Transactor[F],
    ): BookstoreRepository[F] =
    new BookstoreRepository[F] {
      import ctx._

      override def fetchAll: F[List[Book]] =
        run(query[Book]).transact(xa)

      override def createBooks(books: List[Book]): F[Unit] =
        run(
          liftQuery(books)
            .foreach { book =>
              query[Book]
                .insertValue(book)
                .onConflictUpdate(_.title)(
                  _.authorId -> _.authorId
                )
            }
        ).transact(xa).void

      override def createBook(book: Book): F[Unit] =
        run {
          query[Book]
            .insertValue(lift(book))
            .onConflictIgnore
        }
          .transact(xa)
          .void
    }
}
