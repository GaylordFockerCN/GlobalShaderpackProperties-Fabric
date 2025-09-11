package com.p1nero.globalshaderpackproperties.client.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.p1nero.globalshaderpackproperties.client.GlobalShaderpackPropertiesMod;
import net.irisshaders.iris.Iris;
import net.irisshaders.iris.shaderpack.ShaderPack;
import org.spongepowered.asm.mixin.Mixin;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Optional;

@Mixin(ShaderPack.class)
public abstract class ShaderPackMixin {

    @WrapMethod(method = "readProperties", remap = false)
    private static String globalshadersproperties$readProperties(Path shaderPath, String name, Operation<String> original) {
        Optional<String> optName = Iris.getIrisConfig().getShaderPackName();
        Path path = GlobalShaderpackPropertiesMod.SOURCE_DIR;

        if(optName.isPresent()) {
            if(GlobalShaderpackPropertiesMod.hasCurrentProperties(name)) {
                String filename = GlobalShaderpackPropertiesMod.removeZipSuffix(optName.get());
                path = path.resolve(filename);
            }
        } else {
            if(!GlobalShaderpackPropertiesMod.hasGlobalProperties(name)) {
                return null;
            }
        }
        try {
            return Files.readString(path.resolve(name), StandardCharsets.ISO_8859_1);
        } catch (NoSuchFileException e) {
            Iris.logger.debug("An " + path + " " + name + " file was not found in the current shaderpack.Trying to use original path");
            return original.call(shaderPath, name);
        } catch (IOException e) {
            Iris.logger.error("An IOException occurred reading " + path  + " " + name + " from the current shaderpack", e);
            return null;
        }
    }
}
