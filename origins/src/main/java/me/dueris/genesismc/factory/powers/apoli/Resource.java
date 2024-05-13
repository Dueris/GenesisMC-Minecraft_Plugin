package me.dueris.genesismc.factory.powers.apoli;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.Pair;
import me.dueris.calio.data.FactoryData;
import me.dueris.calio.data.annotations.Register;
import me.dueris.calio.data.factory.FactoryJsonObject;
import me.dueris.calio.data.types.RequiredInstance;
import me.dueris.calio.registry.Registrar;
import me.dueris.genesismc.GenesisMC;
import me.dueris.genesismc.event.PowerUpdateEvent;
import me.dueris.genesismc.factory.CraftApoli;
import me.dueris.genesismc.factory.actions.Actions;
import me.dueris.genesismc.factory.data.types.Comparison;
import me.dueris.genesismc.factory.data.types.HudRender;
import me.dueris.genesismc.factory.powers.holder.PowerType;
import me.dueris.genesismc.registry.Registries;
import me.dueris.genesismc.util.DataConverter;
import me.dueris.genesismc.util.TextureLocation;
import me.dueris.genesismc.util.Utils;
import me.dueris.genesismc.util.entity.PowerHolderComponent;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BinaryOperator;

import static me.dueris.genesismc.util.TextureLocation.textureMap;

public class Resource extends PowerType implements Listener, ResourcePower {
	public static HashMap<String, Bar> serverLoadedBars = new HashMap<>(); // IDENTIFIER || BAR_IMPL
	public static HashMap<Player, List<Bar>> currentlyDisplayed = new HashMap<>();

	static {
		GenesisMC.preShutdownTasks.add(() -> {
			serverLoadedBars.values().forEach(Bar::delete);
			currentlyDisplayed.forEach((player, list) -> list.forEach(Bar::delete));
		});
	}

	private final int min;
	private final int max;
	private final HudRender hudRender;
	private final Integer startValue;
	private final FactoryJsonObject minAction;
	private final FactoryJsonObject maxAction;

	@Register
	public Resource(String name, String description, boolean hidden, FactoryJsonObject condition, int loading_priority, int min, int max, FactoryJsonObject hudRender, Optional startValue, FactoryJsonObject minAction, FactoryJsonObject maxAction) {
		super(name, description, hidden, condition, loading_priority);
		this.min = min;
		this.max = max;
		this.hudRender = HudRender.createHudRender(hudRender);
		this.startValue = startValue.isEmpty() ? min : ((JsonElement) startValue.get()).getAsInt();
		this.minAction = minAction;
		this.maxAction = maxAction;
	}

	public static FactoryData registerComponents(FactoryData data) {
		return PowerType.registerComponents(data).ofNamespace(GenesisMC.apoliIdentifier("resource"))
			.addRequiredInstance("min", int.class, new RequiredInstance())
			.addRequiredInstance("max", int.class, new RequiredInstance())
			.add("hud_render", FactoryJsonObject.class, new FactoryJsonObject(new JsonObject()))
			.add("start_value", Optional.class, Optional.empty())
			.add("min_action", FactoryJsonObject.class, new FactoryJsonObject(new JsonObject()))
			.add("max_action", FactoryJsonObject.class, new FactoryJsonObject(new JsonObject()));
	}

	public static Optional<Bar> getDisplayedBar(Entity player, String identifier) {
		currentlyDisplayed.putIfAbsent((Player) player, new ArrayList<>());
		for (Bar bar : currentlyDisplayed.get(player)) {
			if (bar.power.getTag().equalsIgnoreCase(identifier)) return Optional.of(bar);
		}
		return Optional.empty();
	}

