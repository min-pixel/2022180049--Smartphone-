package kr.ac.tukorea.ge.and.minpixel.theyarecoming.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.ILayerProvider;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;
import kr.ac.tukorea.ge.and.minpixel.theyarecoming.R;

public class UpgradeButton extends Sprite implements ILayerProvider<MainScene.Layer> {
    private final Runnable onClick;
    private final String label;
    private final Paint paint = new Paint();

    public UpgradeButton(float cx, float cy, String label, Runnable onClick) {
        super(R.mipmap.button);
        setPosition(cx, cy, Metrics.size(500), Metrics.size(120));
        this.label = label;
        this.onClick = onClick;

        paint.setColor(Color.BLACK);
        paint.setTextSize(Metrics.size(40));
        paint.setTextAlign(Paint.Align.CENTER);


    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        Paint bg = new Paint();
        bg.setColor(Color.GREEN);
        canvas.drawRect(dstRect, bg);

        paint.setColor(Color.BLACK);
        canvas.drawText(label, x, y + 20f, paint);

    }


    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN) return false;

        float[] pts = Metrics.fromScreen(event.getX(), event.getY());
        float tx = pts[0];
        float ty = pts[1];

        if (!dstRect.contains(tx, ty)) return false;
        onClick.run();
        return true;
    }

    @Override
    public MainScene.Layer getLayer() {
        return MainScene.Layer.ui;
    }

}
