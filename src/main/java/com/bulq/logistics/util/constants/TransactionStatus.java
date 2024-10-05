package com.bulq.logistics.util.constants;

public enum TransactionStatus {
    NEW("New wallet"),//'completed',
    COMPLETED("Completed transaction"),//'completed'
    PENDING("Pending transaction"),//,'pending'
    CANCELLED("Cancelled transaction");//'cancelled'

    private final String transactionStatus;
    
    // Constructor to assign the string value to each enum constant
    TransactionStatus(String transactionStatus){
        this.transactionStatus = transactionStatus;
    }

    // Getter to access the description
    public String getDescription(){
        return transactionStatus;
    }

    // Static method to map a string from the payload to the enum constant
    public static TransactionStatus fromValue(String value) {
        for (TransactionStatus status : TransactionStatus.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
