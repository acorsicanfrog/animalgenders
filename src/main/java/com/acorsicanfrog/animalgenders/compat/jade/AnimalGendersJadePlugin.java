package com.acorsicanfrog.animalgenders.compat.jade;

import net.minecraft.world.entity.Entity;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class AnimalGendersJadePlugin implements IWailaPlugin {

	@Override
	public void registerClient(IWailaClientRegistration registration) {
		registration.registerEntityComponent(GenderEntityProvider.INSTANCE, Entity.class);
	}
}
