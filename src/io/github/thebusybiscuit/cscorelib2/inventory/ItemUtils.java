package io.github.thebusybiscuit.cscorelib2.inventory;

import java.lang.reflect.Method;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import io.github.thebusybiscuit.cscorelib2.reflection.ReflectionUtils;

public final class ItemUtils {
	
	private ItemUtils() {}
	
	private static Method copy, getName, toString;
	
	static {
		try {
			copy = ReflectionUtils.getOBCClass("inventory.CraftItemStack").getMethod("asNMSCopy", ItemStack.class);
			getName = ReflectionUtils.getMethod(ReflectionUtils.getNMSClass("ItemStack"), "getName");
			toString = ReflectionUtils.getMethod(ReflectionUtils.getNMSClass("IChatBaseComponent"), "getString");
		}
		catch(Exception x) {
			x.printStackTrace();
		}
	}
	
	/**
	 * This method returns a human-readable version of this item's name.
	 * If the specified {@link ItemStack} has a Custom Display Name, it will return that.
	 * Otherwise it will return the english name of it's {@link Material}
	 * 
	 * @param item	The Item to format
	 * @return		The formatted Item Name
	 */
	public static String getFormattedItemName(ItemStack item) {
		if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
			return item.getItemMeta().getDisplayName();
		}
		
		try {
			Object instance = copy.invoke(null, item);
			return (String) toString.invoke(getName.invoke(instance));
		} catch (Exception e) {
			e.printStackTrace();
			return "ERROR";
		}
	}
	
	/**
	 * This method compares two instances of {@link ItemStack} and checks
	 * whether their {@link Material} and {@link ItemMeta} match.
	 * 
	 * @param a	{@link ItemStack} One
	 * @param b {@link ItemStack} Two
	 * @return	Whether the two instances of {@link ItemStack} are similiar and can be stacked.
	 */
	public static boolean canStack(ItemStack a, ItemStack b) {
		if (a == null || b == null) return false;
		
		if (!a.getType().equals(b.getType())) return false;
		if (a.hasItemMeta() != b.hasItemMeta()) return false;
		
		if (a.hasItemMeta()) {
			ItemMeta aMeta = a.getItemMeta(), bMeta = b.getItemMeta();
			
			if (aMeta instanceof Damageable != bMeta instanceof Damageable) return false;
			if (aMeta instanceof Damageable) {
				if (((Damageable) aMeta).getDamage() != ((Damageable) bMeta).getDamage()) return false;
			}

			if (aMeta instanceof LeatherArmorMeta != bMeta instanceof LeatherArmorMeta) return false;
			if (aMeta instanceof LeatherArmorMeta) {
				if (!((LeatherArmorMeta) aMeta).getColor().equals(((LeatherArmorMeta) bMeta).getColor())) return false;
			}
			
			if (aMeta.getCustomModelData() != bMeta.getCustomModelData()) return true;
			if (!aMeta.getEnchants().equals(bMeta.getEnchants())) return false;
			
			if (aMeta.hasDisplayName() != bMeta.hasDisplayName()) return false;
			if (aMeta.hasDisplayName()) {
				if (!aMeta.getDisplayName().equals(bMeta.getDisplayName())) return false;
			}

			if (aMeta.hasLore() != bMeta.hasLore()) return false;
			if (aMeta.hasLore()) {
				if (aMeta.getLore().size() != bMeta.getLore().size()) return false;
				
				for (int i = 0; i < aMeta.getLore().size(); i++) {
					if (!aMeta.getLore().get(i).equals(bMeta.getLore().get(i))) return false;
				}
			}
		}
		
		return true;
	}

}
