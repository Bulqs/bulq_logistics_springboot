package com.bulq.logistics.util.constants;

public enum TransactionType {
    NEWWALLET("WALLET NOT YET FUNDED"),//fund
    FUND("ADD MONEY TO WALLET "),//fund
    WITHDRAW("DEDUCT MONEY FROM WALLET");//withdraw

    private final String transactionType;
    
    // Constructor to assign the string value to each enum constant
    TransactionType(String transactionType){
        this.transactionType = transactionType;
    }

    // Getter to access the description
    public String getDescription(){
        return transactionType;
    }

    // Static method to map a string from the payload to the enum constant
    public static TransactionType fromValue(String value) {
        for (TransactionType type : TransactionType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
