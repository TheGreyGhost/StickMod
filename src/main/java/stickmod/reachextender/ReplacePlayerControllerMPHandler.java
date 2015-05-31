package stickmod.reachextender;

/**
 * Created by EveryoneElse on 31/05/2015.
 */

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ReplacePlayerControllerMPHandler
{
  @SubscribeEvent
  public void worldLoadEvent(WorldEvent.Load event) {
    if (event.world instanceof WorldClient) {
      Minecraft mc = Minecraft.getMinecraft();
      PlayerControllerMP playerControllerMP = mc.playerController;
      if (!(playerControllerMP instanceof PlayerControllerMPHijack)) {
        mc.playerController = PlayerControllerMPHijack.createFromVanilla(playerControllerMP);
      }
    }
  }
}
