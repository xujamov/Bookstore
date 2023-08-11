package com.bookstore.api.graphql.schema

import java.time.ZonedDateTime
import java.util.UUID

import scala.concurrent.Future

import cats.effect.kernel.Sync
import cats.effect.std.Dispatcher
import cats.implicits._
import sangria.macros.derive.GraphQLField
import sangria.macros.derive.deriveContextObjectType
import sangria.schema.Context
import sangria.schema.ObjectType

import com.bookstore.api.graphql._
import com.bookstore.domain.Book

class BooksApi[F[_]: Sync](implicit dispatcher: Dispatcher[F]) {
  class Queries {
    @GraphQLField
    def books(
        ctx: Context[Ctx[F], Unit]
      ): Future[List[Book]] =
      dispatcher.unsafeToFuture(ctx.ctx.books.fetchAll)
  }

  class Mutations {
    @GraphQLField
    def create(
        ctx: Context[Ctx[F], Unit],
        mediaType: String,
        title: String,
        authorId: Int,
      ): Future[String] = {
      val createTask = for {
        id <- Sync[F].delay(UUID.randomUUID())
        now <- Sync[F].delay(ZonedDateTime.now)
        book = Book(id, title, mediaType, authorId, now)
        _ <- ctx.ctx.books.createBook(book)
      } yield "Ok"
      dispatcher.unsafeToFuture(createTask)
    }
  }
  def queryType: ObjectType[Ctx[F], Unit] =
    deriveContextObjectType[Ctx[F], Queries, Unit](_ => new Queries)
  def mutationType: ObjectType[Ctx[F], Unit] =
    deriveContextObjectType[Ctx[F], Mutations, Unit](_ => new Mutations)
}
