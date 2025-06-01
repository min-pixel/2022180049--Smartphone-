package kr.ac.tukorea.ge.and.minpixel.theyarecoming.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;

public class GameTimer implements IGameObject {
    private float elapsedTime = 0f;
    private final Paint paint = new Paint();

    public GameTimer() {
        paint.setColor(Color.RED);
        paint.setTextSize(50f);
        paint.setTextAlign(Paint.Align.LEFT);
    }

    @Override
    public void update() {
        elapsedTime += GameView.frameTime;
    }

    @Override
    public void draw(Canvas canvas) {
        int seconds = (int) elapsedTime;
        String text = String.format("%02d:%02d", seconds / 60, seconds % 60);
        float x = 400f;
        float y = 130f; //경험치 바보다 아래로 약간 내려서
        canvas.drawText(text, x, y, paint);

    }

    public float getElapsedTime() {
        return elapsedTime;
    }
}
