package shop.payment;

public class PaymentOutcome {

    private final boolean success;
    private final String reference;
    private final String detail;

    private PaymentOutcome(boolean success, String reference, String detail) {
        this.success = success;
        this.reference = reference;
        this.detail = detail;
    }

    public static PaymentOutcome ok(String reference, String detail) {
        return new PaymentOutcome(true, reference, detail);
    }

    public static PaymentOutcome fail(String detail) {
        return new PaymentOutcome(false, null, detail);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getReference() {
        return reference;
    }

    public String getDetail() {
        return detail;
    }
}
