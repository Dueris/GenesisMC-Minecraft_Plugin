package me.dueris.genesismc.factory.powers.apoli;

import me.dueris.genesismc.factory.CraftApoli;
import me.dueris.genesismc.factory.conditions.ConditionExecutor;
import me.dueris.genesismc.factory.powers.CraftPower;
import me.dueris.genesismc.registry.registries.Layer;
import me.dueris.genesismc.registry.registries.Power;
import me.dueris.genesismc.util.LangConfig;
import me.dueris.genesismc.util.Utils;
import me.dueris.genesismc.util.entity.OriginPlayerAccessor;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.BinaryOperator;

public class AttributeConditioned extends CraftPower implements Listener {

    private static final HashMap<Player, Boolean> applied = new HashMap<>();

    public static void executeAttributeModify(String operation, Attribute attribute_modifier, int base_value, Player p, int value) {
        Map<String, BinaryOperator<Integer>> operationMap = new HashMap<>();
        operationMap.put("addition", Integer::sum);
        operationMap.put("subtraction", (a, b) -> a - b);
        operationMap.put("multiplication", (a, b) -> a * b);
        operationMap.put("division", (a, b) -> a / b);
        operationMap.put("multiply_base", (a, b) -> a + (a * b));
        operationMap.put("multiply_total", (a, b) -> a * (1 + b));
        operationMap.put("set_total", (a, b) -> b);

        Random random = new Random();

        operationMap.put("add_random_max", (a, b) -> a + random.nextInt(b));
        operationMap.put("subtract_random_max", (a, b) -> a - random.nextInt(b));
        operationMap.put("multiply_random_max", (a, b) -> a * random.nextInt(b));
        operationMap.put("divide_random_max", (a, b) -> a / random.nextInt(b));

        BinaryOperator mathOperator = operationMap.get(operation);
        if (mathOperator != null) {
            int result = (int) mathOperator.apply(base_value, value);
            p.getAttribute(attribute_modifier).setBaseValue(result);
        } else {
            Bukkit.getLogger().warning(LangConfig.getLocalizedString(p, "powers.errors.attribute"));
        }
        p.sendHealthUpdate();
    }

    public static void executeAttributeModify(String operation, Attribute attribute_modifier, double base_value, Player p, Double value) {
        Map<String, BinaryOperator<Integer>> operationMap = new HashMap<>();
        operationMap.put("addition", Integer::sum);
        operationMap.put("subtraction", (a, b) -> a - b);
        operationMap.put("multiplication", (a, b) -> a * b);
        operationMap.put("division", (a, b) -> a / b);
        operationMap.put("multiply_base", (a, b) -> a + (a * b));
        operationMap.put("multiply_total", (a, b) -> a * (1 + b));
        operationMap.put("set_total", (a, b) -> b);

        Random random = new Random();

        operationMap.put("add_random_max", (a, b) -> a + random.nextInt(b));
        operationMap.put("subtract_random_max", (a, b) -> a - random.nextInt(b));
        operationMap.put("multiply_random_max", (a, b) -> a * random.nextInt(b));
        operationMap.put("divide_random_max", (a, b) -> a / random.nextInt(b));

        BinaryOperator operator = Utils.getOperationMappingsDouble().get(operation);
        if (operator != null) {
            double result = Double.valueOf(String.valueOf(operator.apply(base_value, value)));
            p.getAttribute(attribute_modifier).setBaseValue(result);
        } else {
            Bukkit.getLogger().warning(LangConfig.getLocalizedString(p, "powers.errors.attribute"));
        }
        p.sendHealthUpdate();
    }

