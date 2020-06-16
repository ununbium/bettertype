package dev.errant.bettertype.basic.example.chickens.absorber;

import dev.errant.bettertype.basic.absorber.action.ActionThrowableAbsorber;
import dev.errant.bettertype.basic.absorber.supplier.SupplierThrowableAbsorber;
import dev.errant.bettertype.basic.converter.throwable.ThrowableConverters;
import dev.errant.bettertype.basic.failable.Failable;
import dev.errant.bettertype.basic.failable.SimpleFailable;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ChickenFile {

    public SimpleFailable<String> updateChickenFile(String chickenNotes) {
        return ActionThrowableAbsorber.absorb(
            () -> Files.write(Paths.get("chickens.txt"), chickenNotes.getBytes(), StandardOpenOption.APPEND),
            ThrowableConverters.messagePrintingConverter()
        );
    }

    public Failable<String, String> readChickenFile(String chickenNotes) {
        return SupplierThrowableAbsorber.absorb(
                () -> Files.readString(Paths.get("chickens.txt")),
                ThrowableConverters.messagePrintingConverter()
        );
    }


}
