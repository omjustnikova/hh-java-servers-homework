package dao;

import java.util.concurrent.atomic.AtomicInteger;

public class CounterDAO implements ICounterDAO {

    private final static CounterDAO INSTANCE = new CounterDAO();

    private final AtomicInteger counter = new AtomicInteger(0);

    private CounterDAO() {}

    public static CounterDAO getInstance() {
        return INSTANCE;
    }

    @Override
    public AtomicInteger getCounter() {
        return counter;
    }

    @Override
    public void incrementCounter() {
       counter.incrementAndGet() ;
    }

    @Override
    public synchronized void subtractCounter(int subtractionValue) {
        int newValue = counter.get() - subtractionValue;
        counter.set(newValue);
    }

    @Override
    public void clearCounter() {
        counter.set(0);
    }


}
