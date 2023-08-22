package me.dueris.genesismc.core.factory.powers.OriginsMod.value_modifying;

import me.dueris.genesismc.core.entity.OriginPlayer;
import me.dueris.genesismc.core.factory.conditions.ConditionExecutor;
import me.dueris.genesismc.core.utils.ErrorSystem;
import me.dueris.genesismc.core.utils.OriginContainer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.function.BinaryOperator;

import static me.dueris.genesismc.core.factory.powers.OriginsMod.player.attributes.AttributeHandler.getOperationMappingsFloat;
import static me.dueris.genesismc.core.factory.powers.OriginsMod.value_modifying.ValueModifyingSuperClass.modify_damage_taken;

public class ModifyDamageTakenPower extends ValueModifyingSuperClass implements Listener {
    @EventHandler
    public void damageEVENT(EntityDamageByEntityEvent e){
        if(e.getEntity() instanceof Player){
            Player p = (Player) e.getEntity();
            if(modify_damage_taken.contains(p)){
                for (OriginContainer origin : OriginPlayer.getOrigin(p).values()) {
                    ValueModifyingSuperClass valueModifyingSuperClass = new ValueModifyingSuperClass();
                    try {
                        ConditionExecutor conditionExecutor = new ConditionExecutor();
                        if (conditionExecutor.check("bientity_condition", "bientity_conditions", p, origin, "origins:modify_damage_taken", e, e.getDamager())) {
                            for(HashMap<String, Object> modifier : origin.getPowerFileFromType("origins:modify_damage_taken").getConditionFromString("modifier", "modifiers")){
                                Float value = Float.valueOf(modifier.get("value").toString());
                                String operation = modifier.get("operation").toString();
                                BinaryOperator mathOperator = getOperationMappingsFloat().get(operation);
                                if (mathOperator != null) {
                                    float result = (float) mathOperator.apply(e.getDamage(), value);
                                    e.setDamage(result);
                                }
                            }

                        }
                    } catch (Exception ev) {
                        ErrorSystem errorSystem = new ErrorSystem();
                        errorSystem.throwError("unable to get bi-entity", "origins:modify_damage_taken", p, origin, OriginPlayer.getLayer(p, origin));
                        ev.printStackTrace();
                    }
                }
            }
        }
    }
}
