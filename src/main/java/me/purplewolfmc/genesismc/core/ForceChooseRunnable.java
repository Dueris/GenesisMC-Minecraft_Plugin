package me.purplewolfmc.genesismc.core;

import me.purplewolfmc.genesismc.core.files.GenesisDataFiles;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

import static org.bukkit.Bukkit.createInventory;
import static org.bukkit.ChatColor.LIGHT_PURPLE;

public class ForceChooseRunnable extends BukkitRunnable {
    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!p.getScoreboardTags().contains("chosen")) {
                    if (!p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Human") &&! p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Enderian") &&! p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Shulk") &&! p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Arachnid") &&! p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Creep") &&! p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Phantom") &&! p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Slimeling") &&! p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Vexian") &&! p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Blazeborn") &&! p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Starborne") &&! p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Mermaid") &&! p.getOpenInventory().getTitle().equalsIgnoreCase("Allay") &&! p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Rabbit") &&! p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Bumblebee") &&! p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Elytrian") &&! p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Avian") &&! p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Piglin") &&! p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Dragonborne") &&! p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Allay")) {
                            p.setGameMode(GameMode.SURVIVAL);
                        if (GenesisDataFiles.getPlugCon().getString("custom-origins").equalsIgnoreCase("false")) {
                        Inventory allmenu = createInventory(p, 18, LIGHT_PURPLE + "Origins Menu");

                        ItemStack human = new ItemStack(Material.PLAYER_HEAD);
                        ItemStack enderian = new ItemStack(Material.ENDER_PEARL);
                        ItemStack shulker = new ItemStack(Material.SHULKER_SHELL);
                        ItemStack arachnid = new ItemStack(Material.COBWEB);
                        ItemStack creep = new ItemStack(Material.GUNPOWDER);
                        ItemStack phantom = new ItemStack(Material.PHANTOM_MEMBRANE);
                        ItemStack slimeling = new ItemStack(Material.SLIME_BALL);
                        ItemStack vexian = new ItemStack(Material.IRON_SWORD);
                        ItemStack blazeborn = new ItemStack(Material.BLAZE_POWDER);
                        ItemStack starborne = new ItemStack(Material.NETHER_STAR);
                        ItemStack mermaid = new ItemStack(Material.COD);
                        ItemStack witch = new ItemStack(Material.AMETHYST_SHARD);
                        ItemStack rabbit = new ItemStack(Material.CARROT);
                        ItemStack bumblebee = new ItemStack(Material.HONEYCOMB);
                        ItemStack elytrian = new ItemStack(Material.ELYTRA);
                        ItemStack avian = new ItemStack(Material.FEATHER);
                        ItemStack piglin = new ItemStack(Material.GOLD_INGOT);
                        ItemStack dragonborne = new ItemStack(Material.DRAGON_BREATH);
                        if (GenesisDataFiles.get1().getString("human-disable").equalsIgnoreCase("false")) {
                            ItemMeta human_meta = human.getItemMeta();
                            human_meta.setDisplayName(ChatColor.WHITE + "Human");
                            ArrayList<String> human_lore = new ArrayList<>();
                            human_lore.add(ChatColor.WHITE + "Human Origin");
                            human_meta.setLore(human_lore);
                            human.setItemMeta(human_meta);
                        } else {
                            ItemMeta nope_meta = human.getItemMeta();
                            nope_meta.setDisplayName(ChatColor.RED + "Unavailable");
                            nope_meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                            nope_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            ArrayList<String> nope_lore = new ArrayList<>();
                            nope_lore.add(ChatColor.RED + "This origin is locked by the server owner");
                            nope_meta.setLore(nope_lore);
                            human.setItemMeta(nope_meta);
                        }
                        if (GenesisDataFiles.get().getString("enderian-disable").equalsIgnoreCase("false")) {
                            ItemMeta ender_meta = enderian.getItemMeta();
                            ender_meta.setDisplayName(LIGHT_PURPLE + "Enderian");
                            ArrayList<String> ender_lore = new ArrayList<>();
                            ender_lore.add(ChatColor.WHITE + "Enderman Origin");
                            ender_meta.setLore(ender_lore);
                            enderian.setItemMeta(ender_meta);
                        } else {
                            ItemMeta nope_meta = enderian.getItemMeta();
                            nope_meta.setDisplayName(ChatColor.RED + "Unavailable");
                            nope_meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                            nope_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            ArrayList<String> nope_lore = new ArrayList<>();
                            nope_lore.add(ChatColor.RED + "This origin is locked by the server owner");
                            nope_meta.setLore(nope_lore);
                            enderian.setItemMeta(nope_meta);
                        }
                        if (GenesisDataFiles.get2().getString("shulk-disable").equalsIgnoreCase("false")) {
                            ItemMeta shulker_meta = shulker.getItemMeta();
                            shulker_meta.setDisplayName(ChatColor.DARK_PURPLE + "Shulk");
                            ArrayList<String> shulker_lore = new ArrayList<>();
                            shulker_lore.add(ChatColor.WHITE + "Shulker Origin");
                            shulker_meta.setLore(shulker_lore);
                            shulker.setItemMeta(shulker_meta);
                        } else {
                            ItemMeta nope_meta = shulker.getItemMeta();
                            nope_meta.setDisplayName(ChatColor.RED + "Unavailable");
                            nope_meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                            nope_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            ArrayList<String> nope_lore = new ArrayList<>();
                            nope_lore.add(ChatColor.RED + "This origin is locked by the server owner");
                            nope_meta.setLore(nope_lore);
                            shulker.setItemMeta(nope_meta);
                        }
                        if (GenesisDataFiles.get17().getString("arachnid-disable").equalsIgnoreCase("false")) {
                            ItemMeta spider_meta = arachnid.getItemMeta();
                            spider_meta.setDisplayName(ChatColor.RED + "Arachnid");
                            ArrayList<String> spider_lore = new ArrayList<>();
                            spider_lore.add(ChatColor.WHITE + "Spider Origin");
                            spider_meta.setLore(spider_lore);
                            arachnid.setItemMeta(spider_meta);
                        } else {
                            ItemMeta nope_meta = arachnid.getItemMeta();
                            nope_meta.setDisplayName(ChatColor.RED + "Unavailable");
                            nope_meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                            nope_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            ArrayList<String> nope_lore = new ArrayList<>();
                            nope_lore.add(ChatColor.RED + "This origin is locked by the server owner");
                            nope_meta.setLore(nope_lore);
                            arachnid.setItemMeta(nope_meta);
                        }
                        if (GenesisDataFiles.get3().getString("creep-disable").equalsIgnoreCase("false")) {
                            ItemMeta creep_meta = creep.getItemMeta();
                            creep_meta.setDisplayName(ChatColor.GREEN + "Creep");
                            ArrayList<String> creep_lore = new ArrayList<>();
                            creep_lore.add(ChatColor.WHITE + "Creeper Origin");
                            creep_meta.setLore(creep_lore);
                            creep.setItemMeta(creep_meta);
                        } else {
                            ItemMeta nope_meta = creep.getItemMeta();
                            nope_meta.setDisplayName(ChatColor.RED + "Unavailable");
                            nope_meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                            nope_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            ArrayList<String> nope_lore = new ArrayList<>();
                            nope_lore.add(ChatColor.RED + "This origin is locked by the server owner");
                            nope_meta.setLore(nope_lore);
                            creep.setItemMeta(nope_meta);
                        }
                        if (GenesisDataFiles.get4().getString("phantom-disable").equalsIgnoreCase("false")) {
                            ItemMeta phantom_meta = phantom.getItemMeta();
                            phantom_meta.setDisplayName(ChatColor.BLUE + "Phantom");
                            ArrayList<String> phantom_lore = new ArrayList<>();
                            phantom_lore.add(ChatColor.WHITE + "Phantom Origin");
                            phantom_meta.setLore(phantom_lore);
                            phantom.setItemMeta(phantom_meta);
                        } else {
                            ItemMeta nope_meta = phantom.getItemMeta();
                            nope_meta.setDisplayName(ChatColor.RED + "Unavailable");
                            nope_meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                            nope_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            ArrayList<String> nope_lore = new ArrayList<>();
                            nope_lore.add(ChatColor.RED + "This origin is locked by the server owner");
                            nope_meta.setLore(nope_lore);
                            phantom.setItemMeta(nope_meta);
                        }
                        if (GenesisDataFiles.get5().getString("slimeling-disable").equalsIgnoreCase("false")) {
                            ItemMeta slime_meta = slimeling.getItemMeta();
                            slime_meta.setDisplayName(ChatColor.GREEN + "Slimeling");
                            ArrayList<String> slime_lore = new ArrayList<>();
                            slime_lore.add(ChatColor.WHITE + "Slime Origin");
                            slime_meta.setLore(slime_lore);
                            slimeling.setItemMeta(slime_meta);
                        } else {
                            ItemMeta nope_meta = slimeling.getItemMeta();
                            nope_meta.setDisplayName(ChatColor.RED + "Unavailable");
                            nope_meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                            nope_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            ArrayList<String> nope_lore = new ArrayList<>();
                            nope_lore.add(ChatColor.RED + "This origin is locked by the server owner");
                            nope_meta.setLore(nope_lore);
                            slimeling.setItemMeta(nope_meta);
                        }
                        if (GenesisDataFiles.get6().getString("vexian-disable").equalsIgnoreCase("false")) {
                            ItemMeta vex_meta = vexian.getItemMeta();
                            vex_meta.setDisplayName(ChatColor.AQUA + "Vexian");
                            ArrayList<String> vex_lore = new ArrayList<>();
                            vex_lore.add(ChatColor.WHITE + "Vex Origin");
                            vex_meta.getItemFlags().add(ItemFlag.HIDE_ATTRIBUTES);
                            vex_meta.setLore(vex_lore);
                            vexian.setItemMeta(vex_meta);
                        } else {
                            ItemMeta nope_meta = vexian.getItemMeta();
                            nope_meta.setDisplayName(ChatColor.RED + "Unavailable");
                            nope_meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                            nope_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            ArrayList<String> nope_lore = new ArrayList<>();
                            nope_lore.add(ChatColor.RED + "This origin is locked by the server owner");
                            nope_meta.setLore(nope_lore);
                            vexian.setItemMeta(nope_meta);
                        }
                        if (GenesisDataFiles.get7().getString("blazeborn-disable").equalsIgnoreCase("false")) {
                            ItemMeta blaze_meta = blazeborn.getItemMeta();
                            blaze_meta.setDisplayName(ChatColor.GOLD + "Blazeborn");
                            ArrayList<String> blaze_lore = new ArrayList<>();
                            blaze_lore.add(ChatColor.WHITE + "Blaze Origin");
                            blaze_meta.setLore(blaze_lore);
                            blazeborn.setItemMeta(blaze_meta);
                        } else {
                            ItemMeta nope_meta = blazeborn.getItemMeta();
                            nope_meta.setDisplayName(ChatColor.RED + "Unavailable");
                            nope_meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                            nope_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            ArrayList<String> nope_lore = new ArrayList<>();
                            nope_lore.add(ChatColor.RED + "This origin is locked by the server owner");
                            nope_meta.setLore(nope_lore);
                            blazeborn.setItemMeta(nope_meta);
                        }
                        if (GenesisDataFiles.get8().getString("starborne-disable").equalsIgnoreCase("false")) {
                            ItemMeta star_meta = starborne.getItemMeta();
                            star_meta.setDisplayName(LIGHT_PURPLE + "Starborne");
                            ArrayList<String> star_lore = new ArrayList<>();
                            star_lore.add(ChatColor.WHITE + "Starborne Origin");
                            star_meta.setLore(star_lore);
                            starborne.setItemMeta(star_meta);
                        } else {
                            ItemMeta nope_meta = starborne.getItemMeta();
                            nope_meta.setDisplayName(ChatColor.RED + "Unavailable");
                            nope_meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                            nope_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            ArrayList<String> nope_lore = new ArrayList<>();
                            nope_lore.add(ChatColor.RED + "This origin is locked by the server owner");
                            nope_meta.setLore(nope_lore);
                            starborne.setItemMeta(nope_meta);
                        }
                        if (GenesisDataFiles.get9().getString("merling-disable").equalsIgnoreCase("false")) {
                            ItemMeta mer_meta = mermaid.getItemMeta();
                            mer_meta.setDisplayName(ChatColor.BLUE + "Mermaid");
                            ArrayList<String> mer_lore = new ArrayList<>();
                            mer_lore.add(ChatColor.WHITE + "Mermaid Origin");
                            mer_meta.setLore(mer_lore);
                            mermaid.setItemMeta(mer_meta);
                        } else {
                            ItemMeta nope_meta = mermaid.getItemMeta();
                            nope_meta.setDisplayName(ChatColor.RED + "Unavailable");
                            nope_meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                            nope_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            ArrayList<String> nope_lore = new ArrayList<>();
                            nope_lore.add(ChatColor.RED + "This origin is locked by the server owner");
                            nope_meta.setLore(nope_lore);
                            mermaid.setItemMeta(nope_meta);
                        }
                        if (GenesisDataFiles.get10().getString("allay-disable").equalsIgnoreCase("false")) {
                            ItemMeta witch_meta = witch.getItemMeta();
                            witch_meta.setDisplayName(ChatColor.AQUA + "Allay");
                            ArrayList<String> witch_lore = new ArrayList<>();
                            witch_lore.add(ChatColor.WHITE + "Allay Origin");
                            witch_meta.setLore(witch_lore);
                            witch.setItemMeta(witch_meta);
                        } else {
                            ItemMeta nope_meta = witch.getItemMeta();
                            nope_meta.setDisplayName(ChatColor.RED + "Unavailable");
                            nope_meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                            nope_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            ArrayList<String> nope_lore = new ArrayList<>();
                            nope_lore.add(ChatColor.RED + "This origin is locked by the server owner");
                            nope_meta.setLore(nope_lore);
                            witch.setItemMeta(nope_meta);
                        }
                        if (GenesisDataFiles.get11().getString("rabbit-disable").equalsIgnoreCase("false")) {
                            ItemMeta rabbit_meta = rabbit.getItemMeta();
                            rabbit_meta.setDisplayName(ChatColor.GOLD + "Rabbit");
                            ArrayList<String> rabbit_lore = new ArrayList<>();
                            rabbit_lore.add(ChatColor.WHITE + "Rabbit Origin");
                            rabbit_meta.setLore(rabbit_lore);
                            rabbit.setItemMeta(rabbit_meta);
                        } else {
                            ItemMeta nope_meta = rabbit.getItemMeta();
                            nope_meta.setDisplayName(ChatColor.RED + "Unavailable");
                            nope_meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                            nope_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            ArrayList<String> nope_lore = new ArrayList<>();
                            nope_lore.add(ChatColor.RED + "This origin is locked by the server owner");
                            nope_meta.setLore(nope_lore);
                            rabbit.setItemMeta(nope_meta);
                        }
                        if (GenesisDataFiles.get12().getString("bumblebee-disable").equalsIgnoreCase("false")) {
                            ItemMeta bee_meta = bumblebee.getItemMeta();
                            bee_meta.setDisplayName(ChatColor.YELLOW + "Bumblebee");
                            ArrayList<String> bee_lore = new ArrayList<>();
                            bee_lore.add(ChatColor.WHITE + "Bee Origin");
                            bee_meta.setLore(bee_lore);
                            bumblebee.setItemMeta(bee_meta);
                        } else {
                            ItemMeta nope_meta = bumblebee.getItemMeta();
                            nope_meta.setDisplayName(ChatColor.RED + "Unavailable");
                            nope_meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                            nope_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            ArrayList<String> nope_lore = new ArrayList<>();
                            nope_lore.add(ChatColor.RED + "This origin is locked by the server owner");
                            nope_meta.setLore(nope_lore);
                            bumblebee.setItemMeta(nope_meta);
                        }
                        if (GenesisDataFiles.get13().getString("elytrian-disable").equalsIgnoreCase("false")) {
                            ItemMeta elytra_meta = elytrian.getItemMeta();
                            elytra_meta.setDisplayName(ChatColor.GRAY + "Elytrian");
                            ArrayList<String> elytra_lore = new ArrayList<>();
                            elytra_lore.add(ChatColor.WHITE + "Elytra Origin");
                            elytra_meta.setLore(elytra_lore);
                            elytrian.setItemMeta(elytra_meta);
                        } else {
                            ItemMeta nope_meta = elytrian.getItemMeta();
                            nope_meta.setDisplayName(ChatColor.RED + "Unavailable");
                            nope_meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                            nope_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            ArrayList<String> nope_lore = new ArrayList<>();
                            nope_lore.add(ChatColor.RED + "This origin is locked by the server owner");
                            nope_meta.setLore(nope_lore);
                            elytrian.setItemMeta(nope_meta);
                        }
                        if (GenesisDataFiles.get14().getString("avian-disable").equalsIgnoreCase("false")) {
                            ItemMeta avian_meta = avian.getItemMeta();
                            avian_meta.setDisplayName(ChatColor.DARK_AQUA + "Avian");
                            ArrayList<String> avian_lore = new ArrayList<>();
                            avian_lore.add(ChatColor.WHITE + "Avian Origin");
                            avian_meta.setLore(avian_lore);
                            avian.setItemMeta(avian_meta);
                        } else {
                            ItemMeta nope_meta = avian.getItemMeta();
                            nope_meta.setDisplayName(ChatColor.RED + "Unavailable");
                            nope_meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                            nope_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            ArrayList<String> nope_lore = new ArrayList<>();
                            nope_lore.add(ChatColor.RED + "This origin is locked by the server owner");
                            nope_meta.setLore(nope_lore);
                            avian.setItemMeta(nope_meta);
                        }
                        if (GenesisDataFiles.get15().getString("piglin-disable").equalsIgnoreCase("false")) {
                            ItemMeta pig_meta = piglin.getItemMeta();
                            pig_meta.setDisplayName(ChatColor.GOLD + "Piglin");
                            ArrayList<String> pig_lore = new ArrayList<>();
                            pig_lore.add(ChatColor.WHITE + "Piglin Origin");
                            pig_meta.setLore(pig_lore);
                            piglin.setItemMeta(pig_meta);
                        } else {
                            ItemMeta nope_meta = piglin.getItemMeta();
                            nope_meta.setDisplayName(ChatColor.RED + "Unavailable");
                            nope_meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                            nope_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            ArrayList<String> nope_lore = new ArrayList<>();
                            nope_lore.add(ChatColor.RED + "This origin is locked by the server owner");
                            nope_meta.setLore(nope_lore);
                            piglin.setItemMeta(nope_meta);
                        }
                        if (GenesisDataFiles.get16().getString("dragonborne-disable").equalsIgnoreCase("false")) {
                            ItemMeta dragonborne_meta = dragonborne.getItemMeta();
                            dragonborne_meta.setDisplayName(ChatColor.DARK_PURPLE + "Dragonborne");
                            ArrayList<String> dragonborne_lore = new ArrayList<>();
                            dragonborne_lore.add(ChatColor.WHITE + "Dragon Origin");
                            dragonborne_meta.setLore(dragonborne_lore);
                            dragonborne.setItemMeta(dragonborne_meta);
                        } else {
                            ItemMeta nope_meta = dragonborne.getItemMeta();
                            nope_meta.setDisplayName(ChatColor.RED + "Unavailable");
                            nope_meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                            nope_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            ArrayList<String> nope_lore = new ArrayList<>();
                            nope_lore.add(ChatColor.RED + "This origin is locked by the server owner");
                            nope_meta.setLore(nope_lore);
                            dragonborne.setItemMeta(nope_meta);
                        }

                        ItemStack[] allmenu_items = {human, enderian, shulker, arachnid, creep, phantom, slimeling, vexian, blazeborn, starborne, mermaid, witch, rabbit, bumblebee, elytrian, avian, piglin, dragonborne};
                        allmenu.setContents(allmenu_items);
                        p.openInventory(allmenu);
                    } else {
                        Inventory allmenu = createInventory(p, 27, LIGHT_PURPLE + "Origins Menu");

                        ItemStack human = new ItemStack(Material.PLAYER_HEAD);
                        ItemStack enderian = new ItemStack(Material.ENDER_PEARL);
                        ItemStack shulker = new ItemStack(Material.SHULKER_SHELL);
                        ItemStack arachnid = new ItemStack(Material.COBWEB);
                        ItemStack creep = new ItemStack(Material.GUNPOWDER);
                        ItemStack phantom = new ItemStack(Material.PHANTOM_MEMBRANE);
                        ItemStack slimeling = new ItemStack(Material.SLIME_BALL);
                        ItemStack vexian = new ItemStack(Material.IRON_SWORD);
                        ItemStack blazeborn = new ItemStack(Material.BLAZE_POWDER);
                        ItemStack starborne = new ItemStack(Material.NETHER_STAR);
                        ItemStack mermaid = new ItemStack(Material.COD);
                        ItemStack witch = new ItemStack(Material.AMETHYST_SHARD);
                        ItemStack rabbit = new ItemStack(Material.CARROT);
                        ItemStack bumblebee = new ItemStack(Material.HONEYCOMB);
                        ItemStack elytrian = new ItemStack(Material.ELYTRA);
                        ItemStack avian = new ItemStack(Material.FEATHER);
                        ItemStack piglin = new ItemStack(Material.GOLD_INGOT);
                        ItemStack dragonborne = new ItemStack(Material.DRAGON_BREATH);
                        ItemStack air = new ItemStack(Material.AIR);

                        ItemStack custom_originmenu = new ItemStack(Material.TIPPED_ARROW);
                        ItemStack close = new ItemStack(Material.BARRIER);
                        ItemMeta close_meta = close.getItemMeta();
                        close_meta.setDisplayName(ChatColor.RED + "Close");
                        ArrayList<String> close_lore = new ArrayList<>();
                        close_lore.add(ChatColor.RED + "Cancel Choosing");
                        close_meta.setLore(close_lore);
                        close.setItemMeta(close_meta);

                        ItemMeta originmenuItemMeta = custom_originmenu.getItemMeta();
                        originmenuItemMeta.setDisplayName(ChatColor.BLUE + "Custom Origins");
                        custom_originmenu.setItemMeta(originmenuItemMeta);


                        if (GenesisDataFiles.get1().getString("human-disable").equalsIgnoreCase("false")) {
                            ItemMeta human_meta = human.getItemMeta();
                            human_meta.setDisplayName(ChatColor.WHITE + "Human");
                            ArrayList<String> human_lore = new ArrayList<>();
                            human_lore.add(ChatColor.WHITE + "Human Origin");
                            human_meta.setLore(human_lore);
                            human.setItemMeta(human_meta);
                        } else {
                            ItemMeta nope_meta = human.getItemMeta();
                            nope_meta.setDisplayName(ChatColor.RED + "Unavailable");
                            nope_meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                            nope_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            ArrayList<String> nope_lore = new ArrayList<>();
                            nope_lore.add(ChatColor.RED + "This origin is locked by the server owner");
                            nope_meta.setLore(nope_lore);
                            human.setItemMeta(nope_meta);
                        }
                        if (GenesisDataFiles.get().getString("enderian-disable").equalsIgnoreCase("false")) {
                            ItemMeta ender_meta = enderian.getItemMeta();
                            ender_meta.setDisplayName(LIGHT_PURPLE + "Enderian");
                            ArrayList<String> ender_lore = new ArrayList<>();
                            ender_lore.add(ChatColor.WHITE + "Enderman Origin");
                            ender_meta.setLore(ender_lore);
                            enderian.setItemMeta(ender_meta);
                        } else {
                            ItemMeta nope_meta = enderian.getItemMeta();
                            nope_meta.setDisplayName(ChatColor.RED + "Unavailable");
                            nope_meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                            nope_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            ArrayList<String> nope_lore = new ArrayList<>();
                            nope_lore.add(ChatColor.RED + "This origin is locked by the server owner");
                            nope_meta.setLore(nope_lore);
                            enderian.setItemMeta(nope_meta);
                        }
                        if (GenesisDataFiles.get2().getString("shulk-disable").equalsIgnoreCase("false")) {
                            ItemMeta shulker_meta = shulker.getItemMeta();
                            shulker_meta.setDisplayName(ChatColor.DARK_PURPLE + "Shulk");
                            ArrayList<String> shulker_lore = new ArrayList<>();
                            shulker_lore.add(ChatColor.WHITE + "Shulker Origin");
                            shulker_meta.setLore(shulker_lore);
                            shulker.setItemMeta(shulker_meta);
                        } else {
                            ItemMeta nope_meta = shulker.getItemMeta();
                            nope_meta.setDisplayName(ChatColor.RED + "Unavailable");
                            nope_meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                            nope_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            ArrayList<String> nope_lore = new ArrayList<>();
                            nope_lore.add(ChatColor.RED + "This origin is locked by the server owner");
                            nope_meta.setLore(nope_lore);
                            shulker.setItemMeta(nope_meta);
                        }
                        if (GenesisDataFiles.get17().getString("arachnid-disable").equalsIgnoreCase("false")) {
                            ItemMeta spider_meta = arachnid.getItemMeta();
                            spider_meta.setDisplayName(ChatColor.RED + "Arachnid");
                            ArrayList<String> spider_lore = new ArrayList<>();
                            spider_lore.add(ChatColor.WHITE + "Spider Origin");
                            spider_meta.setLore(spider_lore);
                            arachnid.setItemMeta(spider_meta);
                        } else {
                            ItemMeta nope_meta = arachnid.getItemMeta();
                            nope_meta.setDisplayName(ChatColor.RED + "Unavailable");
                            nope_meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                            nope_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            ArrayList<String> nope_lore = new ArrayList<>();
                            nope_lore.add(ChatColor.RED + "This origin is locked by the server owner");
                            nope_meta.setLore(nope_lore);
                            arachnid.setItemMeta(nope_meta);
                        }
                        if (GenesisDataFiles.get3().getString("creep-disable").equalsIgnoreCase("false")) {
                            ItemMeta creep_meta = creep.getItemMeta();
                            creep_meta.setDisplayName(ChatColor.GREEN + "Creep");
                            ArrayList<String> creep_lore = new ArrayList<>();
                            creep_lore.add(ChatColor.WHITE + "Creeper Origin");
                            creep_meta.setLore(creep_lore);
                            creep.setItemMeta(creep_meta);
                        } else {
                            ItemMeta nope_meta = creep.getItemMeta();
                            nope_meta.setDisplayName(ChatColor.RED + "Unavailable");
                            nope_meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                            nope_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            ArrayList<String> nope_lore = new ArrayList<>();
                            nope_lore.add(ChatColor.RED + "This origin is locked by the server owner");
                            nope_meta.setLore(nope_lore);
                            creep.setItemMeta(nope_meta);
                        }
                        if (GenesisDataFiles.get4().getString("phantom-disable").equalsIgnoreCase("false")) {
                            ItemMeta phantom_meta = phantom.getItemMeta();
                            phantom_meta.setDisplayName(ChatColor.BLUE + "Phantom");
                            ArrayList<String> phantom_lore = new ArrayList<>();
                            phantom_lore.add(ChatColor.WHITE + "Phantom Origin");
                            phantom_meta.setLore(phantom_lore);
                            phantom.setItemMeta(phantom_meta);
                        } else {
                            ItemMeta nope_meta = phantom.getItemMeta();
                            nope_meta.setDisplayName(ChatColor.RED + "Unavailable");
                            nope_meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                            nope_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            ArrayList<String> nope_lore = new ArrayList<>();
                            nope_lore.add(ChatColor.RED + "This origin is locked by the server owner");
                            nope_meta.setLore(nope_lore);
                            phantom.setItemMeta(nope_meta);
                        }
                        if (GenesisDataFiles.get5().getString("slimeling-disable").equalsIgnoreCase("false")) {
                            ItemMeta slime_meta = slimeling.getItemMeta();
                            slime_meta.setDisplayName(ChatColor.GREEN + "Slimeling");
                            ArrayList<String> slime_lore = new ArrayList<>();
                            slime_lore.add(ChatColor.WHITE + "Slime Origin");
                            slime_meta.setLore(slime_lore);
                            slimeling.setItemMeta(slime_meta);
                        } else {
                            ItemMeta nope_meta = slimeling.getItemMeta();
                            nope_meta.setDisplayName(ChatColor.RED + "Unavailable");
                            nope_meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                            nope_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            ArrayList<String> nope_lore = new ArrayList<>();
                            nope_lore.add(ChatColor.RED + "This origin is locked by the server owner");
                            nope_meta.setLore(nope_lore);
                            slimeling.setItemMeta(nope_meta);
                        }
                        if (GenesisDataFiles.get6().getString("vexian-disable").equalsIgnoreCase("false")) {
                            ItemMeta vex_meta = vexian.getItemMeta();
                            vex_meta.setDisplayName(ChatColor.AQUA + "Vexian");
                            ArrayList<String> vex_lore = new ArrayList<>();
                            vex_lore.add(ChatColor.WHITE + "Vex Origin");
                            vex_meta.getItemFlags().add(ItemFlag.HIDE_ATTRIBUTES);
                            vex_meta.setLore(vex_lore);
                            vexian.setItemMeta(vex_meta);
                        } else {
                            ItemMeta nope_meta = vexian.getItemMeta();
                            nope_meta.setDisplayName(ChatColor.RED + "Unavailable");
                            nope_meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                            nope_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            ArrayList<String> nope_lore = new ArrayList<>();
                            nope_lore.add(ChatColor.RED + "This origin is locked by the server owner");
                            nope_meta.setLore(nope_lore);
                            vexian.setItemMeta(nope_meta);
                        }
                        if (GenesisDataFiles.get7().getString("blazeborn-disable").equalsIgnoreCase("false")) {
                            ItemMeta blaze_meta = blazeborn.getItemMeta();
                            blaze_meta.setDisplayName(ChatColor.GOLD + "Blazeborn");
                            ArrayList<String> blaze_lore = new ArrayList<>();
                            blaze_lore.add(ChatColor.WHITE + "Blaze Origin");
                            blaze_meta.setLore(blaze_lore);
                            blazeborn.setItemMeta(blaze_meta);
                        } else {
                            ItemMeta nope_meta = blazeborn.getItemMeta();
                            nope_meta.setDisplayName(ChatColor.RED + "Unavailable");
                            nope_meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                            nope_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            ArrayList<String> nope_lore = new ArrayList<>();
                            nope_lore.add(ChatColor.RED + "This origin is locked by the server owner");
                            nope_meta.setLore(nope_lore);
                            blazeborn.setItemMeta(nope_meta);
                        }
                        if (GenesisDataFiles.get8().getString("starborne-disable").equalsIgnoreCase("false")) {
                            ItemMeta star_meta = starborne.getItemMeta();
                            star_meta.setDisplayName(LIGHT_PURPLE + "Starborne");
                            ArrayList<String> star_lore = new ArrayList<>();
                            star_lore.add(ChatColor.WHITE + "Starborne Origin");
                            star_meta.setLore(star_lore);
                            starborne.setItemMeta(star_meta);
                        } else {
                            ItemMeta nope_meta = starborne.getItemMeta();
                            nope_meta.setDisplayName(ChatColor.RED + "Unavailable");
                            nope_meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                            nope_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            ArrayList<String> nope_lore = new ArrayList<>();
                            nope_lore.add(ChatColor.RED + "This origin is locked by the server owner");
                            nope_meta.setLore(nope_lore);
                            starborne.setItemMeta(nope_meta);
                        }
                        if (GenesisDataFiles.get9().getString("merling-disable").equalsIgnoreCase("false")) {
                            ItemMeta mer_meta = mermaid.getItemMeta();
                            mer_meta.setDisplayName(ChatColor.BLUE + "Mermaid");
                            ArrayList<String> mer_lore = new ArrayList<>();
                            mer_lore.add(ChatColor.WHITE + "Mermaid Origin");
                            mer_meta.setLore(mer_lore);
                            mermaid.setItemMeta(mer_meta);
                        } else {
                            ItemMeta nope_meta = mermaid.getItemMeta();
                            nope_meta.setDisplayName(ChatColor.RED + "Unavailable");
                            nope_meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                            nope_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            ArrayList<String> nope_lore = new ArrayList<>();
                            nope_lore.add(ChatColor.RED + "This origin is locked by the server owner");
                            nope_meta.setLore(nope_lore);
                            mermaid.setItemMeta(nope_meta);
                        }
                        if (GenesisDataFiles.get10().getString("allay-disable").equalsIgnoreCase("false")) {
                            ItemMeta witch_meta = witch.getItemMeta();
                            witch_meta.setDisplayName(ChatColor.AQUA + "Allay");
                            ArrayList<String> witch_lore = new ArrayList<>();
                            witch_lore.add(ChatColor.WHITE + "Allay Origin");
                            witch_meta.setLore(witch_lore);
                            witch.setItemMeta(witch_meta);
                        } else {
                            ItemMeta nope_meta = witch.getItemMeta();
                            nope_meta.setDisplayName(ChatColor.RED + "Unavailable");
                            nope_meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                            nope_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            ArrayList<String> nope_lore = new ArrayList<>();
                            nope_lore.add(ChatColor.RED + "This origin is locked by the server owner");
                            nope_meta.setLore(nope_lore);
                            witch.setItemMeta(nope_meta);
                        }
                        if (GenesisDataFiles.get11().getString("rabbit-disable").equalsIgnoreCase("false")) {
                            ItemMeta rabbit_meta = rabbit.getItemMeta();
                            rabbit_meta.setDisplayName(ChatColor.GOLD + "Rabbit");
                            ArrayList<String> rabbit_lore = new ArrayList<>();
                            rabbit_lore.add(ChatColor.WHITE + "Rabbit Origin");
                            rabbit_meta.setLore(rabbit_lore);
                            rabbit.setItemMeta(rabbit_meta);
                        } else {
                            ItemMeta nope_meta = rabbit.getItemMeta();
                            nope_meta.setDisplayName(ChatColor.RED + "Unavailable");
                            nope_meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                            nope_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            ArrayList<String> nope_lore = new ArrayList<>();
                            nope_lore.add(ChatColor.RED + "This origin is locked by the server owner");
                            nope_meta.setLore(nope_lore);
                            rabbit.setItemMeta(nope_meta);
                        }
                        if (GenesisDataFiles.get12().getString("bumblebee-disable").equalsIgnoreCase("false")) {
                            ItemMeta bee_meta = bumblebee.getItemMeta();
                            bee_meta.setDisplayName(ChatColor.YELLOW + "Bumblebee");
                            ArrayList<String> bee_lore = new ArrayList<>();
                            bee_lore.add(ChatColor.WHITE + "Bee Origin");
                            bee_meta.setLore(bee_lore);
                            bumblebee.setItemMeta(bee_meta);
                        } else {
                            ItemMeta nope_meta = bumblebee.getItemMeta();
                            nope_meta.setDisplayName(ChatColor.RED + "Unavailable");
                            nope_meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                            nope_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            ArrayList<String> nope_lore = new ArrayList<>();
                            nope_lore.add(ChatColor.RED + "This origin is locked by the server owner");
                            nope_meta.setLore(nope_lore);
                            bumblebee.setItemMeta(nope_meta);
                        }
                        if (GenesisDataFiles.get13().getString("elytrian-disable").equalsIgnoreCase("false")) {
                            ItemMeta elytra_meta = elytrian.getItemMeta();
                            elytra_meta.setDisplayName(ChatColor.GRAY + "Elytrian");
                            ArrayList<String> elytra_lore = new ArrayList<>();
                            elytra_lore.add(ChatColor.WHITE + "Elytra Origin");
                            elytra_meta.setLore(elytra_lore);
                            elytrian.setItemMeta(elytra_meta);
                        } else {
                            ItemMeta nope_meta = elytrian.getItemMeta();
                            nope_meta.setDisplayName(ChatColor.RED + "Unavailable");
                            nope_meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                            nope_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            ArrayList<String> nope_lore = new ArrayList<>();
                            nope_lore.add(ChatColor.RED + "This origin is locked by the server owner");
                            nope_meta.setLore(nope_lore);
                            elytrian.setItemMeta(nope_meta);
                        }
                        if (GenesisDataFiles.get14().getString("avian-disable").equalsIgnoreCase("false")) {
                            ItemMeta avian_meta = avian.getItemMeta();
                            avian_meta.setDisplayName(ChatColor.DARK_AQUA + "Avian");
                            ArrayList<String> avian_lore = new ArrayList<>();
                            avian_lore.add(ChatColor.WHITE + "Avian Origin");
                            avian_meta.setLore(avian_lore);
                            avian.setItemMeta(avian_meta);
                        } else {
                            ItemMeta nope_meta = avian.getItemMeta();
                            nope_meta.setDisplayName(ChatColor.RED + "Unavailable");
                            nope_meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                            nope_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            ArrayList<String> nope_lore = new ArrayList<>();
                            nope_lore.add(ChatColor.RED + "This origin is locked by the server owner");
                            nope_meta.setLore(nope_lore);
                            avian.setItemMeta(nope_meta);
                        }
                        if (GenesisDataFiles.get15().getString("piglin-disable").equalsIgnoreCase("false")) {
                            ItemMeta pig_meta = piglin.getItemMeta();
                            pig_meta.setDisplayName(ChatColor.GOLD + "Piglin");
                            ArrayList<String> pig_lore = new ArrayList<>();
                            pig_lore.add(ChatColor.WHITE + "Piglin Origin");
                            pig_meta.setLore(pig_lore);
                            piglin.setItemMeta(pig_meta);
                        } else {
                            ItemMeta nope_meta = piglin.getItemMeta();
                            nope_meta.setDisplayName(ChatColor.RED + "Unavailable");
                            nope_meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                            nope_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            ArrayList<String> nope_lore = new ArrayList<>();
                            nope_lore.add(ChatColor.RED + "This origin is locked by the server owner");
                            nope_meta.setLore(nope_lore);
                            piglin.setItemMeta(nope_meta);
                        }
                        if (GenesisDataFiles.get16().getString("dragonborne-disable").equalsIgnoreCase("false")) {
                            ItemMeta dragonborne_meta = dragonborne.getItemMeta();
                            dragonborne_meta.setDisplayName(ChatColor.DARK_PURPLE + "Dragonborne");
                            ArrayList<String> dragonborne_lore = new ArrayList<>();
                            dragonborne_lore.add(ChatColor.WHITE + "Dragon Origin");
                            dragonborne_meta.setLore(dragonborne_lore);
                            dragonborne.setItemMeta(dragonborne_meta);
                        } else {
                            ItemMeta nope_meta = dragonborne.getItemMeta();
                            nope_meta.setDisplayName(ChatColor.RED + "Unavailable");
                            nope_meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                            nope_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            ArrayList<String> nope_lore = new ArrayList<>();
                            nope_lore.add(ChatColor.RED + "This origin is locked by the server owner");
                            nope_meta.setLore(nope_lore);
                            dragonborne.setItemMeta(nope_meta);
                        }

                        ItemStack[] allmenu_items = {human, enderian, shulker, arachnid, creep, phantom, slimeling, vexian, blazeborn, starborne, mermaid, witch, rabbit, bumblebee, elytrian, avian, piglin, dragonborne, close, air, air, air, custom_originmenu, air, air, air, close};
                        allmenu.setContents(allmenu_items);
                        p.openInventory(allmenu);
                    }
                } else if (p.getOpenInventory().getTitle().equalsIgnoreCase(LIGHT_PURPLE + "Origins Menu") || p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Human") || p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Enderian") || p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Shulk") || p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Arachnid") || p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Creep") || p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Phantom") || p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Slimeling") || p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Vexian") || p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Blazeborn") || p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Starborne") || p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Mermaid") || p.getOpenInventory().getTitle().equalsIgnoreCase("Allay") || p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Rabbit") || p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Bumblebee") || p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Elytrian") || p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Avian") || p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Piglin") || p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Dragonborne") || p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Allay")) {
                        p.setGameMode(GameMode.SPECTATOR);
                }else{
                        p.setGameMode(GameMode.SURVIVAL);
                    }

            }
            if (p.getOpenInventory().getTitle().equalsIgnoreCase(LIGHT_PURPLE + "Origins Menu") || p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Human") || p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Enderian") || p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Shulk") || p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Arachnid") || p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Creep") || p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Phantom") || p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Slimeling") || p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Vexian") || p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Blazeborn") || p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Starborne") || p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Mermaid") || p.getOpenInventory().getTitle().equalsIgnoreCase("Allay") || p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Rabbit") || p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Bumblebee") || p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Elytrian") || p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Avian") || p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Piglin") || p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Dragonborne") || p.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Allay")) {
                p.setGameMode(GameMode.SPECTATOR);
            }

        }

    }
}
