package shop.payment;

import java.util.Scanner;

import shop.model.Order;

public class CreditCardPayment implements PaymentMethod {

    @Override
    public String label() {
        return "Credit Card";
    }

    @Override
    public PaymentOutcome pay(Order order, Scanner input) {
        System.out.print("  Card number: ");
        String digits = input.nextLine().trim().replaceAll("\\s", "");

        System.out.print("  Card holder name: ");
        String holder = input.nextLine().trim();

        if (!digits.matches("\\d{12,19}")) {
            return PaymentOutcome.fail("Card number must contain 12 to 19 digits.");
        }
        if (holder.isEmpty()) {
            return PaymentOutcome.fail("Card holder name is required.");
        }

        String masked = "****" + digits.substring(digits.length() - 4);
        return PaymentOutcome.ok(Reference.create("CC"),
                holder + " charged on card " + masked);
    }
}
