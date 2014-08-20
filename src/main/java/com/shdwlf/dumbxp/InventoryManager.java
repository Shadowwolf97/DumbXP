package com.shdwlf.dumbxp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class InventoryManager implements Listener {

    private DumbXP plugin;
    private ArrayList<String> guiOpen;

    public InventoryManager(DumbXP plugin) {
        this.plugin = plugin;
        guiOpen = new ArrayList<String>();
    }

    public void openGui(Player player) {
        Inventory inv = Bukkit.createInventory(player, 9, ChatColor.BLUE + "DumbXP");
        ItemStack is = new ItemStack(Material.WOOL);
        is.setDurability((short) 5);
        ItemMeta im = is.getItemMeta();
        List<String> lore = new ArrayList<String>();
        lore.add(ChatColor.GRAY + "Click to deposit 1 level");
        lore.add(ChatColor.GRAY + "Shift Click to deposit 5 levels");
        im.setLore(lore);
        im.setDisplayName(ChatColor.GREEN + "Deposit");
        is.setItemMeta(im);
        inv.setItem(3, is);
        is = new ItemStack(Material.WOOL);
        is.setDurability((short) 14);
        im = is.getItemMeta();
        lore = new ArrayList<String>();
        lore.add(ChatColor.GRAY + "Click to withdrawal 1 level");
        lore.add(ChatColor.GRAY + "Shift Click to withdrawal 5 levels");
        im.setDisplayName(ChatColor.RED + "Withdrawal");
        im.setLore(lore);
        is.setItemMeta(im);
        inv.setItem(5, is);
        inv.setItem(4, createExpBottle(player));
        guiOpen.add(player.getName());
        player.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            if(guiOpen.contains(player.getName())) {
                if(event.getCurrentItem() != null) {
                    if(event.getCurrentItem().getType() == Material.WOOL) {
                        if(event.getCurrentItem().getDurability() == 5) { //Deposit
                            if(event.isShiftClick()) {
                                if(player.getLevel() >= 5) {
                                    plugin.getBank().addLevels(player, 5);
                                    player.setLevel(player.getLevel()-5);
                                    updateExpBottle(player);
                                }
                            } else {
                                if(player.getLevel() >= 1) {
                                    plugin.getBank().addLevels(player, 1);
                                    player.setLevel(player.getLevel()-1);
                                    updateExpBottle(player);
                                }
                            }
                        }else if(event.getCurrentItem().getDurability() == 14) { //Withdrawal
                            if(event.isShiftClick()) {
                                if(plugin.getBank().getLevels(player) >= 5) {
                                    plugin.getBank().removeLevels(player, 5);
                                    player.setLevel(player.getLevel() + 5);
                                    updateExpBottle(player);
                                }
                            } else {
                                if(plugin.getBank().getLevels(player) >= 1) {
                                    plugin.getBank().removeLevels(player, 1);
                                    player.setLevel(player.getLevel() + 1);
                                    updateExpBottle(player);
                                }
                            }
                        }
                    }
                    event.setCancelled(true);
                }
            }
        }
    }

    private void updateExpBottle(Player player) {
        player.getOpenInventory().setItem(4, createExpBottle(player));
    }

    private ItemStack createExpBottle(Player player) {
        ItemStack is = new ItemStack(Material.EXP_BOTTLE);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.GOLD + "DumbXP Bank");
        List<String> lore = new ArrayList<String>();
        lore.add(ChatColor.GRAY + "Current Balance");
        lore.add(ChatColor.AQUA + "" + plugin.getBank().getLevels(player) + ChatColor.GRAY + " Levels");
        im.setLore(lore);
        is.setItemMeta(im);
        return is;
    }

}
