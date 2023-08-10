package com.bookstore.integration

import sttp.client3.HttpURLConnectionBackend
import sttp.client3.Identity
import sttp.client3.SttpBackend
import sttp.client3.UriContext
import sttp.client3.basicRequest
import sttp.client3.circe.asJson

import com.bookstore.domain.bookstoreAPI.BookResp

class FreeBookApiClient {
  implicit val backend: SttpBackend[Identity, Any] = HttpURLConnectionBackend()

  def getBooks: List[BookResp] = {
    val apiUrl = uri"https://www.anapioficeandfire.com/api/books"
    val request = basicRequest.get(apiUrl).response(asJson[List[BookResp]])

    val response = request.send(backend)

    response.body match {
      case Right(books) => books
      case Left(error) => throw new RuntimeException(s"Error getting books: ${error.getMessage}")
    }
  }
}
