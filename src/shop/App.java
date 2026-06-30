package shop;

import java.math.BigDecimal;
import java.util.Scanner;

import shop.config.AppConfig;
import shop.model.Item;
import shop.model.Order;
import shop.payment.PaymentFactory;
import shop.payment.PaymentMethod;
import shop.payment.PaymentOutcome;
import shop.repository.OrderRepository;

public class App {

    private final Scanner input = new Scanner(System.in);
    private final AppConfig config = AppConfig.getInstance();
    private final PaymentFactory paymentFactory = new PaymentFactory();
    private final OrderRepository repository = new OrderRepository();
    private Order currentOrder;

    public void run() {
        System.out.println("Welcome to " + config.getShopName());
        boolean running = true;
        while (running) {
            showMenu();
            String choice = input.nextLine().trim();
            switch (choice) {
                case "1":
                    createOrder();
                    break;
                case "2":
                    addItem();
                    break;
                case "3":
                    showSummary();
                    break;
                case "4":
                    payOrder();
                    break;
                case "5":
                    showCompleted();
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Unknown option.");
            }
            System.out.println();
        }
        System.out.println("Thanks for visiting " + config.getShopName() + ".");
    }

    private void showMenu() {
        System.out.println("-------------------------------");
        System.out.println("1) Create order");
        System.out.println("2) Add item");
        System.out.println("3) View order summary");
        System.out.println("4) Pay for order");
        System.out.println("5) View completed orders");
        System.out.println("0) Exit");
        System.out.print("Select: ");
    }

    private void createOrder() {
        currentOrder = new Order();
        System.out.println("Started order " + currentOrder.getId() + ".");
    }

    private void addItem() {
        if (currentOrder == null) {
            System.out.println("Create an order first.");
            return;
        }

        System.out.print("Item name: ");
        String name = input.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Item name cannot be empty.");
            return;
        }

        BigDecimal price = readAmount("Unit price: ");
        if (price == null) {
            return;
        }

        int quantity = readQuantity("Quantity: ");
        if (quantity <= 0) {
            return;
        }

        currentOrder.addItem(new Item(name, price, quantity));
        System.out.println("Added " + quantity + " x " + name + ".");
    }

    private void showSummary() {
        if (currentOrder == null) {
            System.out.println("No active order.");
            return;
        }
        if (currentOrder.isEmpty()) {
            System.out.println("Order " + currentOrder.getId() + " has no items yet.");
            return;
        }

        String currency = config.getCurrency();
        System.out.println("Order " + currentOrder.getId());
        for (Item item : currentOrder.getItems()) {
            System.out.println("  " + item.getQuantity() + " x " + item.getName()
                    + " @ " + item.getUnitPrice() + " = " + item.lineTotal() + " " + currency);
        }
        System.out.println("  Subtotal: " + currentOrder.subtotal() + " " + currency);
        System.out.println("  Tax (" + taxPercent() + "%): " + currentOrder.tax() + " " + currency);
        System.out.println("  Total: " + currentOrder.total() + " " + currency);
    }

    private void payOrder() {
        if (currentOrder == null || currentOrder.isEmpty()) {
            System.out.println("There is nothing to pay for.");
            return;
        }

        System.out.println("Amount due: " + currentOrder.total() + " " + config.getCurrency());
        System.out.println("Payment methods:");
        System.out.println("  1) Credit Card");
        System.out.println("  2) PayPal");
        System.out.println("  3) Gift Card");
        System.out.println("  4) Crypto");
        System.out.println("  5) Bank Transfer");
        System.out.print("Pick a method: ");

        int choice;
        try {
            choice = Integer.parseInt(input.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("That is not a valid choice.");
            return;
        }

        PaymentMethod method = paymentFactory.create(choice);
        if (method == null) {
            System.out.println("Unknown payment method.");
            return;
        }

        PaymentOutcome outcome = method.pay(currentOrder, input);
        if (outcome.isSuccess()) {
            currentOrder.markPaid(method.label(), outcome.getReference());
            repository.save(currentOrder);
            System.out.println("Approved: " + outcome.getDetail());
            System.out.println("Reference: " + outcome.getReference());
            currentOrder = null;
        } else {
            System.out.println("Declined: " + outcome.getDetail());
        }
    }

    private void showCompleted() {
        if (repository.count() == 0) {
            System.out.println("No completed orders yet.");
            return;
        }
        System.out.println("Completed orders:");
        for (Order order : repository.findAll()) {
            System.out.println("  " + order.getId()
                    + " | " + order.total() + " " + config.getCurrency()
                    + " | " + order.getPaymentLabel()
                    + " | " + order.getPaymentReference());
        }
    }

    private BigDecimal readAmount(String prompt) {
        System.out.print(prompt);
        String raw = input.nextLine().trim();
        try {
            BigDecimal value = new BigDecimal(raw);
            if (value.signum() <= 0) {
                System.out.println("Value must be greater than zero.");
                return null;
            }
            return value;
        } catch (NumberFormatException e) {
            System.out.println("That is not a valid number.");
            return null;
        }
    }

    private int readQuantity(String prompt) {
        System.out.print(prompt);
        String raw = input.nextLine().trim();
        try {
            int value = Integer.parseInt(raw);
            if (value <= 0) {
                System.out.println("Quantity must be greater than zero.");
                return 0;
            }
            return value;
        } catch (NumberFormatException e) {
            System.out.println("That is not a whole number.");
            return 0;
        }
    }

    private String taxPercent() {
        return config.getTaxRate()
                .multiply(new BigDecimal("100"))
                .stripTrailingZeros()
                .toPlainString();
    }
}
