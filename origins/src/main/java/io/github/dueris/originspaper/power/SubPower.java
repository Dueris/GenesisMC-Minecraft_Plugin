package io.github.dueris.originspaper.power;

import net.minecraft.resources.ResourceLocation;

public class SubPower extends Power {

	private final ResourceLocation superPowerId;
	private final String subName;

	SubPower(ResourceLocation superPowerId, String subName, Power basePower) {
		super(basePower);
		this.superPowerId = superPowerId;
		this.subName = subName;
	}

	public ResourceLocation getSuperPowerId() {
		return superPowerId;
	}

	public String getSubName() {
		return subName;
	}

}
