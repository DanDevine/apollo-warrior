package com.gamadu.apollowarrior.spatials;

import com.apollo.Layer;

public enum Layers implements Layer {
	Effects,
	Projectiles, 
	Ships;

	@Override
	public int getLayerId() {
		return ordinal();
	}

}