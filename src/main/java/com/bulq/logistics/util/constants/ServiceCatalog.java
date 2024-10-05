package com.bulq.logistics.util.constants;

public enum ServiceCatalog {
    PUP("Pick Up Package"),
    BADO("Book a Drop Off"),
    DP("Deliver Package");

    private final String catalog;
    
    // Constructor to assign the string value to each enum constant
    ServiceCatalog(String catalog){
        this.catalog = catalog;
    }

    // Getter to access the description
    public String getDescription(){
        return catalog;
    }

    // Static method to map a string from the payload to the enum constant
    public static ServiceCatalog fromValue(String value) {
        for (ServiceCatalog catalog : ServiceCatalog.values()) {
            if (catalog.name().equalsIgnoreCase(value)) {
                return catalog;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
