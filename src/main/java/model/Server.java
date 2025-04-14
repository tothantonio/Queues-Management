package model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private final String serverName;
    private final BlockingQueue<Task> taskQ;
    private final AtomicInteger waitingPeriod;
    private final AtomicInteger noPeople;
    private final AtomicBoolean hasFinished = new AtomicBoolean(false);

    public Server(String serverName) {
        this.serverName = serverName;
        this.taskQ = new LinkedBlockingQueue<>();
        this.waitingPeriod = new AtomicInteger(0);
        this.noPeople = new AtomicInteger(0);
    }

    @Override
    public void run() {
        while (!hasFinished.get()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Exception: " + e);
            }
            if(this.taskQ.size() > 1)
                this.waitingPeriod.addAndGet(this.taskQ.size() - 1);
            for (Task task : this.taskQ) {
                task.decrementServiceTime();
                if(task.getServiceTime() <= 0)
                    if(!this.taskQ.remove(task))
                        System.out.println("<DEBUG>ERROR");
                break;
            }
        }
    }

    public synchronized void addTask(Task task) {
        this.noPeople.incrementAndGet();
        this.waitingPeriod.addAndGet(task.getServiceTime() + task.getArrivalTime());
        this.taskQ.add(task);
    }

    public String getServerName() {
        return serverName;
    }
    public BlockingQueue<Task> getTaskQ() {
        return taskQ;
    }
    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }

    public synchronized Boolean setHasFinished() {
        if(this.taskQ.isEmpty()) {
            this.hasFinished.set(true);
            return true;
        }
        return false;
    }
    public synchronized void forceStop() {
        this.hasFinished.set(true);
    }
}