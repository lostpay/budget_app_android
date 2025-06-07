package fcu.app.zhuanti.model;

public class Goals {
    private long id;
    private String name;
    private double currentAmount;
    private double targetAmount;

    // Constructors
    public Goals() {}

    public Goals(long id, String name, double currentAmount, double targetAmount) {
        this.id = id;
        this.name = name;
        this.currentAmount = currentAmount;
        this.targetAmount = targetAmount;
    }

    public Goals(String name, double currentAmount, double targetAmount) {
        this.name = name;
        this.currentAmount = currentAmount;
        this.targetAmount = targetAmount;
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(double currentAmount) {
        this.currentAmount = currentAmount;
    }

    public double getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(double targetAmount) {
        this.targetAmount = targetAmount;
    }

    // Optional: get progress as percentage
    public int getProgressPercent() {
        if (targetAmount == 0) return 0;
        return (int) ((currentAmount / targetAmount) * 100);
    }
}
