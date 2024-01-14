package me.dueris.genesismc.factory.powers.player;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import me.dueris.genesismc.entity.OriginPlayerUtils;
import me.dueris.genesismc.factory.conditions.ConditionExecutor;
import me.dueris.genesismc.factory.powers.CraftPower;
import me.dueris.genesismc.utils.PowerContainer;
import me.dueris.genesismc.utils.translation.LangConfig;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static me.dueris.genesismc.utils.ArmorUtils.getArmorValue;

public class RestrictArmor extends CraftPower implements Listener {

    private final int ticksE;
    private Long interval;

    public RestrictArmor() {
        this.interval = 1L;
        this.ticksE = 0;
    }

    public static boolean compareValues(double value1, String comparison, double value2) {
        switch (comparison) {
            case ">":
                return value1 > value2;
            case ">=":
                return value1 >= value2;
            case "<":
                return value1 < value2;
            case "<=":
                return value1 <= value2;
            case "==":
                return value1 == value2;
            case "=":
                return value1 == value2;
            case "!=":
                return value1 != value2;
            default:
                return false;
        }
    }

    @EventHandler
    public void tick(PlayerArmorChangeEvent e) {
        Player p = e.getPlayer();
        if (getPowerArray().contains(p)) {
            for (me.dueris.genesismc.utils.LayerContainer layer : me.dueris.genesismc.factory.CraftApoli.getLayers()) {
                for (PowerContainer power : OriginPlayerUtils.getMultiPowerFileFromType(p, getPowerFile(), layer)) {
                    if (power == null) continue;
                    ConditionExecutor executor = me.dueris.genesismc.GenesisMC.getConditionExecutor();
                    if (executor.check("condition", "conditions", p, power, getPowerFile(), p, null, p.getLocation().getBlock(), null, p.getItemInHand(), null)) {
                        runPower(p, power);
                    }
                }
            }
        }
    }

    @Override
    public void setActive(Player p, String tag, Boolean bool) {
        if (powers_active.containsKey(p)) {
            if (powers_active.get(p).containsKey(tag)) {
                powers_active.get(p).replace(tag, bool);
            } else {
                powers_active.get(p).put(tag, bool);
            }
        } else {
            powers_active.put(p, new HashMap());
            setActive(p, tag, bool);
        }
    }

    public void run(Player p, HashMap<Player, Integer> ticksEMap) {
        ticksEMap.putIfAbsent(p, 0);
        if (getPowerArray().contains(p)) {
            for (me.dueris.genesismc.utils.LayerContainer layer : me.dueris.genesismc.factory.CraftApoli.getLayers()) {
                for (PowerContainer power : OriginPlayerUtils.getMultiPowerFileFromType(p, getPowerFile(), layer)) {
                    if (power == null) continue;
                    if (power.getObjectOrDefault("interval", 1l) == null) {
                        Bukkit.getLogger().warning(LangConfig.getLocalizedString(p, "powers.errors.action_over_time"));
                        return;
                    }

                    interval = power.getLong("interval");
                    int ticksE = ticksEMap.getOrDefault(p, 0);
                    if (ticksE <= interval) {
                        ticksE++;
                        ticksEMap.put(p, ticksE);
                    } else {
                        ConditionExecutor executor = me.dueris.genesismc.GenesisMC.getConditionExecutor();
                        if (executor.check("condition", "conditions", p, power, getPowerFile(), p, null, p.getLocation().getBlock(), null, p.getItemInHand(), null)) {
                            runPower(p, power);
                        } else {
                            setActive(p, power.getTag(), false);
                        }
                        ticksEMap.put(p, 0);
                    }
                }
            }
        }
    }

