package stickmod.reachextender;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stickmod.items.ItemStick;
import stickmod.usefultools.UsefulFunctions;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Hijack various parts of PlayerControllerMP to extend the block reach distance
 */
@SideOnly(Side.CLIENT)
public class PlayerControllerMPHijack extends PlayerControllerMP {
  private static Method syncCurrentPlayItemMethod;

  public static PlayerControllerMPHijack createFromVanilla(PlayerControllerMP playerControllerMP)
  {
    NetHandlerPlayClient netHandlerPlayClient = ReflectionHelper.getPrivateValue
            (PlayerControllerMP.class, playerControllerMP, "netClientHandler", "field_78774_b");

    PlayerControllerMPHijack retObj = new PlayerControllerMPHijack(Minecraft.getMinecraft(), netHandlerPlayClient);

    WorldSettings.GameType gameType = ReflectionHelper.getPrivateValue(PlayerControllerMP.class, playerControllerMP,
                                                                       "currentGameType", "field_78779_k");

    ReflectionHelper.setPrivateValue(PlayerControllerMP.class, retObj, gameType, "currentGameType", "field_78779_k");
    syncCurrentPlayItemMethod = UsefulFunctions.findMethod(PlayerControllerMP.class,
                                                           new String [] {"syncCurrentPlayItem", "func_78750_j"},
                                                           new Class [] {});
    return retObj;
  }

  private PlayerControllerMPHijack(Minecraft mcIn, NetHandlerPlayClient netHandlerPlayClient)
  {
    super(mcIn, netHandlerPlayClient);
  }

  @Override
  public float getBlockReachDistance()
  {
    EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
    if (player == null) return super.getBlockReachDistance();
    ItemStack heldItem = player.getHeldItem();
    if (heldItem != null && heldItem.getItem() instanceof ItemStick) {
      ItemStick itemStick = (ItemStick)heldItem.getItem();
      return itemStick.getReachDistance(heldItem, super.getBlockReachDistance());
    } else {
      return super.getBlockReachDistance();
    }
  }

  // copied from base class
  @Override
  public void attackEntity(EntityPlayer playerIn, Entity targetEntity)
  {
    try {
      syncCurrentPlayItemMethod.invoke(this);
    } catch (Exception e) {
      throw new RuntimeException("Could not invoke syncCurrentPlayItem()", e);
    }
//    this.syncCurrentPlayItem();
    AttackMessageToServer attackMessageToServer = new AttackMessageToServer(targetEntity);
    StartupCommon.simpleNetworkWrapper.sendToServer(attackMessageToServer);
    if (getCurrentGameType() != WorldSettings.GameType.SPECTATOR)
    {
      playerIn.attackTargetEntityWithCurrentItem(targetEntity);
    }
  }
}
