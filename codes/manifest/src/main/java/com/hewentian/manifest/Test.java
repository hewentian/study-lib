package com.hewentian.manifest;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class Test {
    public static void main(String[] args) throws Exception {
        getManifest();
    }

    static void getManifest() throws IOException {
        JarFile jar = new JarFile(new File("/home/hewentian/Documents/demo/target/demo-0.0.1-SNAPSHOT.jar"));
        Manifest manifest = jar.getManifest();

        System.out.println("-------------------- main attributes");
        Attributes mainAttributes = manifest.getMainAttributes();
        for (Map.Entry<Object, Object> attrEntry : mainAttributes.entrySet()) {
            System.out.println(attrEntry.getKey() + ":" + attrEntry.getValue());
        }

        System.out.println("\n-------------------- entries");
        Map<String, Attributes> entries = manifest.getEntries();
        for (Map.Entry<String, Attributes> entry : entries.entrySet()) {
            Attributes values = entry.getValue();
            for (Map.Entry<Object, Object> attrEntry : values.entrySet()) {
                System.out.println(attrEntry.getKey() + ":" + attrEntry.getValue());
            }
        }

    }

}
