package fcu.app.zhuanti.model;

public class history {
    private String note;
    private String category;
    private double amount;
    private int iconRes;

    public history(String note, String category, double amount, int iconRes) {
        this.note = note;
        this.category = category;
        this.amount = amount;
        this.iconRes = iconRes;
    }

    public String getNote() {
        return note;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public int getIconRes() {
        return iconRes;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }
}
