package org.kantega.niagara.json.examples

import org.kantega.niagara.json.JsonArray
import org.kantega.niagara.json.JsonNumber
import org.kantega.niagara.json.JsonObject
import org.kantega.niagara.json.JsonString
import org.kantega.niagara.json.io.JsonWriter

fun main() {
    val json =
      JsonObject(
        "name" to JsonString("Ola"),
        "age" to JsonNumber(43),
        "address" to JsonObject(
          "street" to JsonString("Northstreet"),
          "num" to JsonString("44"),
          "city" to JsonString("Oslo")
        )
      )

    val arr =
      JsonArray(JsonString("values"), JsonString("b"))

    println(JsonWriter.writePretty(json,4))
    println(JsonWriter.writePretty(arr,4))
}