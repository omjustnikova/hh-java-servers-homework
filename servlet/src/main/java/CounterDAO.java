public class CounterDAO {

    private static final CounterDAO INSTANCE = new CounterDAO();

    private static int COUNTER = 0;

    private CounterDAO() {}

    public static CounterDAO getInstance() {
        return INSTANCE;
    }

    public static int getCounter() {
        return COUNTER;
    }

    public static void incrementCounter() {
       COUNTER++;
    }

    public static void subtractCounter(int subtractionValue) {
        COUNTER-=subtractionValue;
    }

    public static void clearCounter() {
        COUNTER = 0;
    }


}