	protected static KeyedBossBar createRender(String title, double currentProgress, ResourcePower power, Player player) {
		NamespacedKey f = NamespacedKey.fromString(power.getTag() + "_bar_server_loaded");
		KeyedBossBar bossBar = Bukkit.createBossBar(
			player == null ? f : NamespacedKey.fromString(power.getTag() + "_bar_" + player.getName().toLowerCase()),
			title, Bar.getBarColor(power.getHudRender()), BarStyle.SEGMENTED_6);
		bossBar.setProgress(currentProgress);
		return bossBar;
	}

	@EventHandler
	public void preLoad(ServerLoadEvent e) {
		// We preload the bars and then display a clone of them to each player
		((Registrar<PowerType>) GenesisMC.getPlugin().registry.retrieve(Registries.CRAFT_POWER)).values().stream()
			.filter(p -> p.getType().equalsIgnoreCase(getType())).forEach(power -> {
				Bar bar = new Bar((Resource) power, null);
				serverLoadedBars.put(power.getTag(), bar);
			});
		for (Player player : Bukkit.getOnlinePlayers())
			PowerHolderComponent.getPowers(player, Resource.class).forEach(power -> powerAdd(new PowerUpdateEvent(player, power, false, false)));
	}

	@EventHandler
	public void powerAdd(PowerUpdateEvent e) {
		if (e.getPower() instanceof Resource && e.getPower().getTag().equalsIgnoreCase(getTag())) {
			currentlyDisplayed.putIfAbsent(e.getPlayer(), new ArrayList<>());
			if (!e.isRemoved()) {
				// Power is added, display bar
				if (serverLoadedBars.containsKey(e.getPower().getTag())) {
					Resource.Bar displayed = serverLoadedBars.get(e.getPower().getTag());
					if (!currentlyDisplayed.get(e.getPlayer()).contains(displayed)) {
						currentlyDisplayed.get(e.getPlayer()).add(displayed.cloneForPlayer(e.getPlayer()));
					}
				}
			} else if (currentlyDisplayed.containsKey(e.getPlayer())) {
				// Power is removed, remove the bar
				Resource.Bar cD = getDisplayedBar(e.getPlayer(), e.getPower().getTag()).orElse(null);
				if (cD == null) return;
				cD.delete();
			}
		}
	}

