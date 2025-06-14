package kr.ac.tukorea.ge.and.minpixel.theyarecoming.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import java.util.ArrayList;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class TitleScene extends Scene {
    private final Paint titlePaint;
    private final ArrayList<SimpleButton> buttons = new ArrayList<>();

    public TitleScene() {
        titlePaint = new Paint();
        titlePaint.setTextSize(120f);
        titlePaint.setColor(Color.WHITE);
        titlePaint.setTextAlign(Paint.Align.CENTER);

        float cx = Metrics.width / 2f;
        float cy = Metrics.height / 2f;

        initLayers(MainScene.Layer.COUNT.ordinal());

        SimpleButton startBtn = new SimpleButton(cx, cy + 100, 600, 120, "게임 시작", () -> {
            GameView.view.changeScene(new MainScene());
        });

        buttons.add(startBtn);
        add(startBtn);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        canvas.drawText("몰려온다!!!", Metrics.width / 2f, Metrics.height / 2f - 200, titlePaint);
        super.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        for (SimpleButton btn : buttons) {
            if (btn.onTouchEvent(event)) return true;
        }
        return false;
    }
}
