package kr.ac.tukorea.ge.and.minpixel.theyarecoming.game;

import android.graphics.RectF;

import kr.ac.tukorea.ge.and.minpixel.theyarecoming.R;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IBoxCollidable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.ILayerProvider;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IRecyclable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.FollowScrollBackground;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class Bullet extends Sprite implements IRecyclable, IBoxCollidable, ILayerProvider<MainScene.Layer> {
    private static final float BULLET_SIZE = 40f;
    private static final float SPEED = 1000f;
    private int power;

    public static Bullet get(float x, float y, int power) {
        return Scene.top().getRecyclable(Bullet.class).init(x, y, power);
    }

    public Bullet() {
        super(R.mipmap.bullet_01);
        dy = -SPEED; // 위로 날아감
    }

    private Bullet init(float x, float y, int power) {
        setPosition(x, y, BULLET_SIZE, BULLET_SIZE);
        this.power = power;

        // 가장 가까운 Enemy 찾기
        Enemy closest = findClosestEnemyOnScreen(x, y);
        if (closest != null) {
            // 월드 좌표에서 방향 계산 시, 스크롤 보정 추가
            float scrollX = FollowScrollBackground.getScrollX();
            float scrollY = FollowScrollBackground.getScrollY();

            // 플레이어의 월드 위치 = 화면 중앙 + 스크롤
            float worldPlayerX = x + scrollX;
            float worldPlayerY = y + scrollY;

            // 적 위치는 월드 좌표이므로 그대로 사용
            float dx = closest.getX() - worldPlayerX;
            float dy = closest.getY() - worldPlayerY;
            float len = (float)Math.sqrt(dx * dx + dy * dy);
            if (len > 0) {
                dx /= len;
                dy /= len;
            }
            this.dx = dx * SPEED;
            this.dy = dy * SPEED;
        } else {
            this.dx = 0;
            this.dy = 0;
        }

        return this;
    }

    public static Enemy findClosestEnemyOnScreen(float x, float y) {
        float scrollX = FollowScrollBackground.getScrollX();
        float scrollY = FollowScrollBackground.getScrollY();

        float margin = 200f; // ← 화면 밖 200픽셀까지 허용

        float screenLeft = scrollX - margin;
        float screenTop = scrollY - margin;
        float screenRight = scrollX + Metrics.width + margin;
        float screenBottom = scrollY + Metrics.height + margin;

        Enemy closest = null;
        float minDist = Float.MAX_VALUE;

        for (Object obj : Scene.top().objectsAt(MainScene.Layer.enemy)) {
            if (!(obj instanceof Enemy)) continue;
            Enemy enemy = (Enemy) obj;

            float ex = enemy.getX();
            float ey = enemy.getY();

            // "확장된" 스크린 안에 있는지 확인
            if (ex < screenLeft || ex > screenRight || ey < screenTop || ey > screenBottom) continue;

            float dx = ex - (x + scrollX);
            float dy = ey - (y + scrollY);
            float distSq = dx * dx + dy * dy;
            if (distSq < minDist) {
                minDist = distSq;
                closest = enemy;
            }
        }

        return closest;
    }

    @Override
    public void update() {
        super.update();
        if (dstRect.bottom < 0 || dstRect.top > Metrics.height) {
            Scene.top().remove(this);
        }
    }

    public int getPower() {
        return power;
    }

    @Override
    public RectF getCollisionRect() {
        return dstRect;
    }

    @Override
    public void onRecycle() {
        // 필요 시 재사용 초기화
    }


    @Override
    public MainScene.Layer getLayer() {
        return MainScene.Layer.bullet;
    }
}
