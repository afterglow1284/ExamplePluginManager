package com.example;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class PluginManager {
    static PluginManager INSTANCE = null;
    static Path pluginPath = Path.of("YOUR_PATH_HERE");
    final List<String> modules = new ArrayList<>();

    public static List<String> getPluginList() {
        if (INSTANCE == null) {
            INSTANCE = new PluginManager(pluginPath);
        }
        return INSTANCE.modules;
    }

    private PluginManager(Path pluginDir) {
        List<URL> jarUrls = new ArrayList<>();

        try (var paths = Files.list(pluginDir)) {
            paths
                    .filter(p -> p.toString().endsWith(".jar"))
                    .forEach(p -> {
                        try {
                            jarUrls.add(p.toUri().toURL());
                        } catch (MalformedURLException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        URLClassLoader ucl = new URLClassLoader(
                jarUrls.toArray(new URL[0]),
                Plugin.class.getClassLoader()
        );

        ServiceLoader<Plugin> sl = ServiceLoader.load(Plugin.class, ucl);

        for (Plugin p : sl) {
            modules.add(p.getName());
        }
    }
}
