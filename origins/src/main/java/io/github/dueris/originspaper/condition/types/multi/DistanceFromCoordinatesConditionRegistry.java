package io.github.dueris.originspaper.condition.types.multi;

import io.github.dueris.calio.SerializableDataTypes;
import io.github.dueris.calio.parser.SerializableData;
import io.github.dueris.originspaper.OriginsPaper;
import io.github.dueris.originspaper.condition.ConditionFactory;
import io.github.dueris.originspaper.data.ApoliDataTypes;
import io.github.dueris.originspaper.data.types.Comparison;
import io.github.dueris.originspaper.data.types.Shape;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.phys.Vec3;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * @author Alluysl
 * Handles the registry of the distance_from_spawn condition in both block and entity conditions to avoid duplicating code.
 * Ported from Apoli
 */
public class DistanceFromCoordinatesConditionRegistry {

	private static final ArrayList<Object> previousWarnings = new ArrayList<>();

	private static void warnOnce(String warning, Object key) {
		if (!previousWarnings.contains(key)) {
			previousWarnings.add(key);
			OriginsPaper.getPlugin().getLog4JLogger().warn(warning);
		}
	}

	private static void warnOnce(String warning) {
		warnOnce(warning, warning);
	}

	/**
	 * Warns the user of an issue getting an information needed for expected behavior, but only once (doesn't spam the console).
	 *
	 * @param object     the object that couldn't be acquired
	 * @param from       the object that was supposed to provide the required object
	 * @param assumption the result assumed because of the lack of information
	 * @return the assumed result
	 */
	private static <T> T warnCouldNotGetObject(String object, String from, T assumption) {
		warnOnce("Could not retrieve " + object + " from " + from + " for distance_from_spawn condition, assuming " + assumption + " for condition.");
		return assumption;
	}

	/**
	 * Returns an array of aliases for the condition.
	 */
	private static String[] getAliases() {
		return new String[]{"distance_from_spawn", "distance_from_coordinates"};
	}

	private static SerializableData getSerializableData(String alias) {
		// Using doubles and not ints because the player position is a vector of doubles and the sqrt function (for the distance) returns a double so we might as well use that precision
		return SerializableData.serializableData()
			.add("reference", SerializableDataTypes.STRING, alias.equals("distance_from_coordinates") ? "world_origin" : "world_spawn") // the reference point
//          .add("check_modified_spawn", SerializableDataTypes.BOOLEAN, true) // whether to check for modified spawns
			.add("offset", SerializableDataTypes.VECTOR, new Vec3(0, 0, 0)) // offset to the reference point
			.add("coordinates", SerializableDataTypes.VECTOR, new Vec3(0, 0, 0)) // adds up (instead of replacing, for simplicity) to the prior for aliasing
			.add("ignore_x", SerializableDataTypes.BOOLEAN, false) // ignore the axis in the distance calculation
			.add("ignore_y", SerializableDataTypes.BOOLEAN, false) // idem
			.add("ignore_z", SerializableDataTypes.BOOLEAN, false) // idem
			.add("shape", SerializableDataTypes.enumValue(Shape.class), Shape.CUBE) // the shape / distance type
			.add("scale_reference_to_dimension", SerializableDataTypes.BOOLEAN, true) // whether to scale the reference's coordinates according to the dimension it's in and the player is in
			.add("scale_distance_to_dimension", SerializableDataTypes.BOOLEAN, false) // whether to scale the calculated distance to the current dimension
			.add("comparison", ApoliDataTypes.COMPARISON)
			.add("compare_to", SerializableDataTypes.DOUBLE)
			.add("result_on_wrong_dimension", SerializableDataTypes.BOOLEAN, null) // if set and the dimension is not the same as the reference's, the value to set the condition to
			.add("round_to_digit", SerializableDataTypes.INT, null); // if set, rounds the distance to this amount of digits (e.g. 0 for unitary values, 1 for decimals, -1 for multiples of ten)
	}

	/**
	 * Infers the logically meaningful result of a distance comparison for out of bounds points (different dimension with corresponding parameter set, or infinite coordinates).
	 *
	 * @param comparison the comparison set in the data
	 * @return the result of that comparison against out-of-bounds points
	 */
	private static boolean compareOutOfBounds(Comparison comparison) {
		return comparison == Comparison.NOT_EQUAL || comparison == Comparison.GREATER_THAN || comparison == Comparison.GREATER_THAN_OR_EQUAL;
	}

