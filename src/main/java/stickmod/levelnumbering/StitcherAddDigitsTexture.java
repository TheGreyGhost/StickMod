package stickmod.levelnumbering;

/**
 * Created by EveryoneElse on 31/05/2015.
 */

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import stickmod.reachextender.PlayerControllerMPHijack;

public class StitcherAddDigitsTexture
{
  @SubscribeEvent
  public void stitcherEventPre(TextureStitchEvent.Pre event) {
    ResourceLocation digits = new ResourceLocation("stickmod:items/digits");
    event.map.registerSprite(digits);
  }
}
