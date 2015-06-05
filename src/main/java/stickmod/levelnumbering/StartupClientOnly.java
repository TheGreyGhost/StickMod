package stickmod.levelnumbering;

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


  }

  public static void initClientOnly()
  {
  }

  public static void postInitClientOnly()
  {
  }
}
