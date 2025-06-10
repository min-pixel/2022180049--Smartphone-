package kr.ac.tukorea.ge.and.minpixel.theyarecoming.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class DashCooldownBar implements IGameObject {
    private final Player player;
    private final float x, y, width, height;
    private final Paint bgPaint = new Paint();
    private final Paint fgPaint = new Paint();

    public DashCooldownBar(Player player, float x, float y, float width, float height) {
        this.player = player;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        bgPaint.setColor(0xFF444444); // 회색 배경
        fgPaint.setColor(0xFF00C0FF); // 밝은 파란색 전경
    }

    @Override
    public void update() {
        // 별도 로직 없음, draw에서 player 상태만 반영
    }

    @Override
    public void draw(Canvas canvas) {
        float ratio = player.getDashCooldownRatio(); // 0~1 사이 값
        RectF bg = new RectF(x, y, x + width, y + height);
        RectF fg = new RectF(x, y, x + width * ratio, y + height);
        canvas.drawRect(bg, bgPaint);
        canvas.drawRect(fg, fgPaint);
    }
}
