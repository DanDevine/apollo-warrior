package com.gamadu.apollowarrior.builders;

import org.newdawn.slick.geom.Polygon;

import com.apollo.Component;
import com.apollo.Entity;
import com.apollo.EntityBuilder;
import com.apollo.EventHandler;
import com.apollo.World;
import com.apollo.annotate.InjectFromOwner;
import com.apollo.components.Transform;
import com.apollo.managers.GroupManager;
import com.apollo.utils.Timer;
import com.gamadu.apollowarrior.Groups;
import com.gamadu.apollowarrior.components.Movement;
import com.gamadu.apollowarrior.spatials.EnemySpatial;

public class EnemyShipBuilder implements EntityBuilder {
	private Polygon ship;
	private int containerWidth;
	private int containerHeight;

	public EnemyShipBuilder(int containerWidth, int containerHeight) {
		ship = new Polygon();
		ship.addPoint(0,0);
		ship.addPoint(40,0);
		ship.addPoint(20,20);
		
		this.containerWidth = containerWidth;
		this.containerHeight = containerHeight;
	}

	@Override
	public Entity buildEntity(final World world) {
		final Entity e = new Entity(world);
		e.setComponent(new Transform());
		e.setComponent(new EnemySpatial(ship));
		e.setComponent(new Movement(Math.random()>0.5?0.1f:-0.1f, 0f));
		
		// A little AI controller to manage movement.
		e.setComponent(createMovementAI());
		
		// A little AI controller to shoot.
		e.setComponent(createShootAI(world));
		
		e.addEventHandler("EXPIRED", new EventHandler() {
			@Override
			public void handleEvent() {
				System.out.println("Bullet has expired");
			}
		});
		
		e.addEventHandler("EXPLODED", new EventHandler() {
			@Override
			public void handleEvent() {
				Entity explosion = world.createEntity("ShipExplosion");
				explosion.getComponent(Transform.class).setLocation(e.getComponent(Transform.class));
				world.addEntity(explosion);
			}
		});
		
		world.getManager(GroupManager.class).setGroup(e, Groups.ComputerShips);
		
		return e;
	}

	private Component createShootAI(final World world) {
		return new Component() {
			@InjectFromOwner
			Transform transform;
			
			private Timer expiresTimer;
			
			@Override
			public void initialize() {
				expiresTimer = new Timer(1500, true) {
					@Override
					public void execute() {
						Entity bullet = world.createEntity("Bullet");
						bullet.getComponent(Transform.class).setLocation(transform);
						bullet.getComponent(Movement.class).setVectors(0, 0.2f);
						world.getManager(GroupManager.class).setGroup(bullet, Groups.ComputerBullets);
						world.addEntity(bullet);
					}
				};
			}
			
			@Override
			public void update(int delta) {
				expiresTimer.update(delta);
			}
		};
	}

	private Component createMovementAI() {
		return new Component() {
			@InjectFromOwner
			Movement movement;
			@InjectFromOwner
			Transform transform;
			
			@Override
			public void update(int delta) {
				if(transform.getX() < 0) {
					transform.setX(0);
					movement.setVx(-movement.getVx());
				}
				else if(transform.getX() > containerWidth) {
					transform.setX(containerWidth);
					movement.setVx(-movement.getVx());
				}
			}
		};
	}

	
}
