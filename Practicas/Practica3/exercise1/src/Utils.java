import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Utils {

    public static Long timeElapsed(String msg) {
        System.out.println(msg);
        return System.nanoTime();
    }

    public static Long timeElapsed(String msg, Date since) {
        Date now = new Date();
        long elapsed = now.getTime() - since.getTime();
        System.out.println(nullSafeConcat(msg, " elapsed ms: ", elapsed, " (", TimeUnit.MILLISECONDS.toSeconds(elapsed), "sec)"));
        return elapsed;
    }

    public static Long timeElapsed(String msg, Long since) {

        long elapsed = System.nanoTime() - since;
        System.out.println(nullSafeConcat(msg, " elapsed ms: ", (double)elapsed / 1000000, " ns: ", elapsed));
        return elapsed;

    }

    public static String nullSafeConcat(Object... objects) {
        StringBuffer buffer = new StringBuffer();

        for (Object object : objects) {
            if (object != null) {
                buffer.append(object.toString());
            }

        }

        return buffer.toString();
    }

    public static double calculateStandardDeviation(List<Double> numbers)
    {
        double sum = 0.0, standardDeviation = 0.0;
        int length = numbers.size();

        for(double num : numbers) {
            sum += num;
        }

        double mean = sum/length;

        for(double num: numbers) {
            standardDeviation += Math.pow(num - mean, 2);
        }

        return Math.sqrt(standardDeviation/length);
    }

}