    public void executeConditionAttribute(Player p) {
        Map<String, BinaryOperator<Integer>> operationMap = new HashMap<>();
        operationMap.put("addition", Integer::sum);
        operationMap.put("subtraction", (a, b) -> a - b);
        operationMap.put("multiplication", (a, b) -> a * b);
        operationMap.put("division", (a, b) -> a / b);
        operationMap.put("multiply_base", (a, b) -> a + (a * b));
        operationMap.put("multiply_total", (a, b) -> a * (1 + b));
        operationMap.put("set_total", (a, b) -> b);

        Random random = new Random();

        operationMap.put("add_random_max", (a, b) -> a + random.nextInt(b));
        operationMap.put("subtract_random_max", (a, b) -> a - random.nextInt(b));
        operationMap.put("multiply_random_max", (a, b) -> a * random.nextInt(b));
        operationMap.put("divide_random_max", (a, b) -> a / random.nextInt(b));

        for (Layer layer : CraftApoli.getLayersFromRegistry()) {
            for (Power power : OriginPlayerAccessor.getMultiPowerFileFromType(p, getPowerFile(), layer)) {
                if (power == null) continue;
                for (HashMap<String, Object> modifier : power.getJsonListSingularPlural("modifier", "modifiers")) {
                    if (!ConditionExecutor.testEntity(power.get("condition"), (CraftEntity) p)) return;
                    Attribute attribute_modifier = Attribute.valueOf(NamespacedKey.fromString(modifier.get("attribute").toString()).asString().split(":")[1].replace(".", "_").toUpperCase());
                    double val = Float.valueOf(modifier.get("value").toString());
                    double baseVal = p.getAttribute(attribute_modifier).getBaseValue();
                    String operation = modifier.get("operation").toString();
                    BinaryOperator operator = Utils.getOperationMappingsDouble().get(operation);
                    if (operator != null) {
                        double result = Double.valueOf(String.valueOf(operator.apply(baseVal, val)));
                        p.getAttribute(attribute_modifier).setBaseValue(result);
                    } else {
                        Bukkit.getLogger().warning(LangConfig.getLocalizedString(p, "powers.errors.attribute"));
                    }
                    p.sendHealthUpdate();
                }
            }

        }
    }

    public void inverseConditionAttribute(Player p) {
        Map<String, BinaryOperator<Integer>> operationMap = new HashMap<>();
        operationMap.put("addition", (a, b) -> a - b);
        operationMap.put("subtraction", Integer::sum);
        operationMap.put("multiplication", (a, b) -> a / b);
        operationMap.put("division", (a, b) -> a * b);
        operationMap.put("multiply_base", (a, b) -> a / (1 + b));
        operationMap.put("multiply_total", (a, b) -> a / (1 + b));
        operationMap.put("set_total", (a, b) -> a);

        Random random = new Random();

        operationMap.put("add_random_max", (a, b) -> a - random.nextInt(b));
        operationMap.put("subtract_random_max", (a, b) -> a + random.nextInt(b));
        operationMap.put("multiply_random_max", (a, b) -> a / random.nextInt(b));
        operationMap.put("divide_random_max", (a, b) -> a * random.nextInt(b));

        for (Layer layer : CraftApoli.getLayersFromRegistry()) {
            for (Power power : OriginPlayerAccessor.getMultiPowerFileFromType(p, getPowerFile(), layer)) {
                if (power == null) continue;

                for (HashMap<String, Object> modifier : power.getJsonListSingularPlural("modifier", "modifiers")) {
                    Attribute attribute_modifier = Attribute.valueOf(NamespacedKey.fromString(modifier.get("attribute").toString()).asString().split(":")[1].replace(".", "_").toUpperCase());
                    if (modifier.get("value") instanceof Integer) {
                        int value = Integer.valueOf(modifier.get("value").toString());
                        int base_value = (int) p.getAttribute(attribute_modifier).getBaseValue();
                        String operation = String.valueOf(modifier.get("operation"));
                        executeAttributeModify(operation, attribute_modifier, base_value, p, -value);
                    } else if (modifier.get("value") instanceof Double) {
                        double value = Double.valueOf(modifier.get("value").toString());
                        double base_value = p.getAttribute(attribute_modifier).getBaseValue();
                        String operation = String.valueOf(modifier.get("operation"));
                        executeAttributeModify(operation, attribute_modifier, base_value, p, -value);
                    }
                }
            }

        }
    }


    @EventHandler
    public void join(PlayerJoinEvent e) {
        applied.put(e.getPlayer(), false);
    }

    @Override
    public void run(Player p) {
        for (Layer layer : CraftApoli.getLayersFromRegistry()) {
            for (Power power : OriginPlayerAccessor.getMultiPowerFileFromType(p, getPowerFile(), layer)) {
                if (conditioned_attribute.contains(p)) {
                    if (!applied.containsKey(p)) {
                        applied.put(p, false);
                    }
                    ConditionExecutor conditionExecutor = me.dueris.genesismc.GenesisMC.getConditionExecutor();
                    if (ConditionExecutor.testEntity(power.get("condition"), (CraftEntity) p)) {
                        if (!applied.get(p)) {
                            executeConditionAttribute(p);
                            applied.put(p, true);
                            setActive(p, power.getTag(), true);
                        }
                    } else {
                        if (applied.get(p)) {
                            inverseConditionAttribute(p);
                            applied.put(p, false);
                            setActive(p, power.getTag(), false);
                        }
                    }
                }
            }
        }
    }

    @Override
    public String getPowerFile() {
        return "apoli:conditioned_attribute";
    }

    @Override
    public ArrayList<Player> getPowerArray() {
        return conditioned_attribute;
    }
}
