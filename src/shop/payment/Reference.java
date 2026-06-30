package shop.payment;

import java.util.UUID;

final class Reference {

    private Reference() {
    }

    static String create(String prefix) {
        return prefix + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
