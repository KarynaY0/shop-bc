package shop.payment;

import java.util.Scanner;

import shop.model.Order;

public class CryptoPayment implements PaymentMethod {

    @Override
    public String label() {
        return "Crypto";
    }

    @Override
    public PaymentOutcome pay(Order order, Scanner input) {
        System.out.print("  Wallet address: ");
        String wallet = input.nextLine().trim();

        if (wallet.length() < 8) {
            return PaymentOutcome.fail("Wallet address looks invalid.");
        }

        String shortWallet = wallet.substring(0, 4) + "..."
                + wallet.substring(wallet.length() - 4);
        return PaymentOutcome.ok(Reference.create("CR"),
                "Coins sent to wallet " + shortWallet);
    }
}
