package com.example;

import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class PluginManagerTest {
    @Test
    public void LoaderTest() throws URISyntaxException {
        URL res_url = this.getClass().getResource("/jars");
        assertNotNull(res_url);

        Path pluginPath = Path.of(res_url.toURI());

        PluginManager.pluginPath = pluginPath;

        var plugins = PluginManager.getPluginList();
        assertFalse(plugins.isEmpty());

        assertEquals("com.example.ModuleA", plugins.getFirst());
    }
}
