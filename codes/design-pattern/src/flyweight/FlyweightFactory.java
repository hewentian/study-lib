package flyweight;

import java.util.HashMap;
import java.util.Map;

public class FlyweightFactory {
    private static Map<String, Flyweight> flyweights = new HashMap<String, Flyweight>();

    public FlyweightFactory(String arg) {
        flyweights.put(arg, new FlyweightImpl());
    }

    public static Flyweight getFlyweitht(String key) {
        if (null == flyweights.get(key)) {
            flyweights.put(key, new FlyweightImpl());
        }

        return flyweights.get(key);
    }

    public static int getSize() {
        return flyweights.size();
    }
}
