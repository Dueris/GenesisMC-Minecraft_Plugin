package me.dueris.originspaper.factory.data.types;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public record OriginUpgrade(ResourceLocation advancementCondition, ResourceLocation upgradeToOrigin,
							@Nullable String announcement) {
}
