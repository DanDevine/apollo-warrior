package com.gamadu.apollowarrior;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import com.apollo.Entity;
import com.apollo.World;
import com.apollo.components.Transform;
import com.apollo.managers.GroupManager;
import com.apollo.managers.RenderManager;
import com.apollo.managers.TagManager;
import com.gamadu.apollowarrior.builders.BulletBuilder;
import com.gamadu.apollowarrior.builders.EnemyShipBuilder;
import com.gamadu.apollowarrior.builders.ExplosionBuilder;
import com.gamadu.apollowarrior.components.Movement;
import com.gamadu.apollowarrior.managers.CollisionManager;
import com.gamadu.apollowarrior.managers.EnemyShipSpawnManager;
import com.gamadu.apollowarrior.spatials.BackgroundSpatial;
import com.gamadu.apollowarrior.spatials.PlayerSpatial;

public class ApolloWarrior extends BasicGame {
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;

	private World world;
	private RenderManager<Graphics> renderManager;
	private TagManager tagManager;
	private GroupManager groupManager;

	public ApolloWarrior() {
		super("Apollo Warrior");
	}
	
	@Override
	public void init(GameContainer container) throws SlickException {
		world = new World();
		
		renderManager = new RenderManager<Graphics>(container.getGraphics());
		tagManager = new TagManager();
		groupManager = new GroupManager();
		
		world.setManager(renderManager);
		world.setManager(tagManager);
		world.setManager(groupManager);
		world.setManager(new EnemyShipSpawnManager(container));
		world.setManager(new CollisionManager(container));
		
		world.setEntityBuilder("Bullet", new BulletBuilder());
		world.setEntityBuilder("EnemyShip", new EnemyShipBuilder(container.getWidth(), container.getHeight()));
		world.setEntityBuilder("BulletExplosion", new ExplosionBuilder(5));
		world.setEntityBuilder("ShipExplosion", new ExplosionBuilder(20));
		
		
		Entity player = new Entity(world);
		player.setComponent(new Transform(container.getWidth()/2, container.getHeight()-30));
		player.setComponent(new PlayerSpatial());
		player.setComponent(new Movement());
		world.addEntity(player);
		tagManager.register(Tags.Player, player);
		
		Entity bg = new Entity(world);
		bg.setComponent(new BackgroundSpatial());
		world.addEntity(bg);
		
		
		for(int i = 0; 10 > i; i++) {
			Entity enemyShip = world.createEntity("EnemyShip");
			enemyShip.getComponent(Transform.class).setLocation((float)Math.random()*container.getWidth(), 20);
			world.addEntity(enemyShip);
		}
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		world.update(delta);
		
		Input input = container.getInput();
		//if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
		if(input.isKeyPressed(Input.KEY_SPACE)) {
			Entity player = tagManager.getEntity(Tags.Player);
			
			Entity bullet = world.createEntity("Bullet");
			bullet.getComponent(Transform.class).setLocation(player.getComponent(Transform.class));
			bullet.getComponent(Movement.class).setVectors(0, -0.2f);
			groupManager.setGroup(bullet, Groups.PlayerBullets);
			world.addEntity(bullet);
		}
		
		if(input.isKeyDown(Input.KEY_LEFT)) {
			Entity player = tagManager.getEntity(Tags.Player);
			player.getComponent(Movement.class).setVx(-0.3f);
		}
		else if(input.isKeyDown(Input.KEY_RIGHT)) {
			Entity player = tagManager.getEntity(Tags.Player);
			player.getComponent(Movement.class).setVx(0.3f);
		}
		else {
			Entity player = tagManager.getEntity(Tags.Player);
			player.getComponent(Movement.class).setVectors(0, 0);
		}

	}

	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		renderManager.render(g);
	}

	public static void main(String[] args) throws SlickException {
		AppGameContainer container = new AppGameContainer(new ApolloWarrior());
		container.setDisplayMode(ApolloWarrior.WIDTH, ApolloWarrior.HEIGHT, false);
		container.setTargetFrameRate(60);
		container.setMinimumLogicUpdateInterval(1);
		container.setMaximumLogicUpdateInterval(1);
		container.setVSync(false);
		container.start();
	}
}
