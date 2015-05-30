package stickmod.reachextender;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;

/**
 * Hijack various parts of PlayerControllerMP to extend the block reach distance
 */
public class PlayerControllerMPHijack extends PlayerControllerMP {

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
}
