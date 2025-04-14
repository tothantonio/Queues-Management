package model;

public class Task {
    private final Integer id;
    private final Integer arrivalTime;
    private Integer serviceTime;

    public Task(Integer id, Integer arrivalTime, Integer serviceTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }

    @Override
    public String toString() {
        return this.id + " -> (arrT: " + this.arrivalTime + "; servT: " + this.serviceTime + ")";
    }

    public Integer getArrivalTime() {
        return arrivalTime;
    }
    public Integer getServiceTime() {
        return serviceTime;
    }
    public void decrementServiceTime() {
        this.serviceTime -= 1;
    }
}