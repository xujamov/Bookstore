package com.bookstore.domain

import java.util.UUID

import io.circe.generic.JsonCodec

@JsonCodec
case class Author(id: UUID, name: String)
