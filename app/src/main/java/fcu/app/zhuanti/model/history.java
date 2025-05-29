package fcu.app.zhuanti.model;

public class history {
    private String note;
    private String category;
    private double amount;
    private int iconRes;
    private String date;

    public history(String note, String category, double amount, int iconRes, String date) {
        this.note = note;
        this.category = category;
        this.amount = amount;
        this.iconRes = iconRes;
        this.date = date;
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

    public String getDate() {
        return date;
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

    public void setDate(String date) {
        this.date = date;
    }
}
