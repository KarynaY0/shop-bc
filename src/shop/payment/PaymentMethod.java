package shop.payment;

import java.util.Scanner;

import shop.model.Order;

public interface PaymentMethod {

    String label();

    PaymentOutcome pay(Order order, Scanner input);
}
