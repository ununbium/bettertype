package dev.errant.bettertype.basic.example.chickens.simplefailable;

import dev.errant.bettertype.basic.failable.SimpleFailable;

public class ChickenCoop {
    boolean doorStuck = false;
    int chickensTotal = 10;
    int chickensInCoop = 7;

    public SimpleFailable<DoorNotClosedReason> closeCoopDoor() {
        if(chickensTotal < chickensInCoop) {
            return SimpleFailable.failure(DoorNotClosedReason.NOT_ALL_ACCOUNTED_FOR);
        } else if(doorStuck) {
            return SimpleFailable.failure(DoorNotClosedReason.DOOR_STUCK);
        } else {
            return SimpleFailable.success();
        }
    }

    public void lockUpForNight() {
        SimpleFailable<DoorNotClosedReason> closedDoor = closeCoopDoor();

        if(closedDoor.isSuccess()) {
            callItADay();
        } else {
            DoorNotClosedReason failure = closedDoor.getFailure();
            fixIssue(failure);
        }
    }

    private void callItADay() {
        //time for tea
    }

    private void fixIssue(DoorNotClosedReason failure) {
        //address failure reason
    }
}
