package com.gamadu.apollowarrior.spatials;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;

import com.apollo.Layer;
import com.apollo.annotate.InjectComponent;
import com.apollo.components.Transform;

public class EnemySpatial extends SlickSpatial {
	@InjectComponent
	Transform transform;
	
	private Polygon ship;
	
	public EnemySpatial(Polygon ship) {
		this.ship = ship;
	}
	
	@Override
	public void render(Graphics g) {
		g.setColor(Color.red);
		ship.setCenterX(transform.getX());
		ship.setCenterY(transform.getY());
		//ship.setLocation(transform.getX(), transform.getY());
		g.fill(ship);
	}

	@Override
	public Layer getLayer() {
		return Layers.Ships;
	}

}
