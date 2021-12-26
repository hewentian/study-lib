package com.hewentian.jol;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

/**
 * maven自动下载的包，可能是有问题的。例如，程序中找不到相应的类，如 ClassLayout
 * 这时，我们要在本地的maven仓库中删掉相应的jar包，然后手动在maven中央仓库下载相应的jar包，并放到刚才删掉的位置
 * *
 * 详细示例，请查看这里 https://github.com/openjdk/jol/tree/master/jol-samples
 */
public class Test {
    public static void main(String[] args) {
        A a = new A();

        B b = new B();
        B b2 = new B();
        b2.setL(2);

        C c = new C();

        int[] ia = new int[0];
        int[] ia2 = new int[3];
        ia2[0] = 12;
        ia2[1] = 13;
        ia2[2] = 14;

        System.out.println(VM.current().details());
        System.out.println(ClassLayout.parseClass(String.class).toPrintable());
        System.out.println(ClassLayout.parseInstance("hello world").toPrintable());

        System.out.println(ClassLayout.parseInstance(a).toPrintable());
        System.out.println(ClassLayout.parseInstance(b).toPrintable());
        System.out.println(ClassLayout.parseInstance(b2).toPrintable());
        System.out.println(ClassLayout.parseInstance(c).toPrintable());
        System.out.println(ClassLayout.parseInstance(ia).toPrintable());
        System.out.println(ClassLayout.parseInstance(ia2).toPrintable());
    }
}
