package se.dmolinsky.webshop;

public enum Category {
    FLOWERS("Flowers"),
    PLANTS("Plants"),
    SEEDS("Seeds"),
    POTS("Pots"),
    ACCESSORIES("Accessories");

    private final String categoryName;

    Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
