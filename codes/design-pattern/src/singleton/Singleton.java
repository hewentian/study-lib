package singleton;

/**
 * 1. 单例模式分两种：懒汉模式、 饿汉模式；
 * 2. 在多线程的程序中，singleton可能会变的不可靠，可能会出现多个实例，解决的办法很简单，加个同步修饰符：
 * public static synchronized Singleton getInstance() 这样就保证了线程的安全性；
 * 3. 最后要说的是大家可能会看见一些其他实现Singleton模式的方法，因为模式在具体的应用时是灵活的，不是一成
 * 不变的，并没有一个固定的做法，但大都是下面这种方法的变形；
 */

/**
 * 懒汉模式
 */
//public final class Singleton {
//    private static Singleton singleton;
//
//    private Singleton() {
//    }
//
//    public static Singleton getInstance() {
//        if (null == singleton) {
//            singleton = new Singleton();
//        }
//
//        return singleton;
//    }
//}

/**
 * 饿汉模式
 */
//public final class Singleton {
//    private static final Singleton singleton = new Singleton();
//
//    private Singleton() {
//    }
//
//    public static Singleton getInstance() {
//        return singleton;
//    }
//}

// 当加锁时，下面这种在方法体里面加锁更好
public final class Singleton {
    private static Singleton singleton;
    private static String key = "key";

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (singleton == null) {
            // 锁方法就锁大了
            synchronized (key) {
                if (singleton == null) {
                    singleton = new Singleton();
                }
            }
        }

        return singleton;
    }
}
