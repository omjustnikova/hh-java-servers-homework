package dao;

import dao.CounterDAO;

import java.util.concurrent.atomic.AtomicInteger;

public interface ICounterDAO {
        public AtomicInteger getCounter();
        public void incrementCounter();
        public void subtractCounter(int subtractionValue);
        public void clearCounter();
}
