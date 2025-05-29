package fcu.app.zhuanti.model;

public class HistorySection implements HistoryItem {
    private String date;
    private double totalAmount;

    public HistorySection(String date, double totalAmount) {
        this.date = date;
        this.totalAmount = totalAmount;
    }

    public String getDate() {
        return date;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}
