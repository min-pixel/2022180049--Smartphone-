package kr.ac.tukorea.ge.and.minpixel.theyarecoming.app;

import android.os.Bundle;

import kr.ac.tukorea.ge.and.minpixel.theyarecoming.game.MainScene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.activity.GameActivity;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;

public class TheyarecomingActivity extends GameActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GameView.drawsDebugStuffs = true; // 디버그용 그리기 켜기
        super.onCreate(savedInstanceState);

        new MainScene().push();
    }
}