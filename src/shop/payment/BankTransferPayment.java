package shop.payment;

import java.util.Scanner;

import shop.model.Order;

public class BankTransferPayment implements PaymentMethod {

    @Override
    public String label() {
        return "Bank Transfer";
    }

    @Override
    public PaymentOutcome pay(Order order, Scanner input) {
        System.out.print("  IBAN: ");
        String iban = input.nextLine().trim().replaceAll("\\s", "").toUpperCase();

        if (!iban.matches("[A-Z]{2}\\d{2}[A-Z0-9]{10,30}")) {
            return PaymentOutcome.fail("IBAN format is not valid.");
        }

        return PaymentOutcome.ok(Reference.create("BT"),
                "Transfer scheduled from " + iban);
    }
}
