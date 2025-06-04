package fcu.app.zhuanti.model;

public class HistoryTransaction implements HistoryItem {
    private long id;
    private String note;
    private String category;
    private double amount;
    private String date;
    private int iconRes;

    public HistoryTransaction(long id, String note, String category, double amount, String date, int iconRes) {
        this.id = id;
        this.note = note;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.iconRes = iconRes;
    }
    public long getId() {
        return id;
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

    public String getDate() {
        return date;
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

    public void setDate(String date) {
        this.date = date;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }
}
