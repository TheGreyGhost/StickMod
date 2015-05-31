package stickmod.reachextender;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stickmod.items.ItemStick;
import stickmod.items.SlayEntityHandler;

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
  static SimpleNetworkWrapper simpleNetworkWrapper;
  public static final byte ATTACK_MESSAGE_ID = 35;      // a unique ID for this message type.  It helps detect errors if you don't use zero!

  public static void preInitCommon()
  {
    simpleNetworkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("StickModChannel");
    simpleNetworkWrapper.registerMessage(AttackMessageHandlerOnServer.class, AttackMessageToServer.class,
                                         ATTACK_MESSAGE_ID, Side.SERVER);
  }

  public static void initCommon()
  {
  }

  public static void postInitCommon()
  {
  }
}
