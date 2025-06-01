package kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.content.res.Resources;
import android.util.Log;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.spgp2025.a2dg.R;

public class FollowScrollBackground implements IGameObject {

    private final IScrollTarget target;
    private float scrollX = 0;
    private float scrollY = 0;


    public static float getScrollX() { return instance.scrollX; }
    public static float getScrollY() { return instance.scrollY; }
    private static FollowScrollBackground instance;

    private static final float SCROLL_SPEED = 300f;



    private static float scrollRequestX = 0;
    private static float scrollRequestY = 0;

    private final float mapWidth;
    private final float mapHeight;

    private Bitmap fullBitmap;

    private final Rect src = new Rect();
    private final Rect dst = new Rect();

    private final RectF dstF = new RectF();

    private final Paint paint = new Paint();

    public interface IScrollTarget {
        float getX();
        float getY();
    }

    public FollowScrollBackground(IScrollTarget target, float mapWidth, float mapHeight, Bitmap backgroundImage) {
        this.target = target;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        instance = this;
        this.fullBitmap = backgroundImage;


        // scroll 초기값 설정 (화면 중심으로 시작)
        scrollX = clamp(target.getX() - Metrics.width / 2f, 0, mapWidth - Metrics.width);
        scrollY = clamp(target.getY() - Metrics.height / 2f, 0, mapHeight - Metrics.height);
    }

    @Override
    public void update() {

        //Log.d("SCROLL_BG", String.format("Before clamp: scrollY=%.1f requestY=%.1f",
                //scrollY, scrollRequestY));

        scrollX += scrollRequestX;
        scrollY += scrollRequestY;


        scrollX = clamp(scrollX, 0, mapWidth - Metrics.width);


        scrollY = clamp(scrollY, 0, mapHeight - Metrics.height);

        scrollRequestX = 0;
        scrollRequestY = 0;
        Log.d("DEBUG", "map height: " + fullBitmap.getHeight() + ", screen: " + Metrics.height);

        //Log.d("SCROLL_BG", String.format("After clamp: scrollY=%.1f", scrollY));
    }

    @Override
    public void draw(Canvas canvas) {
          canvas.save();
//
//        //  스크롤 만큼 캔버스를 이동시켜서
//        // 맵 전체 중 현재 보이는 부분만 보이게 함
//        canvas.translate(-scrollX, -scrollY);
//
//        //  연두색으로 전체 맵 배경 그리기
//        Paint bgPaint = new Paint();
//        bgPaint.setColor(0xFFAAF4A3); // 연두색
//        canvas.drawRect(0, 0, mapWidth, mapHeight, bgPaint);
//
//        // 테두리 핑크색
//        Paint borderPaint = new Paint();
//        borderPaint.setColor(0xFFFF69B4);
//        borderPaint.setStyle(Paint.Style.FILL);
//
//        float sideThickness = 300f;
//        float topBottomThickness = 600f; // ← 위아래 더 두껍게
//
//        // 위쪽
//        canvas.drawRect(0, 0, mapWidth, topBottomThickness, borderPaint);
//        // 아래쪽
//        canvas.drawRect(0, mapHeight - topBottomThickness, mapWidth, mapHeight, borderPaint);
//        // 왼쪽
//        canvas.drawRect(0, 0, sideThickness, mapHeight, borderPaint);
//        // 오른쪽
//        canvas.drawRect(mapWidth - sideThickness, 0, mapWidth, mapHeight, borderPaint);
//
//        canvas.restore(); // 캔버스 원래대로 복원

        if (fullBitmap == null) return;

        float viewW = Metrics.width;
        float viewH = Metrics.height;

        // float 기반 scroll 위치
        float srcLeft = scrollX;
        float srcTop = scrollY;
        float srcRight = Math.min(srcLeft + viewW, fullBitmap.getWidth());
        float srcBottom = Math.min(srcTop + viewH, fullBitmap.getHeight());

        // src는 여전히 정수형(Rect)로
        src.set((int)srcLeft, (int)srcTop, (int)srcRight, (int)srcBottom);

        // dst는 float 정밀도 유지 (RectF)
        float dstW = srcRight - srcLeft;
        float dstH = srcBottom - srcTop;
        dstF.set(0f, 0f, dstW, dstH); // float 기반으로 정밀하게

        // 최종 draw
        canvas.drawBitmap(fullBitmap, src, dstF, paint);

        Log.d("CHECK", "Bitmap size: " + fullBitmap.getWidth() + " x " + fullBitmap.getHeight());
        Log.d("CHECK", "Screen size: " + Metrics.width + " x " + Metrics.height);


    }


    private float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(val, max));
    }

    public static void requestScrollX(float dx) {
        scrollRequestX += dx;
    }

    public static void requestScrollY(float dy) {
        scrollRequestY += dy;
    }
}
