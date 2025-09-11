package com.p1nero.globalshaderpackproperties.client;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class GlobalShaderpackPropertiesMod implements ClientModInitializer {

    private static final Logger LOGGER = LogUtils.getLogger();
    public static final Path SOURCE_DIR = Paths.get("config/global_shaderpack_properties");
    private static final Set<String> GLOBAL_PROPERTIES_SET = new HashSet<>();
    private static final Set<String> CURRENT_PROPERTIES = new HashSet<>();

    public static void reloadCurrentPropertiesNameCache(String currentName) {
        reloadPropertiesNameCache(CURRENT_PROPERTIES, SOURCE_DIR.resolve(currentName));
    }

    public static void reloadGlobalPropertiesNameCache() {
        reloadPropertiesNameCache(GLOBAL_PROPERTIES_SET, SOURCE_DIR);
    }

    /**
     * 提前缓存省得重复判断
     */
    public static void reloadPropertiesNameCache(Set<String> set, Path path) {
        set.clear();
        if(!Files.exists(path)) {
            LOGGER.error("Source directory {} not exist, Skipped.", path);
            return;
        }
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            for (Path entry : stream) {
                if (Files.isRegularFile(entry)) {
                    String fileName = entry.getFileName().toString();
                    set.add(fileName);
                    LOGGER.debug("Added file to global properties: {}", fileName);
                }
            }
        } catch (IOException e) {
            LOGGER.error("Failed to read source directory: {}", path, e);
        }

        LOGGER.info("Reloaded properties set: {} files", set.size());
    }

    public static boolean hasCurrentProperties(String name) {
        return CURRENT_PROPERTIES.contains(name);
    }

    public static boolean hasGlobalProperties(String name) {
        return GLOBAL_PROPERTIES_SET.contains(name);
    }

    public static String removeZipSuffix(String filename) {
        if (filename != null && filename.toLowerCase().endsWith(".zip")) {
            return filename.substring(0, filename.length() - 4);
        }
        return filename;
    }
    @Override
    public void onInitializeClient() {
        try {
            if (!Files.exists(SOURCE_DIR)) {
                Files.createDirectories(SOURCE_DIR);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to create config/global_shaderpack_properties", e);
        }
    }
}
