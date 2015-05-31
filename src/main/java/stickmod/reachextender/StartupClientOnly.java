package stickmod.reachextender;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.common.MinecraftForge;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 */
public class StartupClientOnly
{
  public static void preInitClientOnly()
  {
    MinecraftForge.EVENT_BUS.register(new ReplacePlayerControllerMPHandler());
  }

  public static void initClientOnly()
  {
  }

  public static void postInitClientOnly()
  {
  }
}
