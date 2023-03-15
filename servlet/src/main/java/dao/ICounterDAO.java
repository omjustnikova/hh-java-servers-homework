package dao;

public interface ICounterDAO {
    int getCounter();

    void incrementCounter();

    void subtractCounter(int subtractionValue);

    void clearCounter();
}
