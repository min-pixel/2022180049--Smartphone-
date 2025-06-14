package kr.ac.tukorea.ge.and.minpixel.theyarecoming.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.AnimSprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.FollowScrollBackground;

public class BossEnemy extends Enemy {
    private final Player target;

    private enum BossState { Idle, Telegraphing, Dashing, Cooldown }
    private BossState state = BossState.Idle;

    private float telegraphTimer = 0f;
    private float dashTimer = 0f;
    private float dashCooldown = 0f;
    private float dx, dy;
    private final float dashSpeed = 1200f;
    private float dashDistanceLeft = 0f;

    private float cooldown = 0.0f;
    private final float skillCooldown = 5.0f;

    private final float mapWidth;
    private final float mapHeight;

    private boolean isDead = false;

    public BossEnemy(Player player,float mapWidth, float mapHeight) {
        super(player, EnemyType.BOSS);
        this.target = player;

        this.maxLife = 300;
        this.life = maxLife;

        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;

        float frameWidth = bitmap.getWidth() / 7f;
        float frameHeight = bitmap.getHeight();
        setPosition(this.x, this.y, frameWidth * 2f, frameHeight * 2f);

        Log.d("BOSS", "보스 생성됨");
    }

    @Override
    public void update() {
        super.update();

        float dt = GameView.frameTime;

        // 상태 처리
        switch (state) {
            case Telegraphing:
                telegraphTimer -= dt;
                if (telegraphTimer <= 0) {
                    state = BossState.Dashing;
                    dashTimer = 2.0f;
                }
                break;

            case Dashing:
                float moveAmount = dashSpeed * dt;
                // 이동
                x += dx * moveAmount;
                y += dy * moveAmount;

                // 거리 감소
                dashDistanceLeft -= moveAmount;

                // 튕기기 처리 + 튕길 때 거리 패널티 추가
                if (x < radius || x > mapWidth - radius) {
                    dx = -dx;
                    dashDistanceLeft -= 100f; // 튕길 때 거리 추가로 깎기
                }
                if (y < radius || y > mapHeight - radius) {
                    dy = -dy;
                    dashDistanceLeft -= 100f;
                }

                if (dashDistanceLeft <= 0f) {
                    state = BossState.Cooldown;
                    dashCooldown = 3.0f;
                }
                break;

            case Cooldown:
                dashCooldown -= dt;
                if (dashCooldown <= 0) {
                    state = BossState.Idle;
                }
                break;

            case Idle:
            default:
                // Idle 상태일 때만 다음 스킬 가능
                cooldown += dt;
                if (cooldown >= skillCooldown) {
                    useSpecialSkill();
                    cooldown = 0f;
                }
                break;
        }
    }

    private void useSpecialSkill() {
        // 보스의 중심 좌표
        float bossX = this.x;
        float bossY = this.y;


        float playerX = target.getWorldX();
        float playerY = target.getWorldY();

        // 실제 월드 좌표 기준 벡터
        float dxRaw = playerX - bossX;
        float dyRaw = playerY - bossY;

        float len = (float)Math.sqrt(dxRaw * dxRaw + dyRaw * dyRaw);
        if (len > 0) {
            dx = dxRaw / len;
            dy = dyRaw / len;
        } else {
            dx = 0;
            dy = 1;
        }

        dashDistanceLeft = len * 3.0f;


        telegraphTimer = 1.0f;
        state = BossState.Telegraphing;

        Log.d("BOSS", "돌진 방향 dx=" + dx + " dy=" + dy);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (state == BossState.Telegraphing) {
            float scrollX = FollowScrollBackground.getScrollX();
            float scrollY = FollowScrollBackground.getScrollY();

            float sx = this.x - scrollX;
            float sy = this.y - scrollY;

            float ex = sx + dx * 2400f;
            float ey = sy + dy * 2400f;

            Paint paint = new Paint();
            paint.setColor(0x88FF0000);
            paint.setStrokeWidth(200f);
            canvas.drawLine(sx, sy, ex, ey, paint);
        }
    }

    public void increaseLife(int amount) {
        life = Math.min(maxLife, life + amount);
    }

    private void onDeath() {
        GameView.view.pushScene(new GameWinScene());
    }
    @Override
    public boolean decreaseLife(int power) {
        life -= power;
        if (life <= 0 && !isDead) {
            isDead = true;
            onDeath();
            return true;
        }
        return false;
    }
}
