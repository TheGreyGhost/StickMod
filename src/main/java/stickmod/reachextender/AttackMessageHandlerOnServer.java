package stickmod.reachextender;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import stickmod.items.ItemStick;

import java.util.Random;

/**
 * The MessageHandlerOnServer is used to process the network message once it has arrived on the Server side.
 * WARNING!  In 1.8 the MessageHandler now runs in its own thread.  This means that if your onMessage code
 * calls any vanilla objects, it may cause crashes or subtle problems that are hard to reproduce.
 * Your onMessage handler should create a task which is later executed by the client or server thread as
 * appropriate - see below.
 * User: The Grey Ghost
 * Date: 15/01/2015
 */
public class AttackMessageHandlerOnServer implements IMessageHandler<AttackMessageToServer, IMessage>
{
  /**
   * Called when a message is received of the appropriate type.
   * CALLED BY THE NETWORK THREAD
   * @param message The message
   */
  public IMessage onMessage(final AttackMessageToServer message, MessageContext ctx) {
    if (ctx.side != Side.SERVER) {
      System.err.println("AttackMessageToServer received on wrong side:" + ctx.side);
      return null;
    }
    if (!message.isMessageValid()) {
      System.err.println("AttackMessageToServer was invalid" + message.toString());
      return null;
    }

    // we know for sure that this handler is only used on the server side, so it is ok to assume
    //  that the ctx handler is a serverhandler, and that WorldServer exists.
    // Packets received on the client side must be handled differently!  See MessageHandlerOnClient

    final EntityPlayerMP sendingPlayer = ctx.getServerHandler().playerEntity;
    if (sendingPlayer == null) {
      System.err.println("EntityPlayerMP was null when AttackMessageToServer was received");
      return null;
    }

    // This code creates a new task which will be executed by the server during the next tick,
    //  for example see MinecraftServer.updateTimeLightAndEntities(), just under section
    //      this.theProfiler.startSection("jobs");
    //  In this case, the task is to call messageHandlerOnServer.processMessage(message, sendingPlayer)
    final WorldServer playerWorldServer = sendingPlayer.getServerForPlayer();
    playerWorldServer.addScheduledTask(new Runnable() {
      public void run() {
        processMessage(message, sendingPlayer);
      }
    });

    return null;
  }

  // This message is called from the Server thread.
  void processMessage(AttackMessageToServer message, EntityPlayerMP sendingPlayer)
  {
    WorldServer worldServer = (WorldServer)sendingPlayer.worldObj;
    Entity entity = worldServer.getEntityByID(message.getEntityID());
    sendingPlayer.markPlayerActive();

    if (entity != null) {
      float reachDistance = 6.0F;  // default
      ItemStack heldItem = sendingPlayer.getHeldItem();
      if (heldItem != null && heldItem.getItem() instanceof ItemStick) {
        ItemStick itemStick = (ItemStick)heldItem.getItem();
        reachDistance = itemStick.getReachDistance(heldItem, reachDistance);
      }

      if (!sendingPlayer.canEntityBeSeen(entity)) {
        reachDistance /= 2.0;
      }

//      System.out.println("custom attack at distance:" + reachDistance);
      if (sendingPlayer.getDistanceSqToEntity(entity) < reachDistance * reachDistance) {
         if (entity instanceof EntityItem || entity instanceof EntityXPOrb
             || entity instanceof EntityArrow || entity == sendingPlayer) {
            System.err.println("Attempted to attack an invalid entity:" + entity);
          } else {
           sendingPlayer.attackTargetEntityWithCurrentItem(entity);
         }
      }
    }
    return;
  }
}
