package stickmod.levelnumbering;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import stickmod.reachextender.ReplacePlayerControllerMPHandler;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 */
public class StartupClientOnly
{
  public static void preInitClientOnly()
  {
    MinecraftForge.EVENT_BUS.register(new StitcherAddDigitsTexture());
    ModelResourceLocation emeraldItemMRL = new ModelResourceLocation("stickmod:item_emerald_stick", "inventory");
    MinecraftForge.EVENT_BUS.register(new ModelBakeEventHandler(emeraldItemMRL));
  }

  public static void initClientOnly()
  {
  }

  public static void postInitClientOnly()
  {
  }
}
