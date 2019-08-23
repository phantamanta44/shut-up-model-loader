package xyz.phanta.shutupmodelloader;

import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.core.Logger;

@Mod(modid = ShutUpModelLoader.MOD_ID, version = ShutUpModelLoader.VERSION, useMetadata = true)
public class ShutUpModelLoader {

    public static final String MOD_ID = "shutupmodelloader";
    public static final String VERSION = "1.0.0";

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        injectFilter(FMLLog.log, (m, t) -> t instanceof ModelLoaderRegistry.LoaderException
                || (m != null && (m.endsWith("(no base model or parts were provided/resolved)")
                || m.startsWith("Exception loading blockstate for the variant"))));
        injectFilter(ModelBlock.LOGGER, (m, t) -> m != null && m.startsWith("Unable to resolve texture due to upward reference"));
    }

    private static void injectFilter(org.apache.logging.log4j.Logger logger, ShutUpFilter.Matcher filter) {
        if (logger instanceof Logger) {
            injectFilter0((Logger)logger, filter);
            logger.info("Successfully shut up logger {}.", logger);
        } else {
            logger.error("Could not shut up logger {}, instance of {}", logger, logger.getClass().getCanonicalName());
        }
    }

    private static void injectFilter0(Logger logger, ShutUpFilter.Matcher filter) {
        logger.addFilter(new ShutUpFilter(filter));
    }

}
