package com.bookstore.repos

import cats.effect.MonadCancelThrow
import doobie.util.transactor.Transactor
import org.typelevel.log4cats.Logger

import com.bookstore.Environment.DBContext

case class Repositories[F[_]](
    books: BookstoreRepository[F]
  )

object Repositories {
  def make[F[_]: MonadCancelThrow: Logger](
      implicit
      ctx: DBContext,
      xa: Transactor[F],
    ): Repositories[F] =
    Repositories[F](
      books = BookstoreRepository.make[F]
    )
}
