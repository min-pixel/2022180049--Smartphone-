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

    public enum StatType {
        SPEED, POWER, RATE, HP, DEFENSE, AOE, PIERCING, DASH
    }
    private static final float PLAYER_WIDTH = 150f;
    private static final float PLAYER_HEIGHT = 150f;
    private static final int FRAME_WIDTH = 64;
    private static final int TOTAL_FRAMES = 8;
    private float elapsedTime = 0;

    // === 스탯 ===
    private int speedLevel = 0;
    private int powerLevel = 0;
    private int rateLevel = 0;
    private int hpLevel = 0;
    private int defenseLevel = 0;

    private int aoeLevel = 0;
    private int piercingLevel = 0;

    private static float SPEED = 300f;     // 기본값
    private int attackPower = 20;
    private float fireInterval = 0.5f; // 0.5초마다 발사
    private int defense = 0;
    private int life = 1000;
    private int maxLife = 1000;

    private boolean isDashing = false;
    private float dashDx, dashDy;
    private float dashTimer = 0f;
    private float dashCooldown = 0f;

    private int dashLevel = 0;

    private final JoyStick joystick;
    private float targetX;

    private final Rect srcRect = new Rect();

    private static final int GAUGE_COLOR_NORMAL = R.color.player_gauge_fg;
    private static final int GAUGE_COLOR_DASH = R.color.exp_gauge_fg;

    private static final Gauge hpGauge = new Gauge(0.1f, R.color.player_gauge_fg, R.color.player_gauge_bg);


    private float fireTime = 0f;

    private int exp = 0;
    private int maxExp = 10;

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

        if (!isDashing && dashCooldown <= 0 && joystick.power >= 1.f) {
            requestDash(-dx, -dy);  // ← 조이스틱 방향 그대로 대시 (스크롤 반영)
        }

        if (isDashing) {
            float scrollAmount = getDashDistance() * GameView.frameTime / 0.2f;
            FollowScrollBackground.requestScrollX(-dashDx * scrollAmount);
            FollowScrollBackground.requestScrollY(-dashDy * scrollAmount);
            dashTimer -= GameView.frameTime;
            if (dashTimer <= 0) {
                isDashing = false;
                hpGauge.setFgColor(R.color.player_gauge_fg);
            }
        } else {
            FollowScrollBackground.requestScrollX(dx * GameView.frameTime);
            FollowScrollBackground.requestScrollY(dy * GameView.frameTime);
        }

        dashCooldown -= GameView.frameTime;
        if (dashCooldown < 0) dashCooldown = 0;


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

        Bullet bullet = Bullet.get(x, y - radius - 10, attackPower, getAOELevel(), getPiercingLevel());
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
        if (exp > maxExp) {
            exp = 0;
            maxExp += 10;
        }
        if (exp >= maxExp) {
            GameView.view.pushScene(new PauseScene(this));
            return;
        }

    }
    public float getExpRatio() {
        return (float) exp / maxExp;
    }

    @Override
    public RectF getCollisionRect() {
        return dstRect; // 또는 inset 적용된 hitbox
    }

    public void decreaseLife(int amount) {
        if (isDashing) return;

        int reduced = Math.max(1, amount - defense);
        life -= reduced;
        if (life < 0) life = 0;
        if (life <= 0) {
            onDeath();
        }
    }

    private void onDeath() {
        // 앱 종료 처리
        GameView.view.pushScene(new GameOverScene());
    }

    public int getLife() {
        return life;
    }

    public void upgradeSpeed() {
        //if (speedLevel >= 3) return;
        speedLevel++;
        SPEED += 5f;
    }

    public void upgradePower() {
        //if (powerLevel >= 3) return;
        powerLevel++;
        attackPower += 5;
    }

    public void upgradeRate() {
        //if (rateLevel >= 3) return;
        rateLevel++;
        fireInterval *= 0.85f; // 더 빠르게
    }

    public void upgradeHP() {
        //if (hpLevel >= 3) return;
        hpLevel++;
        maxLife += 200;
        life = maxLife;
    }

    public void upgradeDefense() {
        //if (defenseLevel >= 3) return;
        defenseLevel++;
        defense += 2;
    }

    public void upgradeAOE() {
        //if (aoeLevel >= 3) return;
        aoeLevel++;
    }

    public void upgradePIERCING() {
        //if (piercingLevel >= 9) return;
        piercingLevel++;
    }

    public int getSpeedLevel() {
        return speedLevel;
    }

    public int getPowerLevel() {
        return powerLevel;
    }

    public int getRateLevel() {
        return rateLevel;
    }

    public int getHPLevel() {
        return hpLevel;
    }


    public boolean isDead() {
        return life <= 0;
    }

    public int getDefenseLevel() {
        return defenseLevel;
    }

    public int getAOELevel() { return aoeLevel; }
    public int getPiercingLevel() { return piercingLevel*3; }

    public void requestDash(float dx, float dy) {
        if (dashCooldown > 0 || isDashing) return;

        hpGauge.setFgColor(R.color.exp_gauge_fg);

        float len = (float) Math.sqrt(dx * dx + dy * dy);
        if (len < 10f) return;

        dashDx = dx / len;
        dashDy = dy / len;
        isDashing = true;
        dashTimer = 0.2f;
        dashCooldown = getDashCooldown();

        MainScene scene = (MainScene) Scene.top();
        scene.add(MainScene.Layer.effect, new DashEffect(Metrics.width / 2, Metrics.height / 2, dashDx, dashDy));

    }


    private float getDashDistance() {
        return 400f + dashLevel * 200f; // Lv0:400, Lv1:600, Lv2:800, Lv3:1000
    }

    private float getDashCooldown() {
        return 3.5f - dashLevel * 0.5f; // Lv0:3.5초, Lv1:3.0초, ...
    }

    public void upgradeDASH() {
        if (dashLevel >= 3) return;
        dashLevel++;
    }

    public int getDashLevel() {
        return dashLevel;
    }

    public float getDashCooldownRatio() {
        return dashCooldown / getDashCooldown(); // 0~1 사이 비율
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }

    public float getWorldX() {
        return x + FollowScrollBackground.getScrollX();
    }

    public float getWorldY() {
        return y + FollowScrollBackground.getScrollY();
    }
}
