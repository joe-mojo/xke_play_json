Step 3: render model to JSON with Writes
========================================

## Common features with Reads

Exactly as [Reads](./Step2.md),
 
 - there is some already existing Writes.
 - if all Writes exist for some types, you gain the Writes of their product for free. 
 - you can create custom Writes
 - if a Writes is depending on another one, this dependency need to be already defined at the place the depending Writes is defined.

## Custom Writes

### Using JsValues

You can [create all possible JSON values using subclasses of `JsValue`](https://www.playframework.com/documentation/2.7.x/ScalaJson#Converting-to-a-JsValue)

Once you know how to create a JsValue, you can create the Writes like this:

```scala
import play.api.libs.json._
case class Cat(name: String, age: Int)
implicit val CatWrite = Writes[Cat] { cat =>
    Json.obj(
    "name" -> JsString(cat.name),
    "age" -> JsNumber(cat.age)
    )
}
```

### Using path and combinators

See [Writes with combinators](https://www.playframework.com/documentation/2.7.x/ScalaJsonCombinators#Writes)

As you can see, you

 - create Writes for each attribute with a path request and a call to `write`
 - combine all writes with `and` from `play.api.libs.functional.syntax._`
 - get a `FunctionalBuilder[Write]#CanBuildN[]T1, ..., Tn` that need a function to create a tuple of from all attributes of a model

If you have a case class, you already have a function that do nearly what the `FunctionalBuilder#CanBuild` wants: the `unapply` function of your model's companion object return an optional tuple from an actual instance. Hence the `unlift` call to convert the function that returns an `Option[T]` to a function that return a `T`

Note that calling "unlift" this function is a little bit abusive: in FP, _unlift_ convert a _function_ that returns an option of T to a _partial function_ that return T. The _lift_ is the opposite, it transforms a partial function returning a T to a plain function returning an option of T; that is a Some for the values in the partial domain of the partial function, None for others.

## Exercices

Complete TODOs 3.x in the followings:

 - [`models.Invoice`](../src/main/scala/models/Invoice.scala) 
 - [`models.Movie`](../src/main/scala/models/Movie.scala)
 - [`models.MovieType`](../src/main/scala/models/MovieType.scala)
 - [`models.MovieEvent`](../src/main/scala/models/MovieEvent.scala)
 - [`models.Customer`](../src/main/scala/models/Customer.scala)

The tests about Writes should pass in the following test suites:

 - [`Step3_WritesSpec`](../src/test/scala/service/Step3_WritesScpec.scala)
 - [`InvoiceSpec`](../src/test/scala/models/InvoiceSpec.scala)
 - [`MovieSpec`](../src/test/scala/models/MovieSpec.scala)
 - [`MovieEventSpec`](../src/test/scala/models/MovieEventSpec.scala)
 - [`CustomerSpec`](../src/test/scala/models/CustomerSpec.scala)

## Next

Checkout Step4 and go to [Step4: some real life cases](./Step4.md)