	@EventHandler
	public void leave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		StringBuilder cooldownBuilder = new StringBuilder();
		cooldownBuilder.append("[");
		Cooldown.cooldowns.putIfAbsent(p, new ArrayList<>());
		for (Pair<KeyedBossBar, ResourcePower> barPair : Cooldown.cooldowns.get(p)) {
			cooldownBuilder.append(barPair.left().getKey().asString() + "<::>" + barPair.left().getProgress());
			cooldownBuilder.append(",");
		}
		cooldownBuilder.append("]");
		p.getPersistentDataContainer().set(GenesisMC.apoliIdentifier("current_cooldowns"), PersistentDataType.STRING, new String(cooldownBuilder).replace(",]", "]"));
		if (currentlyDisplayed.containsKey(p)) {
			currentlyDisplayed.get(p).forEach(Bar::delete);
			currentlyDisplayed.get(p).clear();
		}
		if (Cooldown.cooldowns.containsKey(p)) {
			Cooldown.cooldowns.get(p).forEach(pair -> Bukkit.getServer().removeBossBar(pair.left().getKey()));
			Cooldown.cooldowns.get(p).clear();
		}
	}

	@EventHandler
	public void join(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (p.getPersistentDataContainer().has(GenesisMC.apoliIdentifier("current_cooldowns"))) {
			String encoded = p.getPersistentDataContainer().get(GenesisMC.apoliIdentifier("current_cooldowns"), PersistentDataType.STRING);
			encoded = encoded.replace("[", "").replace("]", "");
			if (encoded.equalsIgnoreCase("")) return;
			Arrays.stream(encoded.split(",")).forEach(key -> {
				String a = key.split("<::>")[0];
				double b = Double.parseDouble(key.split("<::>")[1]);
				PowerType power = CraftApoli.getPowerFromTag(a.split("_cooldown_")[0]);
				if (power instanceof CooldownPower cooldownPower) {
					Cooldown.addCooldown(p, cooldownPower.getCooldown(), cooldownPower, b);
				}
			});
		}
	}

	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}

	public HudRender getHudRender() {
		return hudRender;
	}

	@Nullable
	public Integer getStartValue() {
		return startValue;
	}

	public FactoryJsonObject getMinAction() {
		return minAction;
	}

	public FactoryJsonObject getMaxAction() {
		return maxAction;
	}

	public static class Bar {
		String title;
		Resource power;
		int min;
		int max;
		Double currentProgress; // Use lang class to use Number#intValue()
		Integer mappedProgress;
		KeyedBossBar renderedBar;
		double oneInc;

		Bar(Resource power, Player player) {
			this.title = Utils.getNameOrTag(power).left();
			this.power = power;
			this.min = power.getMin();
			this.max = power.getMax();
			this.currentProgress = (double) (power.getStartValue() != null ? power.getStartValue() : this.min);
			this.mappedProgress = this.currentProgress.intValue();
			this.renderedBar = Resource.createRender(title, formatForFirstRender(this.currentProgress), power, player);
			this.renderedBar.setVisible(true);
			this.oneInc = 1.0 / this.max;
			if (player != null) {
				this.renderedBar.addPlayer(player);
			}

			change(power.getStartValue() != null ? power.getStartValue() : this.min, "set", false);
		}

		public static BarColor getBarColor(HudRender element) {
			if (element != null && element.getSpriteLocation() != null) {
				TextureLocation loc = ((Registrar<TextureLocation>) GenesisMC.getPlugin().registry.retrieve(Registries.TEXTURE_LOCATION))
					.get(DataConverter.resolveTextureLocationNamespace(NamespacedKey.fromString(element.getSpriteLocation())));
				long index = (element.getBarIndex()) + 1;
				BarColor color = textureMap.get(loc.getKey().asString() + "/-/" + index);
				return color != null ? color : BarColor.WHITE;
			}
			return BarColor.WHITE;
		}

		public Bar cloneForPlayer(Player player) {
			return new Bar(this.power, player);
		}

		public void delete() {
			this.renderedBar.setVisible(false);
			this.renderedBar.setProgress(0);
			this.renderedBar.removeAll();
			Bukkit.getServer().removeBossBar(this.renderedBar.getKey());
		}

		public void change(int by, String operation, boolean updateMapped) {
			Map<String, BinaryOperator<Double>> operator = Utils.getOperationMappingsDouble();
			double change = oneInc * by;
			this.renderedBar.setProgress(preVerifyProgress(operator.get(operation).apply(this.renderedBar.getProgress(), change)));
			this.currentProgress = this.renderedBar.getProgress();
			if (updateMapped) {
				int f = operator.get(operation).apply(this.mappedProgress.doubleValue(), (double) by).intValue();
				if (f < 0) f = 0;
				this.mappedProgress = f;
			}
			this.renderedBar.getPlayers().forEach(entity -> {
				if (this.renderedBar.getProgress() == 1.0) {
					Actions.executeEntity(entity, this.power.getMaxAction());
				} else if (this.renderedBar.getProgress() == 0.0) {
					Actions.executeEntity(entity, this.power.getMinAction());
				}
			});
		}

		public void change(int by, String operation) {
			change(by, operation, true);
		}

		public boolean meetsComparison(Comparison comparison, double e) {
			return comparison.compare(this.mappedProgress, e);
		}

		private double formatForFirstRender(double e) {
			if (e == 0) return 0;
			double f = 1.0 / e;
			if (f > 1) return 1;
			if (f < 0) return 0;
			return f;
		}

		private double preVerifyProgress(double e) {
			if (e > 1) return 1.0;
			if (e < 0) return 0.0;
			return e;
		}
	}

}
