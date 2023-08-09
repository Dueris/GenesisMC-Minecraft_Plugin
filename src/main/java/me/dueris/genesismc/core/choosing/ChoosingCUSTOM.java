package me.dueris.genesismc.core.choosing;

import me.dueris.genesismc.core.GenesisMC;
import me.dueris.genesismc.core.entity.OriginPlayer;
import me.dueris.genesismc.core.events.OriginChangeEvent;
import me.dueris.genesismc.core.factory.CraftApoli;
import me.dueris.genesismc.core.factory.powers.OriginsMod.world.WorldSpawnHandler;
import me.dueris.genesismc.core.utils.Lang;
import me.dueris.genesismc.core.utils.OriginContainer;
import me.dueris.genesismc.core.utils.PowerContainer;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static me.dueris.genesismc.core.choosing.ChoosingCORE.*;
import static me.dueris.genesismc.core.choosing.contents.ChooseMenuContents.ChooseMenuContent;
import static me.dueris.genesismc.core.choosing.contents.MainMenuContents.GenesisMainMenuContents;
import static me.dueris.genesismc.core.factory.powers.Powers.*;
import static me.dueris.genesismc.core.items.OrbOfOrigins.orb;
import static org.bukkit.Bukkit.getServer;
import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.RED;

public class ChoosingCUSTOM implements Listener {

    public static List<String> cutStringIntoLists(String string) {
        ArrayList<String> strings = new ArrayList<>();
        int startStringLength = string.length();
        while (string.length() > 40) {
            for (int i = 40; i > 1; i--) {
                if (String.valueOf(string.charAt(i)).equals(" ")) {
                    strings.add(string.substring(0, i));
                    string = string.substring(i + 1);
                    break;
                }
            }
            if (startStringLength == string.length()) return List.of(string);
        }
        if (strings.isEmpty()) return List.of(string);
        strings.add(string);
        return strings.stream().toList();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void CustomOriginMenu(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) return;
        if (e.getView().getTitle().startsWith("Choosing Menu")) {
            Player p = (Player) e.getWhoClicked();
            @NotNull Inventory custommenu = Bukkit.createInventory(e.getWhoClicked(), 54, "Custom Origins - " + choosing.get(p).getName());
            if (e.getCurrentItem().getType().equals(Material.TIPPED_ARROW)) {
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 2);
                custommenu.setContents(ChooseMenuContent(0, choosing.get(p)));
                e.getWhoClicked().openInventory(custommenu);

            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void OriginChooseMenu(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) return;
        if (e.getCurrentItem().getItemMeta() == null) return;
        if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Origin not found")) return;

        NamespacedKey key = new NamespacedKey(GenesisMC.getPlugin(), "originTag");
        if (e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING) != null && !e.getView().getTitle().startsWith("Origin")) {
            Player p = (Player) e.getWhoClicked();
            @NotNull Inventory custommenu = Bukkit.createInventory(e.getWhoClicked(), 54, "Origin - " + choosing.get(p).getName());
            String originTag = e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);
            if (originTag == null) return;

            //gets the origin container from the tag
            OriginContainer origin = null;
            for (OriginContainer origins : CraftApoli.getOrigins()) {
                if (origins.getTag().equals(originTag)) {
                    origin = origins;
                    break;
                }
            }

            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 2);

            ArrayList<PowerContainer> powerContainers = origin.getPowerContainers();

            //gets icon from origin
            String minecraftItem = origin.getIcon();
            String item = minecraftItem.split(":")[1];
            ItemStack originIcon = new ItemStack(Material.valueOf(item.toUpperCase()));

            //making the items to display in the menu
            ItemStack close = itemProperties(new ItemStack(Material.BARRIER), RED + Lang.getLocalizedString("menu.originSelect.close"), null, null, RED + "Cancel Choosing");
            ItemStack back = itemProperties(new ItemStack(Material.SPECTRAL_ARROW), ChatColor.AQUA + Lang.getLocalizedString("menu.originSelect.return"), ItemFlag.HIDE_ENCHANTS, null, null);
            ItemStack lowImpact = itemProperties(new ItemStack(Material.GREEN_STAINED_GLASS_PANE), ChatColor.WHITE + Lang.getLocalizedString("menu.originSelect.impact.impact") + ChatColor.GREEN + Lang.getLocalizedString("menu.originSelect.impact.low"), null, null, null);
            ItemStack mediumImpact = itemProperties(new ItemStack(Material.YELLOW_STAINED_GLASS_PANE), ChatColor.WHITE + Lang.getLocalizedString("menu.originSelect.impact.impact") + ChatColor.YELLOW + Lang.getLocalizedString("menu.originSelect.impact.medium"), null, null, null);
            ItemStack highImpact = itemProperties(new ItemStack(Material.RED_STAINED_GLASS_PANE), ChatColor.WHITE + Lang.getLocalizedString("menu.originSelect.impact.impact") + ChatColor.RED + Lang.getLocalizedString("menu.originSelect.impact.high"), null, null, null);

