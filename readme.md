# BetterType
A library for making your error handling code a little cleaner.

# Motivation
Returning Optional<String> is a great way to indicate to other developers that a specific outcome might not happen. 
It is often the case that the outcome of an action is more complex than either a value with success or a failure without
a value.

While Exceptions can be used to communicate failure reasons, they can result in some messy code and poor interfaces. By
instead relying on strongly semantic return types the interface becomes cleaner, catch blocks can be eliminated favoring
plain code and the nature of the error and how it is handled becomes more prominent in the planning process. 

# Dependency
Just include the core as follows;
```xml
<dependency>
    <artifactId>bettertype-core</artifactId>
    <groupId>dev.errant</groupId>
    <version>0.4.1</version>
</dependency>
```

# Types
A run through the core concepts with some example code for each.

## Failable
Represents an outcome of an action that can fail. The result can either be successful or failure, each with an 
associated value. The success and failure values can (and normally will) have different types, but note that they are 
arbitrary and inferred from the failable parameter.

Declare much like an optional;
```java
    public Failable<Integer, String> countChickens() {
        if(hatched == eggs) {
            return Failable.success(eggs);
        } else {
            return Failable.failure("Some not yet hatched");
        }
    }
```

Use the type safe responses;
```java
    public void bragAboutNumberOfChickens() {
        Failable<Integer, String> chickenCount = countChickens();

        if (chickenCount.isSuccess()) {
            int chickens = chickenCount.getSuccess();
            brag(chickens);
        } else {
            String reason = chickenCount.getFailure();
            quietlyReport(reason);
        }
    }
```

## SimpleFailable
Represents an outcome of an action that can fail. The result can either be successful or failure, but unlike Failable only
 failure has an associated value for a SimpleFailable.

Declaration is similar to Failable but with no "Success value"; 
```java
    public SimpleFailable<DoorNotClosedReason> closeCoopDoor() {
        if(chickensTotal < chickensInCoop) {
            return SimpleFailable.failure(DoorNotClosedReason.NOT_ALL_ACCOUNTED_FOR);
        } else if(doorStuck) {
            return SimpleFailable.failure(DoorNotClosedReason.DOOR_STUCK);
        } else {
            return SimpleFailable.success();
        }
    }
```

Similarly, null checks or semantically problematic enum values (DoorNotClosedReason.NONE) can be avoided, and the 
resulting code is very readable; 
```java
    public void lockUpForNight() {
        SimpleFailable<DoorNotClosedReason> closedDoor = closeCoopDoor();
        
        if(closedDoor.isSuccess()) {
            callItADay();
        } else {
            DoorNotClosedReason failure = closedDoor.getFailure();
            fixIssue(failure);
        }
    }
```

## Absorbing errors (Exceptions)
The static method "absorb" on Failable and SimpleFailable execute some code and soak up any exceptions. If an exception 
is thrown it is absorbed and returned as the failure reason.

SimpleFailable.absorb(...) is used to wrap an action with no "success value", but some potential exceptions such as 
writing to a file;
```java
    public SimpleFailable<Throwable> updateChickenFile(String chickenNotes) {
        return SimpleFailable.absorb(
            () -> Files.write(Paths.get("chickens.txt"), chickenNotes.getBytes(), StandardOpenOption.APPEND)
        );
    }
```

Failable.absorb(...) is used to wrap an action with a "success value" and some potential exceptions such as 
reading data from a file;
```java
    public Failable<String, Throwable> readChickenFile(String chickenNotes) {
        return Failable.absorb(
                () -> Files.readString(Paths.get("chickens.txt"))
        );
    }
```

Note that the return type is "Throwable" because we've not specified how to convert throwables to other values. 
The next section details how to do this. 

## Throwable Converters
It is normally desirable to convert exceptions to some other more meaningful format, like a String, enumerated "cause" 
or complex type. Throwable converters are a functional interface that can be either overriden inline or by inheritance.
There are also some premade basic converters declared statically in ThrowableConverters.

Using ThrowableConverters.messagePrintingConverter() we can capture just the message from the previous example;
```java
    public Failable<String, String> readChickenFile(String chickenNotes) {
        return Failable.absorb(
                () -> Files.readString(Paths.get("chickens.txt")),
                ThrowableConverters.messagePrintingConverter()
        );
    }
```
Note that the return type is now Failable<String, String> rather than Failable<String, Throwable>

While there are some provided basic implementations, the library user is encouraged to implement their own 
ThrowableConverter to capture any relevant information in cases where Exceptions cannot be eliminated, e.g. 3rd party 
integrations.

# See also
Some very basic examples from this document can be found [here](https://github.com/ununbium/bettertype/tree/master/core/src/test/java/dev/errant/bettertype/basic/example/chickens) (in the core test folder, in the package *dev.errant.bettertype.basic.example.chickens*)
