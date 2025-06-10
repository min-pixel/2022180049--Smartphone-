package kr.ac.tukorea.ge.and.minpixel.theyarecoming.game;

import android.graphics.Canvas;
import android.graphics.Rect;

import kr.ac.tukorea.ge.and.minpixel.theyarecoming.R;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.ILayerProvider;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;

public class DashEffect extends Sprite implements ILayerProvider<MainScene.Layer> {
    private float elapsed = 0f;
    private final float duration = 0.2f;  // 총 수명
    private final int frameCount = 2;
    private final int frameWidth;
    private final Rect srcRect = new Rect();
    private float angleDegrees = 0f;

    public DashEffect(float x, float y, float dx, float dy) {
        super(R.mipmap.dash); // dash.png 등록 필요
        setPosition(x, y, 150, 150);

        this.frameWidth = bitmap.getWidth() / frameCount;
        updateSrcRect(0);  // 초기 프레임 설정

        this.angleDegrees = (float) Math.toDegrees(Math.atan2(-dy, -dx));
    }

    @Override
    public void update() {
        elapsed += GameView.frameTime;
        if (elapsed >= duration) {
            Scene.top().remove(this);
            return;
        }

        int currentFrame = (int)((elapsed / duration) * frameCount);
        updateSrcRect(currentFrame);
    }

    private void updateSrcRect(int frameIndex) {
        int left = frameIndex * frameWidth;
        int right = left + frameWidth;
        srcRect.set(left, 0, right, bitmap.getHeight());
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        canvas.rotate(angleDegrees, x, y);  // 중심 기준 회전
        canvas.drawBitmap(bitmap, srcRect, dstRect, null);
        canvas.restore();
    }

    @Override
    public MainScene.Layer getLayer() {
        return MainScene.Layer.effect; // 이펙트 계층에 자동 배치
    }
}
