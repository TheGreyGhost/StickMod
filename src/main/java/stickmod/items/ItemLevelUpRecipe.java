package stickmod.items;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;


/**
 * Created by TGG on 12/06/2015.
 */
public class ItemLevelUpRecipe implements IRecipe {

    // a match if the table has one emerald stick and only emeralds in other slots
    public boolean matches(InventoryCrafting inventoryCrafting, World worldIn)
    {
        ItemStack stack = null;
        int emeraldCount = 0;
        for (int i = 0; i < inventoryCrafting.getSizeInventory(); ++i) {
          ItemStack itemstack = inventoryCrafting.getStackInSlot(i);
          if (itemstack != null) {
            if (itemstack.getItem() == StartupCommon.itemEmeraldStick) {
              if (stack != null) return false;
              stack = itemstack;
            } else if (itemstack.getItem() == Items.emerald) {
              emeraldCount += itemstack.stackSize;
            } else {
              return false;
            }
          }
        }
        if (stack == null || emeraldCount == 0) return false;
        ItemStick itemStick = (ItemStick)stack.getItem();

        int level = itemStick.getLevelAndRemainderXP(stack).getLeft();
        if (itemStick.xpToReachNextLevel(level) == 0) return false; // at max level
        return true;
    }

    public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting)
    {
        ItemStack stack = null;
        int emeraldCount = 0;
        for (int i = 0; i < inventoryCrafting.getSizeInventory(); ++i) {
            ItemStack itemstack = inventoryCrafting.getStackInSlot(i);
            if (itemstack != null) {
                if (itemstack.getItem() == StartupCommon.itemEmeraldStick) {
                    if (stack != null) return null;
                    stack = itemstack;
                } else if (itemstack.getItem() == Items.emerald) {
                    emeraldCount += itemstack.stackSize;
                } else {
                    return null;
                }
            }
        }
        if (stack == null || emeraldCount == 0) return null;

        ItemStack newStack = stack.copy();
        ItemStick itemStick = (ItemStick)stack.getItem();

        while (emeraldCount > 0) {
            Pair<Integer, Integer> levelAndXP = itemStick.getLevelAndRemainderXP(newStack);
            int level = levelAndXP.getLeft();
            int xpRemainder = levelAndXP.getRight();
            if (itemStick.xpToReachNextLevel(level) == 0) break;

            int xpToGo = itemStick.xpToReachNextLevel(level) - xpRemainder;
            int emeraldsNeeded = level * xpToGo / itemStick.xpToReachNextLevel(level);
            if (emeraldCount >= emeraldsNeeded) {
              emeraldCount -= emeraldsNeeded;
              itemStick.addXP(newStack, xpToGo);
            } else {
              int xpGained = itemStick.xpToReachNextLevel(level) * emeraldCount / level;
              itemStick.addXP(newStack, xpGained);
              emeraldCount = 0;
            }
        }

        return newStack;
    }

    public int getRecipeSize()
    {
        return 9;
    }

    public ItemStack getRecipeOutput()
    {
        return null;
    }

    // assumes this is called immediately before item creation, so remove all emeralds (otherwise, only 1 gets removed
    //  per slot
    public ItemStack[] getRemainingItems(InventoryCrafting inventoryCrafting)
    {
        ItemStack[] aitemstack = new ItemStack[inventoryCrafting.getSizeInventory()];

        for (int i = 0; i < aitemstack.length; ++i)
        {
            aitemstack[i] = null;
        }

        inventoryCrafting.clear();
        return aitemstack;
    }


}
