package se.dmolinsky.webshop.model;

public enum OrderStatus {
    PENDING,
    SHIPPED,
    DELIVERED,
    CANCELLED,
    RETURNED;

    public String getStatusString() {
        switch (this) {
            case PENDING:
                return "Pending";
            case SHIPPED:
                return "Shipped";
            case DELIVERED:
                return "Delivered";
            case CANCELLED:
                return "Cancelled";
            case RETURNED:
                return "Returned";
            default:
                return this.name();
        }
    }
}