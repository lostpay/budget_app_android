package fcu.app.zhuanti.model;

public class HistorySection implements HistoryItem {
    private String date;
    private double total;

    public HistorySection(String date, double total) {
        this.date = date;
        this.total = total;
    }

    public String getDate() {
        return date;
    }

    public double getTotal() {
        return total;
    }
}
