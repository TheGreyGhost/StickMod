package stickmod.levelnumbering;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by EveryoneElse on 7/06/2015.
 */
public class LivingAttackEventDebugger {

  @SubscribeEvent
  public void livingAttackEventHandler(LivingAttackEvent event) {
    System.out.println(event.source.getDamageType() + " :" + event.ammount);
  }
}
