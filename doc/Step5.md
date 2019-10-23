JSON transformers
=================
[JSON transformers](https://www.playframework.com/documentation/2.7.x/ScalaJsonTransformers) aim at changing JSON without mapping to model.

## Coast-to-coast design
Official doc introduces [coast-to-coast JSON](https://www.playframework.com/documentation/2.7.x/ScalaJsonTransformers#Introducing-JSON-coast-to-coast-design), a way of designing application dealing with JSON.
Though arguments against OO mapping are admissible, keep in mind that " JSON coast-to-coast" design

- is well suited for small apps like microservices, were sticking to a format and applying business logic directly on it. The small code base make the tight coupling between these two levels of abstraction acceptable.
- is well suited for quick tryouts, drafts, PoC...
- is *not* suited for strongly typed, modularized business logic and separation of levels of abstraction. DDD or "hexagonal architecture" are the opposite of JSON coast-to-coast design.

That said, JSON transformers can be useful beyond "coast-to-coast" design. They may be part of a low-level data validation, data preparation or data formating layers. 

## Exercices

Complete TODOs 5.x in the followings:

 - [`service.Step5`](../src/main/scala/service/Step5.scala) 