	/**
	 * Tests the distance_from_spawn condition for either a block or an entity.
	 * No more and no less than one of either the block or entity argument must be null.
	 *
	 * @param data   the condition's parsed data
	 * @param block  the block to check the condition for
	 * @param entity the entity to check the condition for
	 * @return the result of the distance comparison
	 */
	private static boolean testCondition(SerializableData.Instance data, BlockInWorld block, Entity entity) {
		boolean scaleReferenceToDimension = data.getBoolean("scale_reference_to_dimension"),
			setResultOnWrongDimension = data.isPresent("result_on_wrong_dimension"),
			resultOnWrongDimension = setResultOnWrongDimension && data.getBoolean("result_on_wrong_dimension");
		double x = 0, y = 0, z = 0;
		Vec3 pos;
		Level world;
		// Get the world and its scale from the block/entity
		if (block != null) {
			BlockPos blockPos = block.getPos();
			pos = new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ());
			LevelReader worldView = block.getLevel();
			if (!(worldView instanceof Level))
				return warnCouldNotGetObject("world", "block", compareOutOfBounds(data.get("comparison")));
			else
				world = (Level) worldView;
		} else {
			pos = entity.position();
			world = entity.getCommandSenderWorld();
		}
		double currentDimensionCoordinateScale = world.dimensionType().coordinateScale();

		// Get the reference's scaled coordinates
		switch (data.getString("reference")) {
			case "player_spawn":
//                 if (entity instanceof ServerPlayerEntity) { // null instance of AnyClass is always false so the block case is covered
//
//                 }
//                 // No break on purpose (defaulting to natural spawn)
			case "player_natural_spawn": // spawn not set through commands or beds/anchors
				if (entity instanceof Player) { // && data.getBoolean("check_modified_spawn")){
					warnOnce("Used reference '" + data.getString("reference") + "' which is not implemented yet, defaulting to world spawn.");
				}
				// No break on purpose (defaulting to world spawn)
				if (entity == null)
					warnOnce("Used entity-condition-only reference point in block condition, defaulting to world spawn.");
			case "world_spawn":
				if (setResultOnWrongDimension && world.dimension() != Level.OVERWORLD)
					return resultOnWrongDimension;
				BlockPos spawnPos;
				if (world instanceof ServerLevel)
					spawnPos = world.getSharedSpawnPos();
				else
					return warnCouldNotGetObject("world with spawn position", block != null ? "block" : "entity", compareOutOfBounds(data.get("comparison")));
				x = spawnPos.getX();
				y = spawnPos.getY();
				z = spawnPos.getZ();
				break;
			case "world_origin":
				break;
		}
		Vec3 coords = data.get("coordinates");
		Vec3 offset = data.get("offset");
		x += coords.x + offset.x;
		y += coords.y + offset.y;
		z += coords.z + offset.z;
		if (scaleReferenceToDimension && (x != 0 || z != 0)) {
			if (currentDimensionCoordinateScale == 0) // pocket dimensions?
				// coordinate scale 0 means it takes 0 blocks to travel in the OW to travel 1 block in the dimension,
				// so the dimension is folded on 0 0, so unless the OW reference is at 0 0, it gets scaled to infinity
				return compareOutOfBounds(data.get("comparison"));
			x /= currentDimensionCoordinateScale;
			z /= currentDimensionCoordinateScale;
		}

		// Get the distance to these coordinates
		double distance,
			xDistance = data.getBoolean("ignore_x") ? 0 : Math.abs(pos.x() - x),
			yDistance = data.getBoolean("ignore_y") ? 0 : Math.abs(pos.y() - y),
			zDistance = data.getBoolean("ignore_z") ? 0 : Math.abs(pos.z() - z);
		if (data.getBoolean("scale_distance_to_dimension")) {
			xDistance *= currentDimensionCoordinateScale;
			zDistance *= currentDimensionCoordinateScale;
		}

		distance = Shape.getDistance(data.get("shape"), xDistance, yDistance, zDistance);

		if (data.isPresent("round_to_digit"))
			distance = new BigDecimal(distance).setScale(data.getInt("round_to_digit"), RoundingMode.HALF_UP).doubleValue();

		return ((Comparison) data.get("comparison")).compare(distance, data.getDouble("compare_to"));
	}

	// Watch Java generic type erasure destroy DRY

	public static void registerBlockCondition(Consumer<ConditionFactory<BlockInWorld>> registryFunction) {
		for (String alias : getAliases())
			registryFunction.accept(new ConditionFactory<>(OriginsPaper.apoliIdentifier(alias),
				getSerializableData(alias),
				(data, block) -> testCondition(data, block, null)));
	}

	public static void registerEntityCondition(Consumer<ConditionFactory<Entity>> registryFunction) {
		for (String alias : getAliases())
			registryFunction.accept(new ConditionFactory<>(OriginsPaper.apoliIdentifier(alias),
				getSerializableData(alias),
				(data, entity) -> testCondition(data, null, entity)));
	}
}