            //adds a key to the item that will be used later to get the origin from it
            ItemMeta originIconmeta = originIcon.getItemMeta();
            originIconmeta.setDisplayName(origin.getName());
            originIconmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            originIconmeta.setLore(cutStringIntoLists(origin.getDescription()));
            originIconmeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, originTag);
            NamespacedKey chooseKey = new NamespacedKey(GenesisMC.getPlugin(), "originChoose");
            originIconmeta.getPersistentDataContainer().set(chooseKey, PersistentDataType.INTEGER, 1);
            originIcon.setItemMeta(originIconmeta);

            ArrayList<ItemStack> contents = new ArrayList<>();
            long impact = origin.getImpact();

            //generates menu
            for (int i = 0; i <= 53; i++) {
                if (i == 0 || i == 8) {
                    contents.add(close);
                } else if (i == 1) {
                    if (impact == 1) contents.add(lowImpact);
                    else if (impact == 2) contents.add(mediumImpact);
                    else if (impact == 3) contents.add(highImpact);
                    else contents.add(new ItemStack(Material.AIR));
                } else if (i == 2) {
                    if (impact == 2) contents.add(mediumImpact);
                    else if (impact == 3) contents.add(highImpact);
                    else contents.add(new ItemStack(Material.AIR));
                } else if (i == 3) {
                    if (impact == 3) contents.add(highImpact);
                    else contents.add(new ItemStack(Material.AIR));
                } else if (i == 4) {
                    contents.add(orb);
                } else if (i == 5) {
                    if (impact == 3) contents.add(highImpact);
                    else contents.add(new ItemStack(Material.AIR));
                } else if (i == 6) {
                    if (impact == 2) contents.add(mediumImpact);
                    else if (impact == 3) contents.add(highImpact);
                    else contents.add(new ItemStack(Material.AIR));
                } else if (i == 7) {
                    if (impact == 1) contents.add(lowImpact);
                    else if (impact == 2) contents.add(mediumImpact);
                    else if (impact == 3) contents.add(highImpact);
                    else contents.add(new ItemStack(Material.AIR));
                } else if (i == 13) {
                    if (origin.getTag().equals("origins:human")) {
                        SkullMeta skull_p = (SkullMeta) originIcon.getItemMeta();
                        skull_p.setOwningPlayer(p);
                        skull_p.setOwner(p.getName());
                        skull_p.setPlayerProfile(p.getPlayerProfile());
                        skull_p.setOwnerProfile(p.getPlayerProfile());
                        originIcon.setItemMeta(skull_p);
                    }
                    contents.add(originIcon);
                } else if ((i >= 20 && i <= 24) || (i >= 29 && i <= 33) || (i >= 38 && i <= 42)) {
                    while (powerContainers.size() > 0 && powerContainers.get(0).getHidden()) {
                        powerContainers.remove(0);
                    }
                    if (powerContainers.size() > 0) {

                        ItemStack originPower = new ItemStack(Material.FILLED_MAP);

                        ItemMeta meta = originPower.getItemMeta();
                        meta.setDisplayName(powerContainers.get(0).getName());
                        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                        meta.setLore(cutStringIntoLists(powerContainers.get(0).getDescription()));
                        originPower.setItemMeta(meta);

                        contents.add(originPower);

                        powerContainers.remove(0);

                    } else {
                        if (i >= 38) {
                            contents.add(new ItemStack(Material.AIR));
                        } else {
                            contents.add(new ItemStack(Material.PAPER));
                        }
                    }


                } else if (i == 49) {
                    contents.add(back);
                } else {
                    contents.add(new ItemStack(Material.AIR));
                }
            }
            custommenu.setContents(contents.toArray(new ItemStack[0]));
            e.getWhoClicked().openInventory(custommenu);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void OriginChoose(InventoryClickEvent e) {
        if (e.getCurrentItem() != null) {
            if (e.getCurrentItem().getType() == Material.MAGMA_CREAM) return;
            if (e.getCurrentItem().getItemMeta() == null) return;

            if (e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(GenesisMC.getPlugin(), "originChoose"), PersistentDataType.INTEGER) != null) {
                String originTag = e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(GenesisMC.getPlugin(), "originTag"), PersistentDataType.STRING);
                OriginContainer origin = CraftApoli.getOrigin(originTag);
                Player p = (Player) e.getWhoClicked();

                setAttributesToDefault(p);
                OriginPlayer.setOrigin(p, choosing.get(p), origin);
                choosing.remove(p);
                Bukkit.getScheduler().runTaskLater(GenesisMC.getPlugin(), () -> {
                    p.getPersistentDataContainer().set(new NamespacedKey(GenesisMC.getPlugin(), "can-explode"), PersistentDataType.INTEGER, 1);
                    if (phasing.contains(p)) {
                        p.getPersistentDataContainer().set(new NamespacedKey(GenesisMC.getPlugin(), "in-phantomform"), PersistentDataType.BOOLEAN, true);
                    } else {
                        p.getPersistentDataContainer().set(new NamespacedKey(GenesisMC.getPlugin(), "in-phantomform"), PersistentDataType.BOOLEAN, false);
                    }
                    DefaultChoose.DefaultChoose(p);
                    removeItemPhantom(p);
                    removeItemEnder(p);
                    removeItemElytrian(p);
                }, 1);
                Bukkit.getScheduler().runTaskLater(GenesisMC.getPlugin(), () -> {
                    if (launch_into_air.contains(p)) {
                        ItemStack launchitem = new ItemStack(Material.FEATHER);
                        ItemMeta launchmeta = launchitem.getItemMeta();
                        launchmeta.setDisplayName(GRAY + "Launch");
                        launchmeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                        launchitem.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                        launchitem.setItemMeta(launchmeta);
                        p.getInventory().addItem(launchitem);
                    }
                    if (throw_ender_pearl.contains(p)) {
                        ItemStack infinpearl = new ItemStack(Material.ENDER_PEARL);
                        ItemMeta pearl_meta = infinpearl.getItemMeta();
                        pearl_meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Teleport");
                        ArrayList<String> pearl_lore = new ArrayList();
                        pearl_meta.setUnbreakable(true);
                        pearl_meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                        pearl_meta.setLore(pearl_lore);
                        infinpearl.setItemMeta(pearl_meta);
                        p.getInventory().addItem(infinpearl);
                    }
                    if (phasing.contains(p)) {
                        ItemStack spectatorswitch = new ItemStack(Material.PHANTOM_MEMBRANE);
                        ItemMeta switch_meta = spectatorswitch.getItemMeta();
                        switch_meta.setDisplayName(GRAY + "Phasing Form");
                        ArrayList<String> pearl_lore = new ArrayList();
                        switch_meta.setUnbreakable(true);
                        switch_meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                        switch_meta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
                        switch_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                        switch_meta.setLore(pearl_lore);
                        spectatorswitch.setItemMeta(switch_meta);
                        p.getInventory().addItem(spectatorswitch);
                    }
                    if (nether_spawn.contains(p) && p.getBedSpawnLocation() == null)
                        p.teleport(WorldSpawnHandler.NetherSpawn());
                }, 2);
                OriginChangeEvent Event = new OriginChangeEvent(p);
                getServer().getPluginManager().callEvent(Event);
            }
        }
    }

    @EventHandler
    public void OriginBack(InventoryClickEvent e) {
        if (e.getCurrentItem() != null) {
            if (e.getView().getTitle().startsWith("Origin")) {
                if (e.getCurrentItem().getType().equals(Material.SPECTRAL_ARROW)) {
                    Player p = (Player) e.getWhoClicked();
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 2);
                    NamespacedKey key = new NamespacedKey(GenesisMC.getPlugin(), "originTag");

                    for (OriginContainer origin : CraftApoli.getCoreOrigins()) {
                        if (Objects.equals(e.getClickedInventory().getContents()[13].getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING), origin.getTag())) {
                            @NotNull Inventory mainmenu = Bukkit.createInventory(e.getWhoClicked(), 54, "Choosing Menu - " + choosing.get(p).getName());
                            mainmenu.setContents(GenesisMainMenuContents((Player) e.getWhoClicked()));
                            e.getWhoClicked().openInventory(mainmenu);
                            return;
                        }
                    }

                    @NotNull Inventory custommenu = Bukkit.createInventory(e.getWhoClicked(), 54, "Custom Origins - " + choosing.get(p).getName());
                    custommenu.setContents(ChooseMenuContent(0, choosing.get(p)));
                    e.getWhoClicked().openInventory(custommenu);
                } else e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void OriginClose(InventoryClickEvent e) {
        if (e.getCurrentItem() != null) {
            if (e.getView().getTitle().startsWith("Origin")) {
                if (e.getCurrentItem().getType().equals(Material.BARRIER)) {
                    Player p = (Player) e.getWhoClicked();
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 2);
                    e.getWhoClicked().closeInventory();
                } else {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void scroll(InventoryClickEvent e) {
        if (e.getCurrentItem() != null) {
            if (e.getView().getTitle().startsWith("Custom Origins")) {
                ItemStack item = e.getCurrentItem();
                if (item.getType().equals(Material.ARROW) && (e.getCurrentItem().getItemMeta().getDisplayName().equals(Lang.getLocalizedString("menu.customChoose.back")) || e.getCurrentItem().getItemMeta().getDisplayName().equals(Lang.getLocalizedString("menu.customChoose.next")))) {
                    Player p = (Player) e.getWhoClicked();
                    @NotNull Inventory custommenu = Bukkit.createInventory(e.getWhoClicked(), 54, "Custom Origins - " + choosing.get(p).getName());
                    NamespacedKey key = new NamespacedKey(GenesisMC.getPlugin(), "page");
                    custommenu.setContents(ChooseMenuContent(item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.INTEGER), choosing.get(p)));
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 2);
                    e.getWhoClicked().closeInventory();
                    e.getWhoClicked().openInventory(custommenu);
                } else {
                    e.setCancelled(true);
                }
            }
        }
    }
}


