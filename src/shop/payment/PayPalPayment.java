package shop.payment;

import java.util.Scanner;

import shop.model.Order;

public class PayPalPayment implements PaymentMethod {

    @Override
    public String label() {
        return "PayPal";
    }

    @Override
    public PaymentOutcome pay(Order order, Scanner input) {
        System.out.print("  PayPal email: ");
        String email = input.nextLine().trim();

        if (!email.matches("[^@\\s]+@[^@\\s]+\\.[^@\\s]+")) {
            return PaymentOutcome.fail("Email address is not valid.");
        }

        return PaymentOutcome.ok(Reference.create("PP"),
                "Charged PayPal account " + email);
    }
}
