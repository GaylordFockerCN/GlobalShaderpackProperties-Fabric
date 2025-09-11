package com.p1nero.globalshaderpackproperties.client.mixin;

import com.p1nero.globalshaderpackproperties.client.GlobalShaderpackPropertiesMod;
import net.irisshaders.iris.config.IrisConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IrisConfig.class)
public class IrisConfigMixin {

    @Shadow(remap = false)
    private String shaderPackName;

    @Inject(method = "setShaderPackName", at = @At("TAIL"), remap = false)
    private void globalshadersproperties$setShaderPackName(String name, CallbackInfo ci) {
        if(this.shaderPackName != null) {
            GlobalShaderpackPropertiesMod.reloadCurrentPropertiesNameCache(GlobalShaderpackPropertiesMod.removeZipSuffix(this.shaderPackName));
            GlobalShaderpackPropertiesMod.reloadGlobalPropertiesNameCache();
        }
    }

    @Inject(method = "load", at = @At("TAIL"), remap = false)
    private void globalshadersproperties$setShaderPackName(CallbackInfo ci) {
        if(this.shaderPackName != null) {
            GlobalShaderpackPropertiesMod.reloadCurrentPropertiesNameCache(GlobalShaderpackPropertiesMod.removeZipSuffix(this.shaderPackName));
            GlobalShaderpackPropertiesMod.reloadGlobalPropertiesNameCache();
        }
    }
}
