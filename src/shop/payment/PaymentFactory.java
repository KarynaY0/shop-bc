package shop.payment;

public class PaymentFactory {

    public PaymentMethod create(int choice) {
        switch (choice) {
            case 1:
                return new CreditCardPayment();
            case 2:
                return new PayPalPayment();
            case 3:
                return new GiftCardPayment();
            case 4:
                return new CryptoPayment();
            case 5:
                return new BankTransferPayment();
            default:
                return null;
        }
    }
}
