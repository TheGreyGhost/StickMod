package stickmod.items;

import com.sun.istack.internal.NotNull;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;

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
  }

  public enum StickType {
    LONG_STICK(ToolMaterial.WOOD),
    EXTRA_LONG_STICK(ToolMaterial.WOOD),
    FAT_STICK(ToolMaterial.WOOD),
    COAL_STICK(ToolMaterial.WOOD),
    STONE_STICK(ToolMaterial.STONE),
    IRON_STICK(ToolMaterial.IRON),
    GOLD_STICK(ToolMaterial.GOLD),
    DIAMOND_STICK(ToolMaterial.EMERALD),
    EMERALD_STICK(ToolMaterial.EMERALD);

    StickType(Item.ToolMaterial i_material) {
      material = i_material;
    }

    public Item.ToolMaterial getMaterial() {return material;}
    private Item.ToolMaterial material;
  }

  static private final String XP_TAG = "XP";
  static private final int DOESNT_HAVE_XP = -1;
  static private final int MAXIMUM_LEVEL = 150;

  /**
   * gets the experience level of this itemstack
   * @param itemStack
   * @return the level, or DOESNT_HAVE_XP for item which has no levels
   */
  public int getLevel(ItemStack itemStack)
  {
    int xpLeft = getXP(itemStack);
    if (xpLeft == DOESNT_HAVE_XP) return DOESNT_HAVE_XP;

    int level = 1;
    do {
      xpLeft -= xpToReachNextLevel(level);
      if (xpLeft < 0) break;
      ++level;
    } while (level < MAXIMUM_LEVEL);

    return level;
  }

  /** the experience points required to raise from this level to the next
   *   - same as for player
   * @param level
   * @return xp required to reach the next level from the start of the current level
   */
  private int xpToReachNextLevel(int level)
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

  private StickType stickType;
}
