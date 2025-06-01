package kr.ac.tukorea.ge.and.minpixel.theyarecoming.game;

import android.graphics.Canvas;
import android.graphics.RectF;

import java.util.ArrayList;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IBoxCollidable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.util.CollisionHelper;


public class CollisionChecker implements IGameObject {
    private final MainScene scene;
    private final Player player;
    public CollisionChecker(MainScene mainScene, Player player) {
        this.scene = mainScene;
        this.player = player;
    }

    @Override
    public void update() {
        ArrayList<IGameObject> enemies = scene.objectsAt(MainScene.Layer.enemy);
        for (int e = enemies.size() - 1; e >= 0; e--) {
            Enemy enemy = (Enemy) enemies.get(e);
            ArrayList<IGameObject> bullets = scene.objectsAt(MainScene.Layer.bullet);
            for (int b = bullets.size() - 1; b >= 0; b--) {
                Bullet bullet = (Bullet) bullets.get(b);
                if (CollisionHelper.collides(enemy, bullet)) {
                    scene.remove(MainScene.Layer.bullet, bullet);
                    boolean dead = enemy.decreaseLife(bullet.getPower());
                    if (dead) {
                        scene.remove(MainScene.Layer.enemy, enemy);
                        player.addExp(5);
                        // scene.addScore(enemy.getScore()); // 점수 시스템 있다면 여기서 처리
                    }
                    break; // 한 총알은 한 적에게만 충돌
                }
            }

            // 적과 플레이어 충돌
            if (CollisionHelper.collides(enemy, player)) {
                player.decreaseLife(10); // 체력 10 감소 (원하는 값으로 조정)
            }
            if(CollisionHelper.collides(enemy, enemy)){
                // 적과 적이 너무 겹치지 않게 적 자바에서 함수를 추가해 처리해볼까.
            }


        }



    }

    @Override
    public void draw(Canvas canvas) {
        // 충돌 전용 로직이므로 draw는 비워둠
    }
}
