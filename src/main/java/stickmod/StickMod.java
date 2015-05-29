package stickmod;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/*
  All kinds of sticks...
 */

@Mod(modid = StickMod.MODID, version = StickMod.VERSION)
//,     guiFactory= StickMod.GUIFACTORY)  //delete guiFactory if MBE70 not present and you don't have a configuration GUI
public class StickMod
{
    public static final String MODID = "stickmod";
    public static final String VERSION = "0.1";
//    public static final String GUIFACTORY = "stickmod.mbe70_configuration.MBEGuiFactory"; //delete if MBE70 not present

    // The instance of your mod that Forge uses.  Optional.
    @Mod.Instance(StickMod.MODID)
    public static StickMod instance;

    // Says where the client and server 'proxy' code is loaded.
    @SidedProxy(clientSide="stickmod.ClientOnlyProxy", serverSide="stickmod.DedicatedServerProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
      proxy.preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
      proxy.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
      proxy.postInit();
    }

    /**
     * Prepend the name with the mod ID, suitable for ResourceLocations such as textures.
     * @param name
     * @return eg "stickmod:myblockname"
     */
    public static String prependModID(String name) {return MODID + ":" + name;}
}
