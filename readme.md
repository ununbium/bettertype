# BetterType
A library for making your error handling code a little cleaner.

# Motivation
Returning Optional<String> is a great way to indicate to people that a specific outcome might not happen. 
It is often the case that the outcome of an action is more complex than either a value with success or a failure without
a value.

While Exceptions can be used to communicate failure reasons, they can result in some messy code and poor interfaces. By
instead relying on strongly semantic return types the interface becomes cleaner, catch blocks can be eliminated favoring
plain code and the nature of the error and how it is handled becomes more prominent in the planning process. 

## Types

### Failable
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

### SimpleFailable
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

## Absorbers
Absorbers are utility methods that soak up exceptions and turn them into more useful types.

In this case we simply want to write to a file, so a SimpleFailable is an appropriate return type;
```java
    public SimpleFailable<String> updateChickenFile(String chickenNotes) {
        return ActionThrowableAbsorber.absorb(
            () -> Files.write(Paths.get("chickens.txt"), chickenNotes.getBytes(), StandardOpenOption.APPEND),
            ThrowableConverters.messagePrintingConverter()
        );
    }
```

In this case we want to read a file, so we need the slightly more complicated Failable;
```java
    public Failable<String, String> readChickenFile(String chickenNotes) {
        return SupplierThrowableAbsorber.absorb(
                () -> Files.readString(Paths.get("chickens.txt")),
                ThrowableConverters.messagePrintingConverter()
        );
    }
```


# See also
Some very basic examples from this document can be found in the core test folder, in the package *dev.errant.bettertype.basic.example.chickens*