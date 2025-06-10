package kr.ac.tukorea.ge.and.minpixel.theyarecoming.game;

//package kr.ac.tukorea.ge.and.minpixel.theyarecoming.scene;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.and.minpixel.theyarecoming.R;



public class PauseScene extends Scene {

    private final Paint textPaint = new Paint();

    private static final int BUTTON_COUNT = 3;
    private ArrayList<UpgradeButton> buttons = new ArrayList<>();
    private Player.StatType[] options = new Player.StatType[BUTTON_COUNT];
    private final Random random = new Random();
    private Player player;

    public PauseScene(Player player) {
        this.player = player;

        initLayers(MainScene.Layer.COUNT.ordinal());


        Sprite bg = new Sprite(R.mipmap.trans_50b);
        bg.setPosition(Metrics.width / 2, Metrics.height / 2, Metrics.width, Metrics.height);
        add(MainScene.Layer.background, bg);

        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);

        generateRandomOptions();
        createButtons();
    }


    public void update(float elapsedSeconds) {
        // 일시정지 상태이므로 논리 업데이트 없음
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas); // 이거 없으면 아무것도 안 그림. 꼭 넣자!

        // 배경에 어두운 반투명 레이어 (Optional)
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAlpha(128);
        canvas.drawRect(0, 0, Metrics.width, Metrics.height, paint);

        // 버튼 텍스트나 추가 요소를 직접 그리고 싶다면 여기서
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    private void generateRandomOptions() {
        Player.StatType[] allStats = Player.StatType.values();
        ArrayList<Player.StatType> pool = new ArrayList<>();
        for (Player.StatType stat : allStats) {
            pool.add(stat);
        }

        for (int i = 0; i < BUTTON_COUNT; i++) {
            int index = random.nextInt(pool.size());
            options[i] = pool.remove(index);
        }
    }

    private void createButtons() {
        float centerX = Metrics.width / 2;
        float baseY = Metrics.height * 0.4f;
        float gap = 200f; // 버튼 간격

        for (int i = 0; i < BUTTON_COUNT; i++) {
            final Player.StatType stat = options[i];
            String label = getLabelFor(stat);
            float y = baseY + i * gap;

            UpgradeButton button = new UpgradeButton(centerX, y, label, () -> {
                applyUpgrade(stat);
                GameView.view.popScene(); // PauseScene 종료
            });

            buttons.add(button);
            add(button);
        }
    }

    private String getLabelFor(Player.StatType stat) {
        switch (stat) {
            case SPEED: return "이동 속도 증가";
            case POWER: return "공격력 증가";
            case RATE: return "공격 속도 증가";
            case HP: return "최대 체력 증가";
            case DEFENSE: return "방어력 증가";
            case AOE: return "광역 스킬 강화";
            case PIERCING: return "관통 스킬 강화";
            case DASH: return "대쉬 스킬 강화";
        }
        return "";
    }

    private void applyUpgrade(Player.StatType stat) {
        if (player == null) {
            Log.e("PauseScene", "player is null in applyUpgrade!");
            return;
        }

        Log.d("PauseScene", "업그레이드 실행: " + stat);


        switch (stat) {
            case SPEED: player.upgradeSpeed(); break;
            case POWER: player.upgradePower(); break;
            case RATE: player.upgradeRate(); break;
            case HP: player.upgradeHP(); break;
            case DEFENSE: player.upgradeDefense(); break;
            case AOE: player.upgradeAOE(); break;
            case PIERCING: player.upgradePIERCING(); break;
            case DASH: player.upgradeDASH(); break;
        }
    }

    @Override
    public boolean onTouchEvent(android.view.MotionEvent event) {
        for (UpgradeButton button : buttons) {
            if (button.onTouchEvent(event)) return true;
        }
        return false;
    }

}

