package com.gamadu.apollowarrior.spatials;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;

import com.apollo.Layer;
import com.apollo.annotate.InjectFromOwner;
import com.apollo.components.Transform;

public class PlayerSpatial extends SlickSpatial {
	@InjectFromOwner
	Transform transform;
	
	private Polygon ship;
	
	@Override
	public void initialize() {
		ship = new Polygon();
		ship.addPoint(0,0);
		ship.addPoint(40,0);
		ship.addPoint(20,-20);

	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.green);
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