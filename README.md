XKE Play JSON
==============

## Intro
Learn [Play JSON](https://www.playframework.com/documentation/2.7.x/ScalaJson) with this hands-on exercise.

Play JSON was originally part of [Playframework](https://www.playframework.com), but is an independent lib for a long time now. So you don't need any Playframework concept or dependency to use Play JSON.
Despite that, the Play JSON doc is found in Playframework one. And on [StakOverflow](https://stackoverflow.com/search?q=play+json) too.

## Doc
You may need [Play Json API doc](https://www.playframework.com/documentation/2.6.x/api/scala/index.html#package)

Other needed doc will be refered to in the appropriate chapter.

## General principles

Play JSON allow parsed JSON values handling with a set of classes representing possible JSON value types.
You have `JsString`, `JsArray`, `JsNull`, `JsObject`, etc.

Though you can explore and transform easily JSON values, an interesting part of this lib is mapping JSON values from/to model instances.

Mapping a JSON value to a model is achieved with methods of `JsValue` like `as[T]`, `asOpt[T]` and `validate[T]`. They all needs an implicit `Reads[T]` to map a JSON value to a model.

Rendering JSON from a model is achieved with `Json.toJson[T](value: T)` that will render a JSON value from a model instance provided an implicit `Writes[T]`.

After a short intro on JsValue and JsPath, we will study how to create Reads and Writes.

## Start of trail

Before starting, be aware that some steps have sub-steps (like 3.7.4 for example). Some sub-steps have their own branch because there is different difficulty level.
For example, if you finished all sub-steps of "Step1" branch, you can checkout "Step2" to get Step1 solutions.
Once you finished Step 2.6, you see that you chan checkout "Step2.7.1_easy" or ""Step2.7.1_hard" depending on how much you want to be hurt.

WARNING: checkout of any branch will give solution for former steps !

General advice: when starting a Step N (by doing a checkout on "StepN" branch), always do `git branch -a` to remember if the current step have difficulty levels

To start, checkout `Step1` branch and follow [Step1 guide](doc/Step1.md)

