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

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Hijack various parts of PlayerControllerMP to extend the block reach distance
 */
@SideOnly(Side.CLIENT)
public class PlayerControllerMPHijack extends PlayerControllerMP {
  private static final Method keybindArrayField = ReflectionHelper
          .findMethod(PlayerControllerMP.class, PlayerControllerMPHijack.class, "keybindArray", "field_74516_a");

  public static PlayerControllerMPHijack createFromVanilla(PlayerControllerMP playerControllerMP, NetHandlerPlayClient netHandlerPlayClient)
  {
    PlayerControllerMPHijack retObj = new PlayerControllerMPHijack(Minecraft.getMinecraft(), netHandlerPlayClient);
    retObj.setGameType(playerControllerMP.getCurrentGameType());
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

  @Override
  public void attackEntity(EntityPlayer playerIn, Entity targetEntity)
  {
    this.syncCurrentPlayItem();
    this.netClientHandler.addToSendQueue(new C02PacketUseEntity(targetEntity, C02PacketUseEntity.Action.ATTACK));

    if (this.currentGameType != WorldSettings.GameType.SPECTATOR)
    {
      playerIn.attackTargetEntityWithCurrentItem(targetEntity);
    }
  }


}
