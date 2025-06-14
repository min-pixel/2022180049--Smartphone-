package kr.ac.tukorea.ge.and.minpixel.theyarecoming.game;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;

import kr.ac.tukorea.ge.and.minpixel.theyarecoming.R;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.FollowScrollBackground;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.JoyStick;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;

public class MainScene extends Scene {

    private Player player;
    private JoyStick joyStick;
    private GameTimer gameTimer;

    private StatWindow statWindow;
    private boolean statVisible = false;

    private StatToggleButton statButton;

    int mapWidth = 7200;  // ← 원하는 맵 크기
    int mapHeight = 12800;

    private FollowScrollBackground background;

    private long lastTouchTime = 0;
    private float lastTouchX, lastTouchY;
    public enum Layer {
        background, player, bullet, enemy, ui, controller, effect, COUNT
    }



    public MainScene() {
        //Metrics.setGameSize(900, 1600); // 해상도 설정
        initLayers(Layer.COUNT.ordinal());

        //add(Layer.background, new FixedBackground(GameView.view.getContext().getResources()));

        Bitmap bgBitmap = BitmapFactory.decodeResource(
                GameView.view.getContext().getResources(),
                R.mipmap.background
        );

        int mapWidth = bgBitmap.getWidth();
        int mapHeight = bgBitmap.getHeight();



        this.joyStick = new JoyStick(R.mipmap.joystick_bg, R.mipmap.joystick_thumb, 100, 1500, 100, 30, 80);

        player = new Player(joyStick);

        FollowScrollBackground bg = new FollowScrollBackground(
                player,
                mapWidth,
                mapHeight,
                bgBitmap
        );

        add(Layer.background,bg);
        gameTimer = new GameTimer();
        add(Layer.ui, gameTimer);
        add(Layer.ui, joyStick);
        add(Layer.player, player);
        add(Layer.ui, new ExpBar(player));
        add(Layer.controller, new CollisionChecker(this, player));
        add(Layer.controller, new EnemyGenerator(player, gameTimer,mapWidth, mapHeight));

        statButton = new StatToggleButton(() -> {
            if (statVisible) {
                remove(statWindow);
            } else {
                statWindow = new StatWindow(player);
                add(Layer.ui, statWindow);
            }
            statVisible = !statVisible;
        });

        add(Layer.ui, statButton);

        DashCooldownBar dashBar = new DashCooldownBar(player, 100, 1480, 150, 20);
        add(Layer.ui, dashBar);

    }




    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 먼저 버튼이 눌렸는지 확인
        if (statButton != null && statButton.onTouchEvent(event)) {
            return true; // 버튼이 눌렸으면 더 이상 처리 안 함
        }


        // 그 외에는 조이스틱 처리
        return joyStick.onTouch(event);
    }



}
