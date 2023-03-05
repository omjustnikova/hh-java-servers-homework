import java.util.concurrent.atomic.AtomicInteger;

public class CounterDAO {

    private static final CounterDAO INSTANCE = new CounterDAO();

    private static AtomicInteger COUNTER = new AtomicInteger(0);

    private CounterDAO() {}

    public static CounterDAO getInstance() {
        return INSTANCE;
    }

    public static AtomicInteger getCounter() {
        return COUNTER;
    }

    public static void incrementCounter() {
       COUNTER.incrementAndGet() ;
    }

    public static synchronized void subtractCounter(int subtractionValue) {
        int newValue = COUNTER.get() - subtractionValue;
        COUNTER.set(newValue);
    }

    public static void clearCounter() {
        COUNTER.set(0);
    }


}
