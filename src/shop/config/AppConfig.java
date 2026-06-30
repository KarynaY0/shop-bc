package shop.config;

import java.math.BigDecimal;

public final class AppConfig {

    private static final AppConfig INSTANCE = new AppConfig();

    private final String shopName;
    private final String currency;
    private final BigDecimal taxRate;

    private AppConfig() {
        this.shopName = "Northgate Online Shop";
        this.currency = "EUR";
        this.taxRate = new BigDecimal("0.21");
    }

    public static AppConfig getInstance() {
        return INSTANCE;
    }

    public String getShopName() {
        return shopName;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }
}
