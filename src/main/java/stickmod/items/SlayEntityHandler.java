package stickmod.items;

import com.google.common.base.Throwables;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntityDamageSource;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import stickmod.usefultools.UsefulFunctions;

import java.lang.reflect.Method;

/**
 * Created by EveryoneElse on 31/05/2015.
 */
public class SlayEntityHandler {

  @SubscribeEvent
  public void livingDeathEvent(LivingDeathEvent event)
  {
    if (event.source instanceof EntityDamageSource) {
      EntityDamageSource source = (EntityDamageSource)event.source;
      if (source.getSourceOfDamage() instanceof EntityPlayer) {
        EntityPlayer player = (EntityPlayer)source.getSourceOfDamage();
        ItemStack heldItem = player.getHeldItem();
        if (heldItem != null && heldItem.getItem() instanceof ItemStick) {
          ItemStick heldStick = (ItemStick)heldItem.getItem();
          heldStick.addXP(heldItem, getXPForKill(event.entityLiving, player));
        }
      }
    }
  }

  public int getXPForKill(EntityLivingBase killedEntity, EntityPlayer entityPlayer)
  {
    if (getExperiencePointsMethod == null) {
      getExperiencePointsMethod = UsefulFunctions.findMethod(EntityLivingBase.class,
                                                             new String[]{"getExperiencePoints", "func_70693_a"},
                                                             new Class[]{EntityPlayer.class});
    }

    try {
      return (Integer)getExperiencePointsMethod.invoke(killedEntity, entityPlayer);
    } catch (Exception e) {
      Throwables.propagate(e);
    }
    return 0;
  }

  private static Method getExperiencePointsMethod;
}
