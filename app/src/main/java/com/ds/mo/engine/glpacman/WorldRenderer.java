//package com.ds.mo.engine.glpacman;
//
//import com.ds.mo.engine.android.GLGraphics;
//import com.ds.mo.engine.android.SpriteBatcher;
//import com.ds.mo.engine.common.Camera2D;
//import com.ds.mo.engine.common.Color;
//import com.ds.mo.engine.common.Particle;
//import com.ds.mo.engine.glclonewars.logic.Bullet;
//import com.ds.mo.engine.glclonewars.logic.Enemy;
//import com.ds.mo.engine.glclonewars.logic.Player;
//import com.ds.mo.engine.glclonewars.logic.TypeA;
//import com.ds.mo.engine.glclonewars.logic.TypeB;
//import com.ds.mo.engine.glclonewars.logic.TypeC;
//import com.ds.mo.engine.glclonewars.logic.World;
//
//import javax.microedition.khronos.opengles.GL10;
//
///**
// * Created by Mo on 07/03/2018.
// */
//
//public class WorldRenderer {
//    public static final float FRUSTUM_WIDTH = 640f;
//    public static final float FRUSTUM_HEIGHT = 360f;
//
//    private GLGraphics glGraphics;
//    private World world;
//    private Camera2D cam;
//    private SpriteBatcher batcher;
//
//    public WorldRenderer(GLGraphics glGraphics, SpriteBatcher batcher, World world) {
//        this.glGraphics = glGraphics;
//        this.world = world;
//        this.cam = new Camera2D(glGraphics, FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
//        this.batcher = batcher;
//    }
//
//    private void drawBullets() {
//        int len = world.player.bullets.size();
//        for (int i = 0; i < len; i++) {
//            Bullet bullet = world.player.bullets.get(i);
//
////            batcher.setColor(Color.BLUE);
//            batcher.setColor(Color.WHITE);
//            batcher.drawSprite(bullet.bounds.center.x, bullet.bounds.center.y,
//                    32, 32, Assets.bullet);
//            //draw bounds
////            batcher.drawSprite(bullet.bounds.center.x, bullet.bounds.center.y,
////                    Bullet.BULLET_WIDTH, Bullet.BULLET_HEIGHT, Assets.boundsCircle);
////            batcher.drawSprite(bullet.bounds.center.x, bullet.bounds.center.y,
////                    Bullet.BULLET_WIDTH, Bullet.BULLET_HEIGHT, Assets.boundsCircle);
//        }
//    }
//
//    private void drawPlayer() {
//        Player player = world.player;
//        batcher.setColor(Color.WHITE);
//        batcher.drawSprite(player.position.x, player.position.y,
//                32, 32, player.rotation, Assets.player);
//        //draw bounds (batcher draws from center point)
////        batcher.drawSprite(
////                player.bounds.lowerLeft.x + Player.PLAYER_WIDTH / 2,
////                player.bounds.lowerLeft.y + Player.PLAYER_HEIGHT / 2,
////                Player.PLAYER_WIDTH, Player.PLAYER_HEIGHT, Assets.boundsRect);
//    }
//
//    private void drawEnemies() {
//        int len = world.enemies.size() - 1;
//        for (int i = len; i >= 0; i--) {
//            Enemy e = world.enemies.get(i);
//            switch (e.getType()) {
//                case Enemy.TYPEA:
//                    //draw sprite
//                    batcher.setColor(Color.RED);
//                    batcher.drawSprite(e.position.x, e.position.y,
//                            TypeA.TYPE_A_WIDTH, TypeA.TYPE_A_HEIGHT, Assets.enemy);
////                    //draw bounds
////                    batcher.setColor(Color.WHITE);
////                    batcher.drawSprite(e.bounds.center.x, e.bounds.center.y,
////                            TypeA.TYPE_A_WIDTH, TypeA.TYPE_A_HEIGHT,
////                            Assets.boundsCircle);
//                    break;
//                case Enemy.TYPEB:
//                    //draw sprite
//                    batcher.setColor(Color.BLUE);
//                    batcher.drawSprite(e.position.x, e.position.y,
//                            TypeB.TYPE_B_WIDTH, TypeB.TYPE_B_HEIGHT, e.getRotation(),
//                            Assets.enemy);
////                    //draw bounds
////                    batcher.setColor(Color.WHITE);
////                    batcher.drawSprite(e.bounds.center.x, e.bounds.center.y,
////                            TypeB.TYPE_B_WIDTH, TypeB.TYPE_B_HEIGHT, Assets.boundsCircle);
//                    break;
//                case Enemy.TYPEC:
//                    //draw sprite
//                    batcher.setColor(0.97f, 0.88f, 0.41f, 1);
//                    batcher.drawSprite(e.position.x, e.position.y,
//                            TypeC.TYPE_C_WIDTH, TypeC.TYPE_C_HEIGHT, e.getRotation(),
//                            Assets.enemy);
////                    //draw bounds
////                    batcher.setColor(Color.WHITE);
////                    batcher.drawSprite(e.bounds.center.x, e.bounds.center.y,
////                            TypeC.TYPE_C_WIDTH, TypeC.TYPE_C_HEIGHT, Assets.boundsCircle);
//                    break;
//            }
//        }
//    }
//
//    private void drawParticles() {
//        int len = world.death.particles.size() - 1;
//        for (int i = len; i >= 0; i--) {
//            Particle p = world.death.particles.get(i);
//            batcher.setColor(p.color.r, p.color.g, p.color.b, p.color.a);
//            batcher.drawSprite(p.position.x, p.position.y,
//                    p.width * p.scale, p.height * p.scale,
//                    p.rotation, Assets.fillRect);
//        }
//    }
//
//    private void drawHearts() {
//        int pad = 30;
//        int gap = 5;
//        int len = world.player.lives;
//
//        batcher.setColor(Color.WHITE);
//        for (int i = len; i > 0; i--) {
//            batcher.drawSprite(FRUSTUM_WIDTH - ((i-1) * (32 + gap)) - pad,
//                    FRUSTUM_HEIGHT - pad, 32, 32, Assets.heart);
//        }
//    }
//
//    private void drawObjects(float deltaTime) {
//        GL10 gl = glGraphics.getGL();
//        gl.glClearColor(0f, 0, 0, 1);
////        gl.glEnable(GL10.GL_BLEND);
////        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
//
////        batcher.beginBatch(Assets.worldAtlas);
////        batcher.setColor(1, 1, 1, 1);
//        drawBullets();
//        drawPlayer();
//        drawEnemies();
//        drawParticles();
//        drawHearts();
////        batcher.endBatch();
////        gl.glDisable(GL10.GL_BLEND);
//    }
//
//    /**
//     * Master method
//     */
//    public void draw(float deltaTime) {
//        cam.setViewportAndMatrices();
////        drawBackground();
//        drawObjects(deltaTime);
//    }
//}