    public void runPower(Player p, PowerContainer power) {
        setActive(p, power.getTag(), true);
        boolean headb = true;
        boolean chestb = true;
        boolean legsb = true;
        boolean feetb = true;
        JSONObject headObj = power.get("head");
        JSONObject chestObj = power.get("head");
        JSONObject legsObj = power.get("head");
        JSONObject feetObj = power.get("head");

        if (headObj == null) headb = false;
        if (chestObj == null) chestb = false;
        if (legsObj == null) legsb = false;
        if (feetObj == null) feetb = false;

        if (headObj.get("type").toString().equalsIgnoreCase("origins:armor_value")) {
            String comparisonh = headObj.get("comparison").toString();
            String comparisontoh = headObj.get("compare_to").toString();
            if (!headb) return;
            ItemStack item = p.getInventory().getHelmet();
            if (item != null) {
                double armorValue = getArmorValue(item);
                double compareValue = Double.parseDouble(comparisontoh);
                if (compareValues(armorValue, comparisonh, compareValue)) {
                    OriginPlayerUtils.moveEquipmentInventory(p, EquipmentSlot.HEAD);
                }
            }
        } else if (headObj.get("type").toString().equalsIgnoreCase("origins:ingredient")) {
            if (!headb) return;
            if (p.getInventory().getHelmet() != null) {
                Map<String, Object> ingredientMap = (Map<String, Object>) headObj.get("ingredient");
                if (ingredientMap.containsKey("item")) {
                    String itemValue = ingredientMap.get("item").toString();
                    String item = null;
                    if (itemValue.contains(":")) {
                        item = itemValue.split(":")[1];
                    } else {
                        item = itemValue;
                    }
                    if (p.getInventory().getHelmet().getType().equals(Material.valueOf(item.toUpperCase()))) {
                        OriginPlayerUtils.moveEquipmentInventory(p, EquipmentSlot.HEAD);
                    }
                }
            }
        }

        if (chestObj.get("type").toString().equalsIgnoreCase("origins:armor_value")) {
            String comparisonc = chestObj.get("comparison").toString();
            String comparisontoc = chestObj.get("compare_to").toString();
            if (!chestb) return;
            ItemStack item = p.getInventory().getChestplate();
            if (item != null) {
                double armorValue = getArmorValue(item);
                double compareValue = Double.parseDouble(comparisontoc);
                if (compareValues(armorValue, comparisonc, compareValue)) {
                    OriginPlayerUtils.moveEquipmentInventory(p, EquipmentSlot.CHEST);
                }
            }
        } else if (chestObj.get("type").toString().equalsIgnoreCase("origins:ingredient")) {
            if (!chestb) return;
            if (p.getInventory().getChestplate() != null) {
                Map<String, Object> ingredientMap = (Map<String, Object>) chestObj.get("ingredient");
                if (ingredientMap.containsKey("item")) {
                    String itemValue = ingredientMap.get("item").toString();
                    String item = null;
                    if (itemValue.contains(":")) {
                        item = itemValue.split(":")[1];
                    } else {
                        item = itemValue;
                    }
                    if (p.getInventory().getChestplate().getType().equals(Material.valueOf(item.toUpperCase()))) {
                        OriginPlayerUtils.moveEquipmentInventory(p, EquipmentSlot.CHEST);
                    }
                }
            }
        }

        if (legsObj.get("type").toString().equalsIgnoreCase("origins:armor_value")) {
            String comparisonl = legsObj.get("comparison").toString();
            String comparisontol = legsObj.get("compare_to").toString();
            if (!legsb) return;
            ItemStack item = p.getInventory().getLeggings();
            if (item != null) {
                double armorValue = getArmorValue(item);
                double compareValue = Double.parseDouble(comparisontol);
                if (compareValues(armorValue, comparisonl, compareValue)) {
                    OriginPlayerUtils.moveEquipmentInventory(p, EquipmentSlot.LEGS);
                }
            }
        } else if (legsObj.get("type").toString().equalsIgnoreCase("origins:ingredient")) {
            if (!legsb) return;
            if (p.getInventory().getLeggings() != null) {
                Map<String, Object> ingredientMap = (Map<String, Object>) legsObj.get("ingredient");
                if (ingredientMap.containsKey("item")) {
                    String itemValue = ingredientMap.get("item").toString();
                    String item = null;
                    if (itemValue.contains(":")) {
                        item = itemValue.split(":")[1];
                    } else {
                        item = itemValue;
                    }
                    if (p.getInventory().getLeggings().getType().equals(Material.valueOf(item.toUpperCase()))) {
                        OriginPlayerUtils.moveEquipmentInventory(p, EquipmentSlot.LEGS);
                    }
                }
            }
        }

        if (feetObj.get("type").toString().equalsIgnoreCase("origins:armor_value")) {
            String comparisonf = feetObj.get("comparison").toString();
            String comparisontof = feetObj.get("compare_to").toString();
            if (!feetb) return;
            ItemStack item = p.getInventory().getBoots();
            if (item != null) {
                double armorValue = getArmorValue(item);
                double compareValue = Double.parseDouble(comparisontof);
                if (compareValues(armorValue, comparisonf, compareValue)) {
                    OriginPlayerUtils.moveEquipmentInventory(p, EquipmentSlot.FEET);
                }
            }
        } else if (feetObj.get("type").toString().equalsIgnoreCase("origins:ingredient")) {
            if (!feetb) return;
            if (p.getInventory().getBoots() != null) {
                Map<String, Object> ingredientMap = (Map<String, Object>) feetObj.get("ingredient");
                if (ingredientMap.containsKey("item")) {
                    String itemValue = ingredientMap.get("item").toString();
                    String item = null;
                    if (itemValue.contains(":")) {
                        item = itemValue.split(":")[1];
                    } else {
                        item = itemValue;
                    }
                    if (p.getInventory().getBoots().getType().equals(Material.valueOf(item.toUpperCase()))) {
                        OriginPlayerUtils.moveEquipmentInventory(p, EquipmentSlot.FEET);
                    }
                }
            }
        }
    }

    @Override
    public void run(Player p) {

    }

    @Override
    public String getPowerFile() {
        return "origins:restrict_armor";
    }

    @Override
    public ArrayList<Player> getPowerArray() {
        return restrict_armor;
    }
}
