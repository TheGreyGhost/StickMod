package stickmod.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.util.Color;

import java.util.List;

/**
 * User: The Grey Ghost
 * Date: 29/05/2015

 */
public class ItemStick extends ItemSword
{
  public ItemStick(CreativeTabs whichTab, StickType i_stickType)
  {
    super(i_stickType.getMaterial());
    stickType = i_stickType;
    this.setCreativeTab(whichTab);
    this.setMaxDamage(0);
  }

  public enum StickType {
    LONG_STICK(ToolMaterial.WOOD, 1.0F, false),
    EXTRA_LONG_STICK(ToolMaterial.WOOD, 2.0F, false),
    FAT_STICK(ToolMaterial.WOOD, -1.0F, false),
    COAL_STICK(ToolMaterial.WOOD, 0.0F, false),
    STONE_STICK(ToolMaterial.STONE, 0.0F, false),
    IRON_STICK(ToolMaterial.IRON, 0.0F, false),
    GOLD_STICK(ToolMaterial.GOLD, 0.0F, false),
    DIAMOND_STICK(ToolMaterial.EMERALD, 0.0F, false),
    EMERALD_STICK(ToolMaterial.EMERALD, 0.0F, true);

    StickType(Item.ToolMaterial i_material, float i_reachDistanceIncrease, boolean i_hasXP) {
      material = i_material; reachDistanceIncrease = i_reachDistanceIncrease; hasXP = i_hasXP;
    }

    public Item.ToolMaterial getMaterial() {return material;}
    public float getReachDistanceIncrease() {return reachDistanceIncrease;}
    private Item.ToolMaterial material;
    private float reachDistanceIncrease;
    private boolean hasXP;
  }

  static private final String XP_TAG = "XP";
  static public final int DOESNT_HAVE_XP = -1;
  static private final int MAXIMUM_LEVEL = 150;

  // return the distance in blocks that the stick can reach as a weapon
  public float getReachDistance(ItemStack itemStack, float defaultReach)
  {
    if (itemStack == null || !(itemStack.getItem() instanceof ItemStick)) {
      return defaultReach;
    }
    ItemStick itemStick = (ItemStick)(itemStack.getItem());
    return defaultReach + itemStick.stickType.getReachDistanceIncrease();
  }

  @Override
  public boolean showDurabilityBar(ItemStack stack) {
//    if (stack != null && stack.getItem() instanceof ItemStick) {
//      ItemStick itemStick = (ItemStick)stack.getItem();
//      return itemStick.stickType.hasXP;
//    }
    return false;
  }

  @Override
  public double getDurabilityForDisplay(ItemStack stack)
  {
    Pair<Integer, Integer> levelAndXP = getLevelAndRemainderXP(stack);
    if (levelAndXP.getLeft() == DOESNT_HAVE_XP) return 0;
    return 1.0 - levelAndXP.getRight() / (double)xpToReachNextLevel(levelAndXP.getLeft());
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced)
  {
    Pair<Integer, Integer> levelAndXP = getLevelAndRemainderXP(stack);
    if (levelAndXP.getLeft() == DOESNT_HAVE_XP) return;
    tooltip.add("Level " + levelAndXP.getLeft() + ", XP till next:"
                         + (xpToReachNextLevel(levelAndXP.getLeft()) - levelAndXP.getRight())  );
  }


  /**
   * gets the experience level of this itemstack
   * @param itemStack
   * @return the level (first) and remainder xp (second), or DOESNT_HAVE_XP for item which has no levels
   */
  public Pair<Integer, Integer> getLevelAndRemainderXP(ItemStack itemStack)
  {
    int xpLeft = getXP(itemStack);
    if (xpLeft == DOESNT_HAVE_XP) return new ImmutablePair<Integer, Integer>(DOESNT_HAVE_XP, DOESNT_HAVE_XP);

    int level = 1;
    do {
      int xpRequiredForNextLevel = xpToReachNextLevel(level);
      if (xpLeft < xpRequiredForNextLevel) break;
      xpLeft -= xpRequiredForNextLevel;
      ++level;
    } while (level < MAXIMUM_LEVEL);

    return new ImmutablePair<Integer, Integer>(level, xpLeft);
  }

  /** the experience points required to raise from the given level to the next
   *   - same as for player
   * @param level
   * @return xp required to reach the next level from the start of the given level
   */
  public int xpToReachNextLevel(int level)
  {
    if (level >= 30) {
      return 112 + (level - 30) * 9;
    } else if (level >= 15) {
        return 37 + (level - 15) * 5;
    } else {
      return 7 + level * 2;
    }
  }

  /**
   * gets the experience points (XP) of this itemstack
   * @param itemStack
   * @return the XP, or DOESNT_HAVE_XP for items which has no XP
   */
  public int getXP(ItemStack itemStack)
  {
    if (itemStack == null) return DOESNT_HAVE_XP;
    if (!(itemStack.getItem() instanceof ItemStick)) {
      return DOESNT_HAVE_XP;
    }
    ItemStick stick = (ItemStick)itemStack.getItem();
    if (!stick.stickType.hasXP) return DOESNT_HAVE_XP;
    NBTTagCompound nbtTagCompound = itemStack.getTagCompound();
    if (nbtTagCompound == null) return 0;
    int xp = nbtTagCompound.getInteger(XP_TAG);  // default to 0 if key is wrong type or doesn't exist yet
    return xp;
  }

  /**
   * sets the experience points (XP) of this itemstack
   * @param itemStack
   * @param xp the XP to set for the itemstack
   * @return the XP, or DOESNT_HAVE_XP for items which has no XP
   */
  public void setXP(ItemStack itemStack, int xp)
  {
    if (itemStack == null) return;
    if (!(itemStack.getItem() instanceof ItemStick)) return;

    NBTTagCompound nbtTagCompound = itemStack.getTagCompound();
    if (nbtTagCompound == null) {
      nbtTagCompound = new NBTTagCompound();
      itemStack.setTagCompound(nbtTagCompound);
    }
    nbtTagCompound.setInteger(XP_TAG, xp);
  }

  /**
   * increases the experience points (XP) of this itemstack
   * @param itemStack
   * @param xpToAdd the number of experience points to add
   * @return the XP, or DOESNT_HAVE_XP for items which has no XP
   */
  public void addXP(ItemStack itemStack, int xpToAdd)
  {
    int xp = getXP(itemStack);
    if (xp == DOESNT_HAVE_XP) return;
    setXP(itemStack, xp + xpToAdd);
  }

  @Override
  public int getColorFromItemStack(ItemStack stack, int renderPass)
  {
    switch (renderPass) {
      case 0: return java.awt.Color.WHITE.getRGB();
      case 1: return 0;
      case 2: return java.awt.Color.GREEN.getRGB();
      default:return java.awt.Color.RED.getRGB();
    }
  }

  private StickType stickType;
}
