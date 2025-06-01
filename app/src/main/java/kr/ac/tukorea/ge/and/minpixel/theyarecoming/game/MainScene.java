package kr.ac.tukorea.ge.and.minpixel.theyarecoming.game;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
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

    int mapWidth = 7200;  // ← 원하는 맵 크기
    int mapHeight = 12800;

    private FollowScrollBackground background;
    public enum Layer {
        background, player, bullet, enemy, ui, controller, COUNT
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
        add(Layer.controller, new EnemyGenerator(player, gameTimer));

    }




    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return joyStick.onTouch(event);
    }
}
