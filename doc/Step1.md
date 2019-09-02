Step 1: Creating JSON
=====================

## JSON values
### JSON types
 
See [JsValue](https://www.playframework.com/documentation/2.7.x/ScalaJson#JsValue)

All JsValues can pattern match easily, except `JsObject` because the order of its field/value entries will count, but this not wanted because in JSON object there is no order.

```scala
import play.api.libs.json._
Json.parse(""" "toto" """) match {
    case JsString(str) => s"""we have the string "$str""""
    case other => s"""Hu ? I didn't expect $other..."""
}
```
Here, please note the trick on `JsNumber`: `BigDecimal` has no `unapply`. 
```scala
import play.api.libs.json._
Json.parse("""["toto"], 1, true]""") match {
    case JsArray(Seq(JsString("toto"), JsNumber(v), JsBoolean(true))) if v == BigDecimal("1")=> true
    case other => false
}
```

In the above code, `JsBoolean(true)` could be replaced with `JsTrue`.

### Create JSON by parsing a string

See  [string parsing](https://www.playframework.com/documentation/2.7.x/ScalaJson#Using-string-parsing)

### Create JSON by building it programmatically

See [class construction](https://www.playframework.com/documentation/2.7.x/ScalaJson#Using-class-construction)

### Use path

See [traversing a JSON structure](https://www.playframework.com/documentation/2.7.x/ScalaJson#Traversing-a-JsValue-structure)

## `validate` or `as` ?

### as

`as` throws exception on failure
`asOpt` returns Some(result) or None if failure

See [`as` and `asOpt`](https://www.playframework.com/documentation/2.7.x/ScalaJson#Using-JsValue.as/asOpt) 

### validate

`validate` return a `JsResult`, than can be either `JsSuccess` or `JsError`. You can pattern match or use usual map/flatMap/fold etc.

See [`validate`](https://www.playframework.com/documentation/2.7.x/ScalaJson#Using-validation)

## Exercise

Complete [Step1_CreatingJsons](../src/main/scala/service/Step1_CreatingJsons.scala); [Step 1 tests](../src/test/scala/service/Step1_CreatingJsonsSpec.scala) must compile and pass.

## Next

Checkout Step2 branch and go to [Step2: Reads](./Step2.md)
