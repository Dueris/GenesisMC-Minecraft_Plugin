package me.dueris.genesismc.factory.powers.value_modifying;

import me.dueris.genesismc.entity.OriginPlayerUtils;
import me.dueris.genesismc.factory.actions.Actions;
import me.dueris.genesismc.factory.conditions.ConditionExecutor;
import me.dueris.genesismc.factory.conditions.item.ItemCondition;
import me.dueris.genesismc.factory.powers.CraftPower;
import me.dueris.genesismc.factory.powers.block.RecipePower;
import me.dueris.genesismc.utils.ErrorSystem;
import me.dueris.genesismc.utils.PowerContainer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import static me.dueris.genesismc.factory.powers.value_modifying.ValueModifyingSuperClass.modify_crafting;

public class ModifyCraftingPower extends CraftPower implements Listener {

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

    @Override
    public void run(Player p) {

    }

    @EventHandler
    public void runD(PrepareItemCraftEvent e) {
        Player p = (Player) e.getInventory().getHolder();
        if (modify_crafting.contains(p)) {
            if (e.getRecipe() == null) return;
            if (e.getInventory().getResult() == null) return;
            for (me.dueris.genesismc.utils.LayerContainer layer : me.dueris.genesismc.factory.CraftApoli.getLayers()) {
                ConditionExecutor conditionExecutor = me.dueris.genesismc.GenesisMC.getConditionExecutor();
                for (PowerContainer power : OriginPlayerUtils.getMultiPowerFileFromType(p, getPowerFile(), layer)) {
                    if (conditionExecutor.check("condition", "condition", p, power, "apoli:modify_crafting", p, null, p.getLocation().getBlock(), null, p.getItemInHand(), null)) {
//                        if (conditionExecutor.check("item_condition", "item_condition", p, power, "apoli:modify_crafting", p, null, p.getLocation().getBlock(), null, e.getInventory().getResult(), null)) {
                            String currKey = RecipePower.computeTag(e.getRecipe());
                            if(currKey == null) return;
                            String provKey = power.getStringOrDefault("recipe", currKey);
                            boolean set = false;
                            if(currKey == provKey){ // Matched on crafting
                                Optional<Boolean> condition = ConditionExecutor.itemCondition.check(power.get("item_condition"), p, null, p.getLocation().getBlock(), null, e.getInventory().getResult(), null);
                                if(condition.isPresent()){
                                    if(condition.get()){
                                        set = true;
                                    }
                                }else{
                                    set = true;
                                }
                            }
                            if(set){
                                e.getInventory().setResult(RecipePower.computeResult(power.get("result")));
                                Actions.EntityActionType(p, power.getAction("entity_action"));
                                Actions.ItemActionType(e.getInventory().getResult(), power.getItemAction());
                                Actions.BlockActionType(p.getLocation(), power.getBlockAction());
                            }
//                        } else {
//                            setActive(p, power.getTag(), false);
//                        }
                    } else {
                        setActive(p, power.getTag(), false);
                    }
                }
            }
        }
    }

    @Override
    public String getPowerFile() {
        return "apoli:modify_crafting";
    }

    @Override
    public ArrayList<Player> getPowerArray() {
        return modify_crafting;
    }
}
