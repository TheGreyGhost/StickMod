package stickmod.levelnumbering;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import stickmod.reachextender.AttackMessageHandlerOnServer;
import stickmod.reachextender.AttackMessageToServer;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 * The Startup classes for this example are called during startup, in the following order:
 *  preInitCommon
 *  preInitClientOnly
 *  initCommon
 *  initClientOnly
 *  postInitCommon
 *  postInitClientOnly
 *  See MinecraftByExample class for more information
 */
public class StartupCommon {
  public static void preInitCommon()
  {
//    MinecraftForge.EVENT_BUS.register(new LivingAttackEventDebugger());
  }

  public static void initCommon()
  {
  }

  public static void postInitCommon()
  {
  }
}
