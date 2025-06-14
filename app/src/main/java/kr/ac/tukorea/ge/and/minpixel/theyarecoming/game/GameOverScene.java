package kr.ac.tukorea.ge.and.minpixel.theyarecoming.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import java.util.ArrayList;

import kr.ac.tukorea.ge.and.minpixel.theyarecoming.R;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class GameOverScene extends Scene {
    private final Paint paint;
    private final ArrayList<SimpleButton> buttons = new ArrayList<>();

    public GameOverScene() {
        paint = new Paint();
        paint.setTextSize(100);
        paint.setColor(Color.RED);
        paint.setTextAlign(Paint.Align.CENTER);

        float centerX = Metrics.width / 2f;
        float centerY = Metrics.height / 2f;

        initLayers(MainScene.Layer.COUNT.ordinal());

        SimpleButton retryBtn = new SimpleButton(centerX, centerY + 100, 600, 120, "재도전", () -> {
            while (GameView.view.getSceneCount() > 1) {
                GameView.view.popScene();
            }
            GameView.view.pushScene(new MainScene());
        });

        SimpleButton exitBtn = new SimpleButton(centerX, centerY + 250, 600, 120, "나가기", () -> {
            System.exit(0);
        });

        buttons.add(retryBtn);
        buttons.add(exitBtn);
        add(retryBtn);
        add(exitBtn);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        canvas.drawText("Game Over", Metrics.width / 2f, Metrics.height / 2f - 100, paint);
        super.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        for (SimpleButton button : buttons) {
            if (button.onTouchEvent(event)) return true;
        }
        return false;
    }
}

