    import java.util.concurrent.atomic.AtomicInteger;

    public class CounterDAO {

        private final static CounterDAO INSTANCE = new CounterDAO();

        private final AtomicInteger counter = new AtomicInteger(0);

        private CounterDAO() {}

        public static CounterDAO getInstance() {
            return INSTANCE;
        }

        public AtomicInteger getCounter() {
            return counter;
        }

        public void incrementCounter() {
           counter.incrementAndGet() ;
        }

        public synchronized void subtractCounter(int subtractionValue) {
            int newValue = counter.get() - subtractionValue;
            counter.set(newValue);
        }

        public void clearCounter() {
            counter.set(0);
        }


    }
