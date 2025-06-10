package kr.ac.tukorea.ge.and.minpixel.theyarecoming.game;

import android.graphics.Canvas;

import java.util.Random;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.FollowScrollBackground;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;

public class EnemyGenerator implements IGameObject {
    private static final float GEN_INTERVAL = 3.0f; // 3초 간격
    private final Random random = new Random();
    private float enemyTime = 0;

    private final Player player;

    private final GameTimer timer;

    public EnemyGenerator(Player player, GameTimer timer) {
        this.player = player;
        this.timer = timer;
    }

    @Override
    public void update() {
        enemyTime -= GameView.frameTime;
        if (enemyTime < 0) {
            generate();
            enemyTime = GEN_INTERVAL;
        }
    }

    private void generate() {
        float elapsed = timer.getElapsedTime();
        int spawnCount = 1 + (int)(elapsed / 15);
        spawnCount = Math.min(spawnCount, 10);

        for (int i = 0; i < spawnCount; i++) {
            float angle = random.nextFloat() * 360;
            float distance = 600 + random.nextFloat() * 200;


            float worldPlayerX = player.getX() + FollowScrollBackground.getScrollX();
            float worldPlayerY = player.getY() + FollowScrollBackground.getScrollY();

            float x = worldPlayerX + (float)Math.cos(Math.toRadians(angle)) * distance;
            float y = worldPlayerY + (float)Math.sin(Math.toRadians(angle)) * distance;
            //시간에 따라 타입 고정
            EnemyType randomType;
            if (elapsed < 10) {
                randomType = EnemyType.GREEN;
            } else if (elapsed < 30) {
                randomType = EnemyType.RED;
            } else {
                randomType = EnemyType.BLACK;
            }

            Enemy enemy = new Enemy(player, randomType);
            enemy.setPosition(x, y, 100);
            Scene.top().add(enemy);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        // 이 생성기는 그릴 게 없음
    }
}
