package stickmod.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 */
public class StartupClientOnly
{
  public static void preInitClientOnly()
  {
  }

  public static void initClientOnly()
  {
    final int DEFAULT_ITEM_SUBTYPE = 0;
    ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
    ModelResourceLocation itemModelResourceLocation;
    itemModelResourceLocation = new ModelResourceLocation("stickmod:item_long_stick", "inventory");
    mesher.register(StartupCommon.itemLongStick, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);

    itemModelResourceLocation = new ModelResourceLocation("stickmod:item_extra_long_stick", "inventory");
    mesher.register(StartupCommon.itemExtraLongStick, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);

    itemModelResourceLocation = new ModelResourceLocation("stickmod:item_fat_stick", "inventory");
    mesher.register(StartupCommon.itemFatStick, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);

    itemModelResourceLocation = new ModelResourceLocation("stickmod:item_coal_stick", "inventory");
    mesher.register(StartupCommon.itemCoalStick, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);

    itemModelResourceLocation = new ModelResourceLocation("stickmod:item_stone_stick", "inventory");
    mesher.register(StartupCommon.itemStoneStick, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);

    itemModelResourceLocation = new ModelResourceLocation("stickmod:item_iron_stick", "inventory");
    mesher.register(StartupCommon.itemIronStick, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);

    itemModelResourceLocation = new ModelResourceLocation("stickmod:item_gold_stick", "inventory");
    mesher.register(StartupCommon.itemGoldStick, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);

    itemModelResourceLocation = new ModelResourceLocation("stickmod:item_diamond_stick", "inventory");
    mesher.register(StartupCommon.itemDiamondStick, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);

    itemModelResourceLocation = new ModelResourceLocation("stickmod:item_emerald_stick", "inventory");
    mesher.register(StartupCommon.itemEmeraldStick, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);
  }

  public static void postInitClientOnly()
  {
  }
}
