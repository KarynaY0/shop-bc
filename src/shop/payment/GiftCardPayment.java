package shop.payment;

import java.math.BigDecimal;
import java.util.Scanner;

import shop.model.Order;

public class GiftCardPayment implements PaymentMethod {

    @Override
    public String label() {
        return "Gift Card";
    }

    @Override
    public PaymentOutcome pay(Order order, Scanner input) {
        System.out.print("  Gift card code: ");
        String code = input.nextLine().trim();

        System.out.print("  Available balance: ");
        String rawBalance = input.nextLine().trim();

        if (code.isEmpty()) {
            return PaymentOutcome.fail("Gift card code is required.");
        }

        BigDecimal balance;
        try {
            balance = new BigDecimal(rawBalance);
        } catch (NumberFormatException e) {
            return PaymentOutcome.fail("Balance must be a number.");
        }

        if (balance.signum() < 0) {
            return PaymentOutcome.fail("Balance cannot be negative.");
        }

        BigDecimal total = order.total();
        if (balance.compareTo(total) < 0) {
            return PaymentOutcome.fail("Balance " + balance
                    + " does not cover the order total " + total + ".");
        }

        BigDecimal remaining = balance.subtract(total);
        return PaymentOutcome.ok(Reference.create("GC"),
                "Gift card " + code + " used, remaining balance " + remaining);
    }
}
