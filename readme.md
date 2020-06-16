# Bettertype
Some basic java types to make life a bit better.

## Types

### Failable
Represents an outcome of an action that can fail. The result can either be successful or failure, each with an 
associated value. The success and failure values can (and normally will) have different types.

Declare much like an optional;
```java
Failable<Integer, String> FailableCountChickens;
FailableCountChickens chickens = Failable.success(7);
FailableCountChickens otherChickens = Failable.failure("Some not yet hatched");
```

Use the type safe responses without messy exception handling;
```java
Failable<Integer, String> failableCountChickens = countChickens();

if(FailableCountChickens.isSuccess()) {
    int chickens = failableCountChickens.get();
    ...
} else {
    String failureReason = failableCountChickens.getFailure()
    ...
}
```


### SimpleFailable
Represents an outcome of an action that can fail. The result can either be successful or failure, only failure has an 
associated value.

Usage Example; the process of sending an email succeeds or fails. There is no "success" value necessary, and the 
failure value can be a Simple string that describes the issue. 

Either "success (no value)" or "failure with value".

Declare much like an optional;
```java
Failable<String> emailSent = sendEmail(someContent);
if(emailSent.isSuccess()) {
    //...
} else {
    error("something went wrong; " + emailSent.getFailure());
}
```

## Absorbers
Absorbers are utility methods that soak up exceptions and turn them into more useful types.

```java
Failable<String, String> outcome = SupplierThrowableAbsorber.absorb(
    this::readFile();
    , (throwable) -> throwable.getMessage() );

if(outcome.isSuccess()) {
    String contents = outcome.getSuccess();
    //...
} else {
    logger.error(outcome.getFailure());
}
```
