package fcu.app.zhuanti.model;

public class history {
    private String name;
    private String category;
    private int amount;
    private int imageResId;

    public history(String name, String category, int amount, int imageResId) {
        this.name = name;
        this.category = category;
        this.amount = amount;
        this.imageResId = imageResId;
    }

    public String getName() { return name; }
    public String getCategory() { return category; }
    public int getAmount() { return amount; }
    public int getImageResId() { return imageResId; }
}
