package kr.ac.tukorea.ge.and.minpixel.theyarecoming.game;

import android.graphics.Canvas;
import android.graphics.RectF;

import java.util.ArrayList;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IBoxCollidable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.util.CollisionHelper;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;

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

                    if (bullet.alreadyHit(enemy)) continue;  // 이미 맞았으면 무시

                    bullet.markHit(enemy); //최초 히트 적 등록


                    if (bullet.decreasePiercingLevel()) {
                        scene.remove(MainScene.Layer.bullet, bullet); // piercingLevel이 0이면 제거
                    }
                    bullet.explodeAOE();
                    boolean dead = enemy.decreaseLife(bullet.getPower());
                    if (dead) {
                        scene.remove(MainScene.Layer.enemy, enemy);
                        player.addExp(5);
                        // scene.addScore(enemy.getScore()); // 점수 시스템 있다면 여기서 처리
                    }


                }
            }

            // 적과 플레이어 충돌
            if (CollisionHelper.collides(enemy, player)) {
                player.decreaseLife(10); // 체력 10 감소 (원하는 값으로 조정)

            }
        }



        // 적들끼리 겹치지 않게 밀어내기
        for (int i = 0; i < enemies.size(); ++i) {
            Enemy e1 = (Enemy) enemies.get(i);
            RectF r1 = e1.getCollisionRect();

            for (int j = i + 1; j < enemies.size(); ++j) {
                Enemy e2 = (Enemy) enemies.get(j);
                RectF r2 = e2.getCollisionRect();

                if (RectF.intersects(r1, r2)) {
                    // 보스와 일반 적 충돌 처리
                    if (e1.getType() == EnemyType.BOSS && e2.getType() != EnemyType.BOSS) {
                        scene.remove(MainScene.Layer.enemy, e2);
                        ((BossEnemy)e1).increaseLife(10);  // 회복량 조정 가능
                        continue;
                    }
                    if (e2.getType() == EnemyType.BOSS && e1.getType() != EnemyType.BOSS) {
                        scene.remove(MainScene.Layer.enemy, e1);
                        ((BossEnemy)e2).increaseLife(10);
                        continue;
                    }

                    float dx = e1.getX() - e2.getX();
                    float dy = e1.getY() - e2.getY();
                    float dist = (float)Math.sqrt(dx * dx + dy * dy);
                    if (dist == 0) {
                        dx = 1; dy = 0; dist = 1;
                    }

                    float push = 1.5f;
                    float px = dx / dist * push;
                    float py = dy / dist * push;

                    e1.move(px, py);
                    e2.move(-px, -py);
                }
            }
        }


    }

    @Override
    public void draw(Canvas canvas) {
        // 충돌 전용 로직이므로 draw는 비워둠
    }
}
