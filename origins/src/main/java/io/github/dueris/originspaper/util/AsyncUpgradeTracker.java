package io.github.dueris.originspaper.util;

import io.github.dueris.calio.util.holder.TriPair;
import io.github.dueris.originspaper.OriginsPaper;
import io.github.dueris.originspaper.origin.Origin;
import io.github.dueris.originspaper.origin.OriginLayer;
import io.github.dueris.originspaper.registry.ApoliRegistries;
import io.github.dueris.originspaper.storage.OriginComponent;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AsyncUpgradeTracker implements Listener {
	public static ConcurrentHashMap<Origin, TriPair<ResourceLocation, ResourceLocation, String>/*ResourceLocation advancement, NamespacedKey identifier, String announcement*/> upgrades = new ConcurrentHashMap<>();
	public static String NO_ANNOUNCEMENT = "no_announcement_found";

	@EventHandler
	public void startEvent(ServerLoadEvent e) {
		new BukkitRunnable() {
			@Override
			public void run() {
				MinecraftServer server = OriginsPaper.server;
				for (Map.Entry<Origin, TriPair<ResourceLocation, ResourceLocation, String>> entry : upgrades.entrySet()) {
					for (CraftPlayer player : ((CraftServer) Bukkit.getServer()).getOnlinePlayers()) {
						for (OriginLayer layer : ApoliRegistries.ORIGIN_LAYER) {
							if (OriginComponent.getOrigin(player, layer).equals(entry.getKey())) {
								ResourceLocation advancement = entry.getValue().a();
								ResourceLocation originToSet = entry.getValue().b();
								String announcement = entry.getValue().c();

								AdvancementHolder advancementHolder = server.getAdvancements().get(advancement);
								if (advancementHolder == null) {
									OriginsPaper.LOGGER.error("Advancement \"{}\" did not exist but was referenced in the an origin upgrade!", advancement);
								}

								AdvancementProgress progress = player.getHandle().getAdvancements().getOrStartProgress(advancementHolder);
								if (progress.isDone()) {
									OriginComponent.setOrigin(player, layer, ApoliRegistries.ORIGIN.get(originToSet));
									if (!announcement.equals(NO_ANNOUNCEMENT)) {
										player.sendMessage(announcement);
									}
								}
							}
						}
					}
				}
			}
		}.runTaskTimerAsynchronously(OriginsPaper.getPlugin(), 0, 5);
	}
}
