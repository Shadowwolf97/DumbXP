package com.shdwlf.dumbxp;

import com.turt2live.commonsense.DumbPlugin;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.scheduler.BukkitTask;

import java.io.IOException;
import java.util.ArrayList;

public class DumbXP extends DumbPlugin {

    private ArrayList<String> guiOpen; //List of player names that are in the GUI
    private ItemStack[] guiArray; //Store the itemstacks that we will want to put in the GUI
    private DumbXP plugin;
    private InventoryManager inventoryManager;

    private XPBank bank;

    private final int autosave = 200;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        plugin = this;

        inventoryManager = new InventoryManager(this);
        bank = new XPBank(this, "players.yml");

        getServer().getPluginManager().registerEvents(inventoryManager, this);

        Runnable r = new Runnable() {
            @Override
            public void run() {
                System.out.println("[DumbXP] Performing Auto Save");
                try {
                    bank.saveFile();
                } catch (IOException e) {
                    System.out.println("[DumbXP] Error Saving Player Data");
                    e.printStackTrace();
                }
            }
        };

        getServer().getScheduler().scheduleSyncRepeatingTask(this, r, autosave, autosave);
        // initCommonSense(72073);
    }

    @Override
    public void onDisable() {
        try {
            bank.saveFile();
        } catch (IOException e) {
            System.out.println("[DumbXP] Error Saving Player Data");
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("dumbxp")) {
            if(args.length > 0) {
                if(args[0].equalsIgnoreCase("reload")) {
                    if(sender.hasPermission("dumbxp.reload")) {
                        reloadConfig();
                        sendMessage(sender, ChatColor.GREEN + "Configuration reloaded.");
                        return true;
                    }else {
                        sendMessage(sender, ChatColor.RED + "No Permission.");
                        return true;
                    }
                }
                return false;
            }else if(sender instanceof Player) {
                if(sender.hasPermission("dumbxp.use")) {
                    inventoryManager.openGui((Player) sender);
                    return true;
                }else {
                    sendMessage(sender, ChatColor.RED + "No Permission");
                    return true;
                }
            }
        }

        return false;
    }

    void sendMessage(CommandSender player, String message) {
        String prefix = getConfig().getString("prefix", "&7[DumbXP]");
        prefix = ChatColor.translateAlternateColorCodes('&', prefix);

        if (prefix.length() > 0 && !prefix.endsWith(" ")) prefix += " ";

        player.sendMessage(prefix + message);
    }

    public XPBank getBank() {
        return bank;
    }

}
