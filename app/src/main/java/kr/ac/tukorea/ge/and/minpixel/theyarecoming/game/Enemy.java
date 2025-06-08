package kr.ac.tukorea.ge.and.minpixel.theyarecoming.game;

import android.graphics.Canvas;
import android.graphics.RectF;

import kr.ac.tukorea.ge.and.minpixel.theyarecoming.R;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IBoxCollidable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.ILayerProvider;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.FollowScrollBackground;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.util.Gauge;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.AnimSprite;
import kr.ac.tukorea.ge.and.minpixel.theyarecoming.game.EnemyType;



public class Enemy extends AnimSprite implements IBoxCollidable, ILayerProvider<MainScene.Layer> {
    private static final float ENEMY_WIDTH = 150f;
    private int life = 30;
    private int maxLife = 30;

    private final Player target;
    private static final Gauge hpGauge = new Gauge(0.1f, R.color.enemy_gauge_fg, R.color.enemy_gauge_bg);
    protected RectF collisionRect = new RectF();
    private final EnemyType type;



    public Enemy(Player target, EnemyType type) {
        super(getImageResId(type), 8);
        this.target = target;
        this.type = type;
        switch (type) {
            case GREEN:
                maxLife = 30;
                break;
            case RED:
                maxLife = 50;
                break;
            case BLACK:
                maxLife = 70;
                break;
        }
        this.life = maxLife;

    }

    private static int getImageResId(EnemyType type) {
        switch (type) {
            case RED: return R.mipmap.enemy_02;
            case BLACK: return R.mipmap.enemy_03;
            case GREEN:
            default: return R.mipmap.enemy_01;
        }
    }


    @Override
    public void update() {
        // 추적 로직
        float dx = target.getX() + FollowScrollBackground.getScrollX() - x;
        float dy = target.getY() + FollowScrollBackground.getScrollY() - y;
        float length = (float)Math.sqrt(dx * dx + dy * dy);
        if (length > 0) {
            dx /= length;
            dy /= length;
        }
        float speed;
        switch (type) {
            case GREEN: speed = 150f; break;  // 가장 빠름
            case RED:   speed = 100f; break;  // 중간
            case BLACK: speed = 70f; break;   // 가장 느림
            default:    speed = 100f; break;
        }

        this.dx = dx * speed;
        this.dy = dy * speed;

        super.update();
        updateCollisionRect();
    }

    @Override
    public void draw(Canvas canvas) {
        float scrollX = FollowScrollBackground.getScrollX();
        float scrollY = FollowScrollBackground.getScrollY();

        //  월드 좌표 (x, y)를 스크린 기준으로 보정
        float drawX = x - scrollX;
        float drawY = y - scrollY;

        //  스크린 좌표 기준으로 dstRect 수동 설정
        dstRect.set(
                drawX - width / 2, drawY - height / 2,
                drawX + width / 2, drawY + height / 2
        );

        super.draw(canvas);

        float gaugeWidth = width * 0.7f;
        float gaugeX = drawX - gaugeWidth / 2;
        float gaugeY = dstRect.bottom;
        hpGauge.draw(canvas, gaugeX, gaugeY, gaugeWidth, (float) life / maxLife);
    }



    private void updateCollisionRect() {
        collisionRect.set(dstRect);
        collisionRect.inset(15f, 15f);
    }

    public boolean isDead() {
        return life <= 0;
    }

    public boolean decreaseLife(int power) {
        life -= power;
        if (life < 0) life = 0;
        return life <= 0; // 죽었으면 true
    }

    public RectF getCollisionRect() {
        return collisionRect;
    }
    @Override
    public MainScene.Layer getLayer() {
        return MainScene.Layer.enemy;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public void move(float dx, float dy) {
        this.x += dx;
        this.y += dy;
    }

}
