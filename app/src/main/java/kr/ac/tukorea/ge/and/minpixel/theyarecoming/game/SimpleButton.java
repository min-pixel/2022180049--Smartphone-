package kr.ac.tukorea.ge.and.minpixel.theyarecoming.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import kr.ac.tukorea.ge.and.minpixel.theyarecoming.R;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.ILayerProvider;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class SimpleButton extends Sprite implements ILayerProvider<MainScene.Layer> {
    private final Runnable action;
    private final Paint textPaint = new Paint();
    private final String label;

    public SimpleButton(float x, float y, float width, float height, String label, Runnable action) {
        super(R.mipmap.button_background); // 또는 임시 투명 배경 리소스
        setPosition(x, y, width, height);
        this.label = label;
        this.action = action;

        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(50f);
    }


    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN) return false;


        float[] pt = Metrics.fromScreen(event.getX(), event.getY());

        if (!dstRect.contains(pt[0], pt[1])) return false;

        action.run();
        return true;
    }
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawText(label, x, y + 15f, textPaint);  // 중앙에 표시
    }

    @Override
    public MainScene.Layer getLayer() {
        return MainScene.Layer.ui;
    }
}

