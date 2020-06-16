package dev.errant.bettertype.basic.example.chickens.failable;

import dev.errant.bettertype.basic.failable.Failable;


public class CountChickens {
    int hatched = 4;
    int eggs = 10;

    public Failable<Integer, String> countChickens() {
        if(hatched == eggs) {
            return Failable.success(eggs);
        } else {
            return Failable.failure("Some not yet hatched");
        }
    }

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

    private void brag(int chickens) {
        //brags on twitter
    }

    private void quietlyReport(String reason) {
        //DMs reason
    }
}
