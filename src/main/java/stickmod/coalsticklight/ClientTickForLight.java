package stickmod.coalsticklight;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import stickmod.StickMod;
import stickmod.items.StartupCommon;

/**
 * Created by TGG
 */
public class ClientTickForLight {
  @SubscribeEvent
  public void clientTick(TickEvent.ClientTickEvent event)
  {
    if (event.phase != TickEvent.Phase.END) return;


    EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
    boolean lightOn = false;
    if (player != null) {
      ItemStack itemStack = player.getHeldItem();
      if (itemStack != null) {
        if (itemStack.getItem() == StartupCommon.itemCoalStick) {
          lightOn = true;
        }
      }
    }

    FMLInterModComms.sendRuntimeMessage(StickMod.MODID, "DynamicLights_thePlayer",
                                        lightOn ? "forceplayerlighton" : "forceplayerlightoff",
                                        "");
  }
}
