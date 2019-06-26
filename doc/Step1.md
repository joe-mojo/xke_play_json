Step 1: Creating JSON
=====================

## JSON values
### JSON types
 
See [JsValue](https://www.playframework.com/documentation/2.7.x/ScalaJson#JsValue)

### Create JSON by parsing a string

See  [string parsing](https://www.playframework.com/documentation/2.7.x/ScalaJson#Using-string-parsing)

### Create JSON by building it programmatically

See [class construction](https://www.playframework.com/documentation/2.7.x/ScalaJson#Using-class-construction)

### Use path

See [traversing a JSON structure](https://www.playframework.com/documentation/2.7.x/ScalaJson#Traversing-a-JsValue-structure)

## validate or as ?

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

See [Step2: Reads](./Step2.md)
