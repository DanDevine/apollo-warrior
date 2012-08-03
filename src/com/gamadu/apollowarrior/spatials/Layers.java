package com.gamadu.apollowarrior.spatials;

import com.apollo.Layer;

public enum Layers implements Layer {
	Background,
	Effects,
	Projectiles, 
	Ships, 
	Interface;

	@Override
	public int getLayerId() {
		return ordinal();
	}

}
