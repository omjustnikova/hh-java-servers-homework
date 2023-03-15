package dao;

import java.util.concurrent.atomic.LongAdder;

public class CounterDAO implements ICounterDAO {

    private final static CounterDAO INSTANCE = new CounterDAO();

    private final LongAdder counter = new LongAdder();

    private CounterDAO() {
    }

    public static CounterDAO getInstance() {
        return INSTANCE;
    }

    @Override
    public int getCounter() {
        return counter.intValue();
    }

    @Override
    public void incrementCounter() {
        counter.increment();
    }

    @Override
    public void subtractCounter(int subtractionValue) {
        if (subtractionValue != 0) {
            counter.add(-1 * subtractionValue);
        }
    }

    @Override
    public void clearCounter() {
        counter.reset();
    }
}
