package me.dueris.genesismc.factory.powers.apoli;

import me.dueris.genesismc.factory.CraftApoli;
import me.dueris.genesismc.factory.conditions.ConditionExecutor;
import me.dueris.genesismc.factory.powers.CraftPower;
import me.dueris.genesismc.factory.powers.apoli.superclass.ValueModifyingSuperClass;
import me.dueris.genesismc.registry.LayerContainer;
import me.dueris.genesismc.registry.PowerContainer;
import me.dueris.genesismc.util.entity.OriginPlayerAccessor;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.function.BinaryOperator;

import static me.dueris.genesismc.factory.powers.apoli.AttributeHandler.getOperationMappingsInteger;

public class ModifyEnchantmentLevel extends CraftPower {

    @Override
    public void run(Player p) {
        if (getPowerArray().contains(p)) {
            for (LayerContainer layer : CraftApoli.getLayers()) {
                ValueModifyingSuperClass valueModifyingSuperClass = new ValueModifyingSuperClass();
                ConditionExecutor conditionExecutor = me.dueris.genesismc.GenesisMC.getConditionExecutor();
                for (PowerContainer power : OriginPlayerAccessor.getMultiPowerFileFromType(p, getPowerFile(), layer)) {
                    HashSet<ItemStack> items = new HashSet<>(Arrays.stream(p.getInventory().getArmorContents()).toList());
                    items.add(p.getInventory().getItemInMainHand());
                    for (ItemStack item : items)
                        if (conditionExecutor.check("condition", "conditions", p, power, getPowerFile(), p, null, p.getLocation().getBlock(), null, item, null)) {
                            if (conditionExecutor.check("item_condition", "item_conditions", p, power, getPowerFile(), p, null, p.getLocation().getBlock(), null, item, null)) {
                                for (HashMap<String, Object> modifier : power.getPossibleModifiers("modifier", "modifiers")) {
                                    Enchantment enchant = Enchantment.getByKey(NamespacedKey.fromString(power.getString("enchantment")));
                                    if (item.containsEnchantment(enchant)) {
                                        item.removeEnchantment(enchant);
                                    }
                                    int result = 1;
                                    Integer value = Integer.valueOf(modifier.get("value").toString());
                                    String operation = modifier.get("operation").toString();
                                    BinaryOperator mathOperator = getOperationMappingsInteger().get(operation);
                                    if (mathOperator != null) {
                                        result = Integer.valueOf(String.valueOf(mathOperator.apply(0, value)));
                                    }
                                    if (result < 0) {
                                        result = 1;
                                    }
                                    try {
                                        item.addEnchantment(enchant, result);
                                    } catch (Exception e) {
                                        // ignore. -- cannot apply enchant to itemstack
                                    }
                                }
                            }
                        }
                }
            }
        }
    }

    @Override
    public String getPowerFile() {
        return "apoli:modify_enchantment_level";
    }

    @Override
    public ArrayList<Player> getPowerArray() {
        return ValueModifyingSuperClass.modify_enchantment_level;
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

}
