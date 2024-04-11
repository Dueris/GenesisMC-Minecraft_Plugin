package me.dueris.genesismc.factory.powers.apoli;

import me.dueris.genesismc.factory.CraftApoli;
import me.dueris.genesismc.factory.conditions.ConditionExecutor;
import me.dueris.genesismc.factory.powers.CraftPower;
import me.dueris.genesismc.registry.registries.Layer;
import me.dueris.genesismc.registry.registries.Power;
import me.dueris.genesismc.util.entity.OriginPlayerAccessor;
import org.bukkit.craftbukkit.v1_20_R3.block.CraftBlock;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;

import static me.dueris.genesismc.factory.powers.apoli.superclass.PreventSuperClass.prevent_block_use;

public class PreventBlockUse extends CraftPower implements Listener {


    @EventHandler
    public void run(BlockPlaceEvent e) {
        if (prevent_block_use.contains(e.getPlayer())) {
            for (Layer layer : CraftApoli.getLayersFromRegistry()) {
                ConditionExecutor conditionExecutor = me.dueris.genesismc.GenesisMC.getConditionExecutor();
                for (Power power : OriginPlayerAccessor.getMultiPowerFileFromType(e.getPlayer(), getPowerFile(), layer)) {
                    if (ConditionExecutor.testBlock(power.get("block_condition"), (CraftBlock) e.getBlock()) && ConditionExecutor.testEntity(power.get("condition"), (CraftEntity) e.getPlayer())) {
                        e.setCancelled(true);
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
        return "apoli:prevent_block_used";
    }

    @Override
    public ArrayList<Player> getPowerArray() {
        return prevent_block_use;
    }
}
