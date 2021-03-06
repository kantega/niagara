package org.kantega.niagara.json


import java.math.BigDecimal
import io.vavr.collection.List
import org.kantega.niagara.data.curried

typealias JsonDecoder<A> = (JsonValue) -> JsonResult<A>

inline infix fun <A, reified B> JsonDecoder<A>.map(crossinline f: (A) -> B): JsonDecoder<B> =
  { p1 -> this(p1).map(f) }

inline fun <A, reified B> JsonDecoder<A>.tryMap(crossinline f: (A) -> JsonResult<B>): JsonDecoder<B> =
  { p1 -> this(p1).bind(f) }

inline infix fun <A, reified B> JsonDecoder<A>.bind(crossinline f: (A) -> JsonDecoder<B>): JsonDecoder<B> =
  { p1 -> this(p1).bind { a -> f(a)(p1) } }

infix fun <A, B> JsonDecoder<(A) -> B>.apply(v: JsonDecoder<A>): JsonDecoder<B> =
  { p1 -> this(p1).apply(v(p1)) }

fun <A, B> decode(constructor: (A) -> B): JsonDecoder<(A) -> B> =
  { _ -> jOk(constructor) }

fun <A, B, C> decode(constructor: (A, B) -> C): JsonDecoder<(A) -> (B) -> C> =
  decode(constructor.curried())

fun <A, B, C, D> decode(constructor: (A, B, C) -> D): JsonDecoder<(A) -> (B) -> (C) -> D> =
  decode(constructor.curried())

fun <A, B, C, D, E> decode(constructor: (A, B, C, D) -> E): JsonDecoder<(A) -> (B) -> (C) -> (D) -> E> =
  decode(constructor.curried())

fun <A, B, C, D, E, F> decode(constructor: (A, B, C, D, E) -> F): JsonDecoder<(A) -> (B) -> (C) -> (D) -> (E) -> F> =
  decode(constructor.curried())

fun <A, B, C, D, E, F, G> decode(constructor: (A, B, C, D, E, F) -> G): JsonDecoder<(A) -> (B) -> (C) -> (D) -> (E) -> (F) -> G> =
  decode(constructor.curried())

fun <A, B, C, D, E, F, G, H> decode(constructor: (A, B, C, D, E, F, G) -> H): JsonDecoder<(A) -> (B) -> (C) -> (D) -> (E) -> (F) -> (G) -> H> =
  decode(constructor.curried())

fun <A, B, C, D, E, F, G, H, I> decode(constructor: (A, B, C, D, E, F, G, H) -> I): JsonDecoder<(A) -> (B) -> (C) -> (D) -> (E) -> (F) -> (G) -> (H) -> I> =
  decode(constructor.curried())

fun <A, B, C, D, E, F, G, H, I, J> decode(constructor: (A, B, C, D, E, F, G, H, I) -> J): JsonDecoder<(A) -> (B) -> (C) -> (D) -> (E) -> (F) -> (G) -> (H) -> (I) -> J> =
  decode(constructor.curried())


val decodeString: JsonDecoder<String> =
  { it.asString() }

val decodeNumber: JsonDecoder<BigDecimal> =
  { it.asNumber() }

val decodeInt: JsonDecoder<Int> =
  decodeNumber.map { it.toInt() }

val decodeDouble: JsonDecoder<Double> =
  decodeNumber.map { it.toDouble() }

val decodeLong: JsonDecoder<Long> =
  decodeNumber.map { it.toLong() }

val decodeBool: JsonDecoder<Boolean> =
  { it.asBoolean() }

inline fun <reified A> decodeField(name: String, crossinline valueDecoder: JsonDecoder<A>): JsonDecoder<A> =
  { it.field(name).bind(valueDecoder) }

inline fun <reified A, reified B> JsonDecoder<(A) -> B>.field(name: String, crossinline decoderForField: JsonDecoder<A>): JsonDecoder<B> =
  this.apply(decodeField(name, decoderForField))

fun <A, B> JsonDecoder<(A) -> B>.value(a: A): JsonDecoder<B> =
  this.apply({ jOk(a) })

fun <A> decodeArray(elemDecoder: JsonDecoder<A>): JsonDecoder<List<A>> =
  { it.asArray().bind { list -> list.values.map(elemDecoder).traverseJsonResult() } }

fun <A> List<JsonResult<A>>.traverseJsonResult(): JsonResult<List<A>> =
  this.foldRight(jOk(List.empty()), { ja, jas -> jas.bind { alist -> ja.map { a -> alist.prepend(a) } } })

fun <A> JsonDecoder<A>.or(other: JsonDecoder<A>): JsonDecoder<A> =
  { jsonValue -> this(jsonValue).orElse({ other(jsonValue) }) }

fun <A> succeed(a: A): JsonDecoder<A> =
  { _ -> JsonResult.success(a) }