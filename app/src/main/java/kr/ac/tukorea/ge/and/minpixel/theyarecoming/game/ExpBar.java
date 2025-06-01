package kr.ac.tukorea.ge.and.minpixel.theyarecoming.game;

import android.graphics.Canvas;

import kr.ac.tukorea.ge.and.minpixel.theyarecoming.R;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.util.Gauge;

public class ExpBar implements IGameObject {
    private final Player player;
    private final Gauge gauge;

    public ExpBar(Player player) {
        this.player = player;
        this.gauge = new Gauge(0.1f, R.color.exp_gauge_fg, R.color.exp_gauge_bg);
    }

    @Override
    public void update() {
        // 없음
    }

    @Override
    public void draw(Canvas canvas) {
        float x = 50f;            // 좌측 여백
        float y = 30f;            // 화면 위쪽
        float width = 800f;       // 가로 길이
        float ratio = player.getExpRatio();
        gauge.draw(canvas, x, y, width, ratio);
    }
}
