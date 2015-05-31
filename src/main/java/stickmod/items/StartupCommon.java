package stickmod.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 * The Startup classes for this example are called during startup, in the following order:
 *  preInitCommon
 *  preInitClientOnly
 *  initCommon
 *  initClientOnly
 *  postInitCommon
 *  postInitClientOnly
 *  See MinecraftByExample class for more information
 */
public class StartupCommon
{
  public static CreativeTabs stickmodTab;

  public static ItemStick itemLongStick;
  public static ItemStick itemExtraLongStick;
  public static ItemStick itemFatStick;
  public static ItemStick itemCoalStick;
  public static ItemStick itemStoneStick;
  public static ItemStick itemIronStick;
  public static ItemStick itemGoldStick;
  public static ItemStick itemDiamondStick;
  public static ItemStick itemEmeraldStick;

  public static void preInitCommon()
  {
    stickmodTab = new CreativeTabs("stickmod_tab") {
      @Override
      @SideOnly(Side.CLIENT)
      public Item getTabIconItem() {
        return Items.stick;
      }
    };

    itemLongStick = (ItemStick)(new ItemStick(stickmodTab, ItemStick.StickType.LONG_STICK).setUnlocalizedName("item_long_stick"));
    itemExtraLongStick = (ItemStick)(new ItemStick(stickmodTab, ItemStick.StickType.EXTRA_LONG_STICK).setUnlocalizedName("item_extra_long_stick"));
    itemFatStick = (ItemStick)(new ItemStick(stickmodTab, ItemStick.StickType.FAT_STICK).setUnlocalizedName("item_fat_stick"));
    itemCoalStick = (ItemStick)(new ItemStick(stickmodTab, ItemStick.StickType.COAL_STICK).setUnlocalizedName("item_coal_stick"));
    itemStoneStick = (ItemStick)(new ItemStick(stickmodTab, ItemStick.StickType.STONE_STICK).setUnlocalizedName("item_stone_stick"));
    itemIronStick = (ItemStick)(new ItemStick(stickmodTab, ItemStick.StickType.IRON_STICK).setUnlocalizedName("item_iron_stick"));
    itemGoldStick = (ItemStick)(new ItemStick(stickmodTab, ItemStick.StickType.GOLD_STICK).setUnlocalizedName("item_gold_stick"));
    itemDiamondStick = (ItemStick)(new ItemStick(stickmodTab, ItemStick.StickType.DIAMOND_STICK).setUnlocalizedName("item_diamond_stick"));
    itemEmeraldStick = (ItemStick)(new ItemStick(stickmodTab, ItemStick.StickType.EMERALD_STICK).setUnlocalizedName("item_emerald_stick"));

    GameRegistry.registerItem(itemLongStick, "item_long_stick");
    GameRegistry.registerItem(itemExtraLongStick, "item_extra_long_stick");
    GameRegistry.registerItem(itemFatStick, "item_fat_stick");
    GameRegistry.registerItem(itemCoalStick, "item_coal_stick");
    GameRegistry.registerItem(itemStoneStick, "item_stone_stick");
    GameRegistry.registerItem(itemIronStick, "item_iron_stick");
    GameRegistry.registerItem(itemGoldStick, "item_gold_stick");
    GameRegistry.registerItem(itemDiamondStick, "item_diamond_stick");
    GameRegistry.registerItem(itemEmeraldStick, "item_emerald_stick");
  }

  public static void initCommon()
  {
    GameRegistry.addRecipe(new ItemStack(itemLongStick), new Object[]{
            ".S.",
            ".S.",
            ".S.",
            'S', Items.stick
    });
    GameRegistry.addRecipe(new ItemStack(itemExtraLongStick), new Object[]{
            ".S.",
            ".S.",
            ".S.",
            'S', itemLongStick
    });
    GameRegistry.addRecipe(new ItemStack(itemFatStick), new Object[]{
            "...",
            "SSS",
            "...",
            'S', Items.stick
    });
    GameRegistry.addRecipe(new ItemStack(itemCoalStick), new Object[]{
            "CCC",
            "CSC",
            "CCC",
            'S', itemFatStick,
            'C', Items.coal
    });
    GameRegistry.addRecipe(new ItemStack(itemStoneStick), new Object[]{
            "CCC",
            "CSC",
            "CCC",
            'S', itemFatStick,
            'C', Blocks.stone
    });
    GameRegistry.addRecipe(new ItemStack(itemIronStick), new Object[]{
            "CCC",
            "CSC",
            "CCC",
            'S', itemStoneStick,
            'C', Items.iron_ingot
    });
    GameRegistry.addRecipe(new ItemStack(itemGoldStick), new Object[]{
            "CCC",
            "CSC",
            "CCC",
            'S', itemIronStick,
            'C', Items.gold_ingot
    });
    GameRegistry.addRecipe(new ItemStack(itemDiamondStick), new Object[]{
            "CCC",
            "CSC",
            "CCC",
            'S', itemGoldStick,
            'C', Items.diamond
    });
    GameRegistry.addRecipe(new ItemStack(itemEmeraldStick), new Object[]{
            "CCC",
            "CSC",
            "CCC",
            'S', itemDiamondStick,
            'C', Items.emerald
    });

    MinecraftForge.EVENT_BUS.register(new SlayEntityHandler());
  }

  public static void postInitCommon()
  {
  }
}
