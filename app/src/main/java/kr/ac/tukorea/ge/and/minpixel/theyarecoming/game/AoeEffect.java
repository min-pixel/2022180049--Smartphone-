package kr.ac.tukorea.ge.and.minpixel.theyarecoming.game;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.ArrayList;

import kr.ac.tukorea.ge.and.minpixel.theyarecoming.R;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IBoxCollidable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.ILayerProvider;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.util.CollisionHelper;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;

public class AoeEffect extends Sprite implements IBoxCollidable, ILayerProvider<MainScene.Layer> {
    private float elapsedTime = 0f;
    private float lifeTime = 0.4f; // 전체 수명
    private static final int TOTAL_FRAMES = 7;
    private int frameWidth;

    private int aoeLevel;
    private int baseDamage;

    private boolean hasDamaged = false;

    private final Rect srcRect = new Rect();

    public AoeEffect(float x, float y, int aoeLevel, int baseDamage) {
        super(R.mipmap.aoef);
        setPosition(x, y, 300, 300);

        this.frameWidth = bitmap.getWidth() / TOTAL_FRAMES;
        this.aoeLevel = aoeLevel;
        this.baseDamage = baseDamage;

        // 첫 프레임
        srcRect.set(0, 0, frameWidth, bitmap.getHeight());
    }

    @Override
    public void update() {
        elapsedTime += GameView.frameTime;
        int frame = (int)(elapsedTime * (TOTAL_FRAMES / lifeTime)); // lifeTime 동안 TOTAL_FRAMES 재생
        if (frame >= TOTAL_FRAMES) {
            Scene.top().remove(this);
            return;
        }

        srcRect.set(
                frame * frameWidth,
                0,
                (frame + 1) * frameWidth,
                bitmap.getHeight()
        );
        if (!hasDamaged) {
            hasDamaged = true;
            ArrayList<IGameObject> enemies = Scene.top().objectsAt(MainScene.Layer.enemy);
            for (IGameObject obj : enemies) {
                if (!(obj instanceof Enemy)) continue;
                Enemy enemy = (Enemy)obj;
                if (CollisionHelper.collides(this, enemy)) {
                    enemy.decreaseLife(getDamage());
                }
            }
        }


    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, srcRect, dstRect, null);
    }

    @Override
    public RectF getCollisionRect() {
        return dstRect;
    }

    public int getDamage() {
        return baseDamage - 5 + (aoeLevel * 5);
    }

    @Override
    public MainScene.Layer getLayer() {
        return MainScene.Layer.effect;
    }
}
