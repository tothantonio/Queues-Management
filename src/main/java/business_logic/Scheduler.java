package business_logic;

import model.Server;
import model.Task;
import util.SelectionPolicy;
import util.Strategy;

import java.util.ArrayList;

public class Scheduler {
    private final ArrayList<Server> serverList;
    private Strategy strategy;

    public Scheduler(Integer maximumNumberOfServers) {
        this.serverList = new ArrayList<>();
        for (int i = 0; i < maximumNumberOfServers; i++) {
            this.serverList.add(new Server("Thread #" + (i + 1)));
            Thread t = new Thread(this.serverList.get(i), this.serverList.get(i).getServerName());
            t.start();
        }
    }

    public void changeStrategy(SelectionPolicy policy) {
        switch (policy) {
            case SHORTEST_TIME -> this.strategy = new TimeStrategy();
            case SHORTEST_QUEUE -> this.strategy = new ShortestQueueStrategy();
            default -> System.out.println("Invalid Selection Policy");
        }
    }

    public synchronized void dispatchTask(Task task) {
        this.strategy.addTask(this.serverList, task);
    }

    public synchronized Boolean stopServers() {
        boolean flag = true;
        for (Server server : this.serverList) {
            flag = flag & server.setHasFinished();
        }
        return flag;
    }

    public synchronized void forceStopServers() {
        for (Server server : this.serverList) {
            server.forceStop();
        }
    }

    public ArrayList<Server> getServerList() {
        return serverList;
    }
}