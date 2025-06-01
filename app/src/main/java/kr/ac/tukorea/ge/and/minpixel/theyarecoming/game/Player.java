package kr.ac.tukorea.ge.and.minpixel.theyarecoming.game;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;

import kr.ac.tukorea.ge.and.minpixel.theyarecoming.R;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IBoxCollidable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.FollowScrollBackground;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.JoyStick;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.util.Gauge;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.util.RectUtil;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;
import android.util.Log;

public class Player extends Sprite implements IBoxCollidable, FollowScrollBackground.IScrollTarget{
    private static final float PLAYER_WIDTH = 150f;
    private static final float PLAYER_HEIGHT = 150f;
    private static final int FRAME_WIDTH = 64;
    private static final int TOTAL_FRAMES = 8;
    private float elapsedTime = 0;
    private static final float SPEED = 300f;
    private final JoyStick joystick;
    private float targetX;

    private final Rect srcRect = new Rect();

    private int life = 100000;
    private int maxLife = 100000;
    private static final Gauge hpGauge = new Gauge(0.1f, R.color.player_gauge_fg, R.color.player_gauge_bg);

    private float fireInterval = 0.5f; // 0.5초마다 발사
    private float fireTime = 0f;

    private int exp = 0;
    private int maxExp = 100;

    private int frameWidth;



    private static final float SCROLL_SPEED = 300f;

    private float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(val, max));
    }


    //public Player() {
        //super(R.mipmap.player); // 스프라이트 시트
        //setPosition(Metrics.width / 2, Metrics.height - 200, PLAYER_WIDTH, PLAYER_WIDTH);
        //targetX = x;
    //}

    public Player(JoyStick joystick) {
        super(R.mipmap.player);
        this.joystick = joystick;
        //setPosition(Metrics.width / 2, Metrics.height - 200, PLAYER_WIDTH, PLAYER_WIDTH);
        //프레임 너비를 이미지로부터 계산
        this.frameWidth = bitmap.getWidth() / TOTAL_FRAMES;

        //초기 프레임 설정
        srcRect.set(0, 0, frameWidth, bitmap.getHeight());
    }

    @Override
    public void update() {
        float dx = (float) Math.cos(joystick.angle_radian) * joystick.power * SPEED;
        float dy = (float) Math.sin(joystick.angle_radian) * joystick.power * SPEED;

        // 맵을 반대 방향으로 스크롤하여 플레이어가 중앙에 고정된 것처럼 보이게
        FollowScrollBackground.requestScrollX(dx * GameView.frameTime);
        FollowScrollBackground.requestScrollY(dy * GameView.frameTime);

        setPosition(Metrics.width / 2f, Metrics.height / 2f, PLAYER_WIDTH, PLAYER_HEIGHT);
        updateRoll();
        updateFire();
    }


    private void updateFire() {
        fireTime -= GameView.frameTime;
        if (fireTime <= 0) {
            fire(); // 총알 발사
            fireTime = fireInterval;
        }
    }

    private void fire() {
        Enemy closest = Bullet.findClosestEnemyOnScreen(x, y);
        if (closest == null) return; // 화면에 적 없으면 발사 안 함

        Bullet bullet = Bullet.get(x, y - radius - 10, 10);
        Scene.top().add(bullet);
    }

    private void updateRoll() {
        int frameHeight = bitmap.getHeight();

        if (joystick.power > 0.1f) {
            // 이동 중일 때 프레임 순환
            elapsedTime += GameView.frameTime;
            int frame = (int)(elapsedTime * 10) % TOTAL_FRAMES; // 초당 10프레임
            srcRect.set(
                    frame * frameWidth, 0,
                    (frame + 1) * frameWidth, bitmap.getHeight()
            );
        } else {
            // 정지 시 첫 프레임 고정
            elapsedTime = 0;
            srcRect.set(0, 0, frameWidth, bitmap.getHeight());
        }
    }


    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, srcRect, dstRect, null);

        // 체력바 위치 계산
        float gaugeWidth = width * 0.8f;
        float gaugeX = x - gaugeWidth / 2;
        float gaugeY = dstRect.top - 10.0f; // 플레이어 위에 표시되도록

        hpGauge.draw(canvas, gaugeX, gaugeY, gaugeWidth, (float) life / maxLife);
    }

    public boolean onTouch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                float[] pts = Metrics.fromScreen(event.getX(), event.getY());
                setTargetX(pts[0]);
                return true;
        }
        return false;
    }

    private void setTargetX(float x) {
        targetX = Math.max(radius, Math.min(x, Metrics.width - radius));
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public void addExp(int amount) {
        exp += amount;
        if (exp > maxExp) exp = maxExp; // 추후 레벨업 확장 가능
    }
    public float getExpRatio() {
        return (float) exp / maxExp;
    }

    @Override
    public RectF getCollisionRect() {
        return dstRect; // 또는 inset 적용된 hitbox
    }

    public void decreaseLife(int amount) {
        life -= amount;
        if (life < 0) life = 0;

        if (life <= 0) {
            onDeath(); // 사망 처리
        }
    }

    private void onDeath() {
        // 앱 종료 처리
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    public int getLife() {
        return life;
    }
}
