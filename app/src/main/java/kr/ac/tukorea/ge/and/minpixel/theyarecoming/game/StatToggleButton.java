package kr.ac.tukorea.ge.and.minpixel.theyarecoming.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.ILayerProvider;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;
import kr.ac.tukorea.ge.and.minpixel.theyarecoming.R;
public class StatToggleButton implements ILayerProvider<MainScene.Layer>, IGameObject {
    private final Runnable onClick;
    private final RectF rect;
    private final Paint paint = new Paint();

    public StatToggleButton(Runnable onClick) {
        this.onClick = onClick;
        float size = 100f;
        float x = Metrics.width - size - 20f; // 오른쪽 위
        float y = 20f;
        rect = new RectF(x, y, x + size, y + size);

        paint.setColor(Color.LTGRAY);
    }

    @Override
    public void update() {}

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(rect, paint);
        Paint text = new Paint();
        text.setColor(Color.BLACK);
        text.setTextSize(40f);
        text.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("스탯", rect.centerX(), rect.centerY() + 15f, text);
    }


    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN) return false;
        float[] pts = Metrics.fromScreen(event.getX(), event.getY());
        if (!rect.contains(pts[0], pts[1])) return false;
        onClick.run();
        return true;
    }

    @Override
    public MainScene.Layer getLayer() {
        return MainScene.Layer.ui;
    }

}
