package com.bookstore.domain

import java.time.ZonedDateTime
import java.util.UUID

import io.circe.generic.JsonCodec

@JsonCodec
case class Book(
    id: UUID,
    title: String,
    authorId: Int,
    createdAt: ZonedDateTime,
  )
