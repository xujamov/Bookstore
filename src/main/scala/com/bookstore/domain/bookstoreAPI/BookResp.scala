package com.bookstore.domain.bookstoreAPI

import java.util.UUID

import io.circe.generic.JsonCodec

@JsonCodec
case class BookResp(
//    id: UUID,
    name: String,
    mediaType: String,
//    url: String,
//    numberOfPages: Int,
    authors: List[String],
  )
