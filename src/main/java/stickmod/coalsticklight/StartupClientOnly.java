package stickmod.coalsticklight;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import stickmod.items.*;
import stickmod.items.StartupCommon;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 */
public class StartupClientOnly
{
  public static void preInitClientOnly()
  {
  }

  public static void initClientOnly()
  {
  }

  public static void postInitClientOnly()
  {
    FMLCommonHandler.instance().bus().register(new ClientTickForLight());
  }
}
