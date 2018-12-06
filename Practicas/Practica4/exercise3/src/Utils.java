import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Utils {

    public static String s(Object... objects) {
        StringBuffer buffer = new StringBuffer();

        for (Object object : objects) {
            if (object != null) {
                buffer.append(object.toString());
            }

        }

        return buffer.toString();
    }


}
