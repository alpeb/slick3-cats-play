# slick3-cats-play

This project is a minimal implementation of what I've found to be a solid pattern for composing service calls in an asynchronous
non-blocking way
in Scala/Play, while properly handling errors without throwing exceptions.

In this example we're composing two sequential calls, one to the database ([UserService.get](https://github.com/alpeb/slick3-cats-play/blob/master/app/services/UserService.scala)),
followed by a second one to some 3rd party API over the network
([GenderService.get](https://github.com/alpeb/slick3-cats-play/blob/master/app/services/GenderService.scala)).

Each call is asynchronous, so they return `Future` (which is a Monad): the database call uses Slick v3 which returns `Future` out of the
box and does all the thread-pooling work on its own without blocking, and the network call uses Play's webservices API which does the same.

These two facts allow us to compose the calls through a for-comprehension like shown in [Application.scala](https://github.com/alpeb/slick3-cats-play/blob/master/app/controllers/Application.scala).

The services' actual result type is of the form `Future[Throwable Xor User]`, making use of [cat's](https://github.com/non/cats)
`Xor` type to capture eventual errors instead of throwing exceptions.

As mentioned above, both services use themselves APIs that return `Future`. Whenever these APIs fail, we capture the problem with the
recover combinator, transforming the `Throwable` into a left `Xor` instance.

Back into  [Application.scala](https://github.com/alpeb/slick3-cats-play/blob/master/app/controllers/Application.scala),
combining these two `Future[Throwable Xor Blah]` can't be done just like that, because we would need to
flatMap both the `Future` and the `Xor`, i.e. compose on the future's eventual result and then compose on the success/failure expressed
by the `Xor` instance.

Monad Transformers let us alleviate this task; we just wrap our "composite" monads into `XorT()` and then we can use a simple
for-comprehension to compose the calls. Then we unwrap things to get back a `Future[Throwable Xor Result]` by calling `.value` on the
result of the for-comprehension.

Also note the controller is built with `Action.async` so that we're fine with just returning a `Future`
to Play, and thus everything is handled asynchronously down from the database/network calls up until Play's guts.
