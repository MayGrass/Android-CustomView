package tw.org.iii.iiiandroid07;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.LinkedList;

public class MyView extends View {
    private LinkedList<LinkedList<Point>> lines, recycler; //存放point的line //存放很多線

    //有AttributeSet才可載入XML的設定
    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(Color.BLUE);

        lines = new LinkedList<>();
        recycler = new LinkedList<>();

        //點下去放開才觸發一次
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("DCH", "onClick");
            }
        });
    }

    //觸摸事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float ex = event.getX(), ey = event.getY();
        Point point = new Point(ex, ey);

        //按下去開始畫新線
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Log.v("DCH", "Down");
            recycler.clear();
            LinkedList<Point> line = new LinkedList<>();
            lines.add(line);
        }

        lines.getLast().add(point);
        invalidate(); //觸發onDraw方法
        //測試動作

        //Log.v("DCH", "x:" + ex + "\ty:" + ey);
        // return true不會偵測onClick，點下去就觸發一次，滑動也會持續觸發多次，放開又觸發一次
        // return false 只會偵測按下去的動作觸發一次，且持續滑動也不會觸發，放開不會觸發
        return true; //super.onTouchEvent(event); 可偵測onClick的動作，並return true
    }

    //onDraw是任何時候都顯示在畫面
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setStrokeWidth(10);

        //手動畫線
        for (LinkedList<Point> line : lines) {
            for(int i=1; i<line.size(); i++) {
                Point p0 = line.get(i-1); Point p1 = line.get(i);
                canvas.drawLine(p0.x, p0.y, p1.x, p1.y, paint);
            }
        }

        //自動畫線
        canvas.drawLine(0,0,200,200, paint);
    }

    public void clear() {
        lines.clear();
        invalidate();
    }

    public void undo() {
        if (lines.size() > 0) {
            recycler.add(lines.removeLast());
            invalidate();
        }
    }

    public void redo() {
        if (recycler.size() > 0) {
            lines.add(recycler.removeLast());
            invalidate();
        }
    }

    private class Point {
        float x, y;
        Point(float x, float y) {
            this.x = x; this.y = y;
        }
    }

}
