package me.jarvischen.timeline.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import me.jarvischen.timeline.R;

/**
 * Created by chenfuduo on 2016/3/27.
 */
public class TimeLineView extends View {

    private int lineWidth;

    private int timeLineMarkerSize;

    private Drawable beginLine;

    private Drawable endLine;

    private Drawable timeLineMarker;

    public TimeLineView(Context context) {
        this(context, null);
    }

    public TimeLineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TimeLineView);
        lineWidth = typedArray.getDimensionPixelSize(R.styleable.TimeLineView_lineWidth, 15);
        timeLineMarkerSize = typedArray.getDimensionPixelSize(R.styleable.TimeLineView_timeLineMarkerSize, 25);
        beginLine = typedArray.getDrawable(R.styleable.TimeLineView_beginLine);
        endLine = typedArray.getDrawable(R.styleable.TimeLineView_endLine);
        timeLineMarker = typedArray.getDrawable(R.styleable.TimeLineView_timeLineMarker);
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (beginLine != null) {
            beginLine.draw(canvas);
        }
        if (endLine != null) {
            endLine.draw(canvas);
        }
        if (timeLineMarker != null) {
            timeLineMarker.draw(canvas);
        }
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = timeLineMarkerSize + getPaddingLeft() + getPaddingRight();
        int h = timeLineMarkerSize + getPaddingTop() + getPaddingBottom();
        int widthSize = resolveSizeAndState(w, widthMeasureSpec, 0);
        int heightSize = resolveSizeAndState(h, heightMeasureSpec, 0);
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initDrawable();
    }

    private void initDrawable() {
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        int paddingTop = getPaddingTop();

        //父容器的宽高
        int width = getWidth();
        int height = getHeight();

        int cWidth = width - paddingLeft - paddingRight;
        int cHeight = height - paddingTop - paddingBottom;

        int lastTimeLineMarkerSize = Math.min(timeLineMarkerSize, Math.min(cWidth, cHeight));

        Rect bounds = null;
        if (timeLineMarker != null) {
            timeLineMarker.setBounds(paddingLeft, paddingTop,
                    paddingLeft + lastTimeLineMarkerSize, paddingTop + lastTimeLineMarkerSize);
            bounds = timeLineMarker.getBounds();
        }
        //注意运算符的优先级
        int lineLeft = bounds.centerX() - (lineWidth >> 2);

        if (beginLine != null) {
            beginLine.setBounds(lineLeft, 0, lineLeft + lineWidth, bounds.top);
        }

        if (endLine != null) {
            endLine.setBounds(lineLeft, bounds.bottom, lineLeft + lineWidth, height);
        }
    }

    public void setBeginLine(Drawable beginLine) {
        this.beginLine = beginLine;
        initDrawable();
        invalidate();
    }

    public void setEndLine(Drawable endLine) {
        this.endLine = endLine;
        initDrawable();
        invalidate();
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
        initDrawable();
        invalidate();
    }

    public void setTimeLineMarker(Drawable timeLineMarker) {
        this.timeLineMarker = timeLineMarker;
        initDrawable();
        invalidate();
    }

    public void setTimeLineMarkerSize(int timeLineMarkerSize) {
        this.timeLineMarkerSize = timeLineMarkerSize;
        initDrawable();
        invalidate();
    }
}
