package me.dueris.genesismc.factory.powers.apoli;

import com.mojang.brigadier.StringReader;
import io.papermc.paper.util.MCUtil;
import me.dueris.calio.util.MiscUtils;
import me.dueris.genesismc.GenesisMC;
import me.dueris.genesismc.event.KeybindTriggerEvent;
import me.dueris.genesismc.factory.CraftApoli;
import me.dueris.genesismc.factory.conditions.ConditionExecutor;
import me.dueris.genesismc.factory.powers.CraftPower;
import me.dueris.genesismc.registry.registries.Layer;
import me.dueris.genesismc.registry.registries.Power;
import me.dueris.genesismc.util.KeybindingUtils;
import me.dueris.genesismc.util.entity.OriginPlayerAccessor;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.phys.Vec3;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FireProjectile extends CraftPower implements Listener {

    private static final ArrayList<Player> doubleFirePatch = new ArrayList<>();
    public static HashMap<Player, ArrayList<String>> in_continuous = new HashMap<>();
    public static ArrayList<Player> enderian_pearl = new ArrayList<>();
    public static ArrayList<Player> in_cooldown_patch = new ArrayList<>();

    public static void addCooldownPatch(Player p) {
        in_cooldown_patch.add(p);
        new BukkitRunnable() {
            @Override
            public void run() {
                in_cooldown_patch.remove(p);
            }
        }.runTaskLater(GenesisMC.getPlugin(), 5);
    }

    @EventHandler
    public void inContinuousFix(KeybindTriggerEvent e) {
        Player p = e.getPlayer();
        for (Layer layer : CraftApoli.getLayersFromRegistry()) {
            if (fire_projectile.contains(p)) {
                for (Power power : OriginPlayerAccessor.getMultiPowerFileFromType(p, getType(), layer)) {
                    if (KeybindingUtils.isKeyActive(power.getJsonObject("key").getStringOrDefault("key", "key.origins.primary_active"), p)) {
                        in_continuous.putIfAbsent(p, new ArrayList<>());
                        if (power.getJsonObject("key").getBooleanOrDefault("continuous", false)) {
                            if (in_continuous.get(p).contains(power.getJsonObject("key").getStringOrDefault("key", "key.origins.primary_active"))) {
                                in_continuous.get(p).remove(power.getJsonObject("key").getStringOrDefault("key", "key.origins.primary_active"));
                            } else {
                                in_continuous.get(p).add(power.getJsonObject("key").getStringOrDefault("key", "key.origins.primary_active"));
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void teleportDamgeOff(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        if (enderian_pearl.contains(e.getPlayer()) && e.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            e.setCancelled(true);
            p.teleportAsync(e.getTo());
            p.setFallDistance(0);
        }

    }

    @EventHandler
    public void keybindPress(KeybindTriggerEvent e) {
        Player p = e.getPlayer();
        ArrayList<Player> peopladf = new ArrayList<>();
        if (doubleFirePatch.contains(p)) return;
        for (Layer layer : CraftApoli.getLayersFromRegistry()) {
            for (Power power : OriginPlayerAccessor.getMultiPowerFileFromType(p, getType(), layer)) {
                if (fire_projectile.contains(p)) {
                    if (ConditionExecutor.testEntity(power.getJsonObject("condition"), (CraftEntity) p)) {
                        if (!Cooldown.isInCooldown(p, power)) {
                            if (KeybindingUtils.isKeyActive(power.getJsonObject("key").getStringOrDefault("key", "key.origins.primary_active"), p)) {
                                int cooldown = power.getNumberOrDefault("cooldown", 1).getInt();
                                String tag = power.getStringOrDefault("tag", "{}");
                                float divergence = power.getNumberOrDefault("divergence", 1.0f).getFloat();
                                float speed = power.getNumberOrDefault("speed", 1).getFloat();
                                int amt = power.getNumberOrDefault("count", 1).getInt();
                                int start_delay = power.getNumberOrDefault("start_delay", 0).getInt();
                                int interval = power.getNumberOrDefault("interval", 1).getInt();

                                // Introduce a slight random divergence
                                divergence += (float) ((Math.random() - 0.5) * 0.05); // Adjust the 0.05 value to control the randomness level

                                EntityType type;
                                if (power.getString("entity_type").equalsIgnoreCase("origins:enderian_pearl")) {
                                    type = EntityType.ENDER_PEARL;
                                    enderian_pearl.add(p);
                                } else {
                                    type = EntityType.valueOf(power.getNamespacedKey("entity_type").asString().split(":")[1].toUpperCase());
                                    enderian_pearl.remove(p);
                                }
                                String key = power.getJsonObject("key").getStringOrDefault("key", "key.origins.primary_active");

                                float finalDivergence = divergence;
                                boolean cont = !power.getJsonObject("key").getBooleanOrDefault("continuous", false);
                                new BukkitRunnable() {
                                    final float finalDivergence1 = finalDivergence;
                                    int shotsLeft = -amt;

                                    @Override
                                    public void run() {
                                        if (shotsLeft >= 0) {
                                            if ((!cont || !KeybindingUtils.activeKeys.get(p).contains(key)) && !in_continuous.get(p).contains(key)) {
                                                Cooldown.addCooldown(p, cooldown, power);
                                                setActive(p, power.getTag(), false);
                                                this.cancel();
                                                return;
                                            }
                                        }
                                        addCooldownPatch(p);

                                        if (type.getEntityClass() != null) {
                                            if (doubleFirePatch.contains(p)) return;
                                            Entity entityToSpawn = ((CraftEntity) p.getWorld().spawnEntity(p.getEyeLocation(), type)).getHandle();
                                            if (entityToSpawn.getBukkitEntity() instanceof Projectile proj) {
                                                proj.getScoreboardTags().add("fired_from_fp_power_by_" + p.getUniqueId());
                                                proj.setShooter(p);
                                                proj.setHasBeenShot(true);
                                                ((CraftEntity) proj).getHandle().saveWithoutId(new CompoundTag());
                                                Vector direction = p.getEyeLocation().getDirection();
                                                Vector dir = direction.clone().normalize().multiply(1);
                                                Vec3 startPos = MCUtil.toVec3(p.getEyeLocation());
                                                Vec3 endPos = startPos.add(dir.getX(), dir.getY(), dir.getZ());
                                                entityToSpawn.getBukkitEntity().getLocation().setDirection(direction);
                                                Location spawnLoc = CraftLocation.toBukkit(endPos);

                                                entityToSpawn.getBukkitEntity().teleport(spawnLoc);

                                                double yawRadians = Math.toRadians(p.getEyeLocation().getYaw() + finalDivergence1);

                                                double x = -Math.sin(yawRadians) * Math.cos(Math.toRadians(p.getEyeLocation().getPitch()));
                                                double y = -Math.sin(Math.toRadians(p.getEyeLocation().getPitch()));
                                                double z = Math.cos(yawRadians) * Math.cos(Math.toRadians(p.getEyeLocation().getPitch()));

                                                direction.setX(x);
                                                direction.setY(y);
                                                direction.setZ(z);
                                                Vector finalVeloc = direction.normalize().multiply(speed);

                                                entityToSpawn.getBukkitEntity().setVelocity(finalVeloc);
                                                CompoundTag mergedTag = entityToSpawn.saveWithoutId(new CompoundTag());
                                                String[] finalNbtTag = {""};
                                                if (power.getJsonObject("tag") != null) {
                                                    finalNbtTag[0] = mergedTag.merge(MiscUtils.ParserUtils.parseJson(new StringReader(tag), CompoundTag.CODEC)).getAsString();
                                                } else {
                                                    finalNbtTag[0] = mergedTag.getAsString();
                                                }

                                                Pattern pattern = Pattern.compile("b,Motion:\\[(.*?)\\],OnGround");
                                                Matcher matcher = pattern.matcher(finalNbtTag[0]);

                                                String previousSetMotion = "";
                                                if (matcher.find()) {
                                                    previousSetMotion = matcher.group(1);
                                                }
                                                finalNbtTag[0].replace(previousSetMotion, finalVeloc.getX() + "," + finalVeloc.getY() + "," + finalVeloc.getZ());
                                                entityToSpawn.remove(RemovalReason.DISCARDED);
                                                final boolean returnToNormal = p.getWorld().getGameRuleValue(GameRule.SEND_COMMAND_FEEDBACK);
                                                p.getWorld().setGameRule(GameRule.SEND_COMMAND_FEEDBACK, false);
                                                new BukkitRunnable() {
                                                    @Override
                                                    public void run() {
                                                        String cmd = "execute at {player} run summon {type} {loc} {nbt}"
                                                            .replace("{player}", p.getName())
                                                            .replace("{type}", type.key().asString())
                                                            .replace("{loc}", "^ ^1 ^")
                                                            .replace("{nbt}", finalNbtTag[0]);
                                                        CommandSourceStack source = new CommandSourceStack(
                                                            CommandSource.NULL,
                                                            ((CraftPlayer) p).getHandle().position(),
                                                            ((CraftPlayer) p).getHandle().getRotationVector(),
                                                            ((CraftPlayer) p).getHandle().level() instanceof ServerLevel ? (ServerLevel) ((CraftPlayer) p).getHandle().level() : null,
                                                            4,
                                                            ((CraftPlayer) p).getHandle().getName().getString(),
                                                            ((CraftPlayer) p).getHandle().getDisplayName(),
                                                            ((CraftPlayer) p).getHandle().getServer(),
                                                            ((CraftPlayer) p).getHandle());
                                                        ((CraftPlayer) p).getHandle().getServer().getCommands().performPrefixedCommand(source, cmd);
                                                        setActive(p, power.getTag(), true);
                                                        new BukkitRunnable() {
                                                            @Override
                                                            public void run() {
                                                                p.getWorld().setGameRule(GameRule.SEND_COMMAND_FEEDBACK, returnToNormal);
                                                            }
                                                        }.runTaskLater(GenesisMC.getPlugin(), 1);
                                                        doubleFirePatch.add(p);
                                                        new BukkitRunnable() {
                                                            @Override
                                                            public void run() {
                                                                doubleFirePatch.remove(p);
                                                            }
                                                        }.runTaskLater(GenesisMC.getPlugin(), 5);

                                                        org.bukkit.entity.Entity gottenEntity = null;
                                                        for (org.bukkit.entity.Entity nearbyEntity : p.getNearbyEntities(1, 2, 1)) {
                                                            if (nearbyEntity.getScoreboardTags().contains("fired_from_fp_power_by_" + p.getUniqueId())) {
                                                                gottenEntity = nearbyEntity;
                                                                break;
                                                            }
                                                        }

                                                        if (gottenEntity == null) return;

                                                        org.bukkit.entity.Entity finalEntity = p.getWorld().spawnEntity(spawnLoc, type);
                                                        if (finalEntity instanceof Projectile proj) {
                                                            proj.setShooter(p);
                                                        }
                                                        finalEntity.setCustomName(gottenEntity.getCustomName());
                                                        finalEntity.setCustomNameVisible(gottenEntity.isCustomNameVisible());
                                                        finalEntity.setFallDistance(gottenEntity.getFallDistance());
                                                        finalEntity.setFireTicks(gottenEntity.getFireTicks());
                                                        finalEntity.setFreezeTicks(gottenEntity.getFreezeTicks());
                                                        finalEntity.setGlowing(gottenEntity.isGlowing());
                                                        finalEntity.setGravity(gottenEntity.hasGravity());
                                                        finalEntity.setInvulnerable(gottenEntity.isInvulnerable());
                                                        finalEntity.setPassenger(gottenEntity.getPassenger());
                                                        finalEntity.setOp(gottenEntity.isOp());
                                                        finalEntity.setVelocity(gottenEntity.getVelocity());
                                                        finalEntity.setSilent(gottenEntity.isSilent());
                                                        if (((CraftEntity) gottenEntity).getHandle().getFirstPassenger() != null)
                                                            ((CraftEntity) gottenEntity).getHandle().getFirstPassenger().remove(RemovalReason.DISCARDED);
                                                        ((CraftEntity) gottenEntity).getHandle().remove(RemovalReason.DISCARDED);
                                                        setActive(p, power.getTag(), true);

                                                        peopladf.add(p);
                                                    }
                                                }.runTaskLater(GenesisMC.getPlugin(), 1);
                                            }

                                            shotsLeft++; // Decrement the remaining shots
                                        }
                                    }
                                }.runTaskTimer(GenesisMC.getPlugin(), start_delay, interval);
                            }
                        }
                    }
                }
            }
        }
    }


    @Override
    public String getType() {
        return "apoli:fire_projectile";
    }

    @Override
    public ArrayList<Player> getPlayersWithPower() {
        return fire_projectile;
    }
}
