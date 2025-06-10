package kr.ac.tukorea.ge.and.minpixel.theyarecoming.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.ILayerProvider;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class StatWindow extends Sprite implements ILayerProvider<MainScene.Layer> {
    private final Player player;
    private final Paint paint = new Paint();

    public StatWindow(Player player) {
        super(0); // 이미지 없음
        this.player = player;
        setPosition(Metrics.width / 2, Metrics.height / 2, 600, 700);
        paint.setColor(Color.WHITE);
        paint.setTextSize(40f);
    }

    @Override
    public void draw(Canvas canvas) {
        // 배경
        Paint bg = new Paint();
        bg.setColor(Color.argb(180, 0, 0, 0));
        canvas.drawRect(dstRect, bg);

        float textX = dstRect.left + 40;
        float textY = dstRect.top + 80;
        float gap = 80;

        canvas.drawText("이동 속도: " + player.getSpeedLevel(), textX, textY, paint);
        canvas.drawText("공격력: " + player.getPowerLevel(), textX, textY += gap, paint);
        canvas.drawText("공격 속도: " + player.getRateLevel(), textX, textY += gap, paint);
        canvas.drawText("체력: " + player.getHPLevel(), textX, textY += gap, paint);
        canvas.drawText("방어력: " + player.getDefenseLevel(), textX, textY += gap, paint);
        canvas.drawText("광역 스킬: " + player.getAOELevel(), textX, textY += gap, paint);
        canvas.drawText("관통 스킬: " + player.getPiercingLevel(), textX, textY += gap, paint);
        canvas.drawText("대쉬 스킬: " + player.getDashLevel(), textX, textY += gap, paint);
    }

    @Override
    public MainScene.Layer getLayer() {
        return MainScene.Layer.ui;
    }



}
