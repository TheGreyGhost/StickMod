package stickmod.items;

import com.sun.istack.internal.NotNull;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;

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

  private StickType stickType;
}
