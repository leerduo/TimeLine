在QQ空间，淘宝，京东，以及一些旅游类的app上经常可以看到时间轴的效果，这种时间轴的效果有多种实现方式，本文用RecyclerView和自定义View来实现这个效果。


实现的效果图如下：
![效果图](http://7xljei.com1.z0.glb.clouddn.com/device-2016-03-29-205457.png)

# 分析

从上面的效果图可以看出，这个就是一个RecyclerView，并且他的LayoutManager为LinearLayoutManager，LinearLayoutManager的方向是垂直的。但是我们注意到他的item不是都是一致的，大概有这几种情况：

* 只有一个item

![效果图](http://7xljei.com1.z0.glb.clouddn.com/%E5%BE%AE%E4%BF%A1%E6%88%AA%E5%9B%BE_20160329210955.png)


* 第一个item

![效果图](http://7xljei.com1.z0.glb.clouddn.com/%E5%BE%AE%E4%BF%A1%E6%88%AA%E5%9B%BE_20160329211352.png)


* 普通item

![效果图](http://7xljei.com1.z0.glb.clouddn.com/%E5%BE%AE%E4%BF%A1%E6%88%AA%E5%9B%BE_20160329210221.png)

* 最后一个item

![效果图](http://7xljei.com1.z0.glb.clouddn.com/%E5%BE%AE%E4%BF%A1%E6%88%AA%E5%9B%BE_20160329210315.png)

所以可以我们可以将item的viewType分为四类。以普通的为例，我们需要自定义的是item左侧的部分，包括开始的线条，中间的圆形，下面的线条三个部分。其他的控件都是系统现成的。

![效果图](http://7xljei.com1.z0.glb.clouddn.com/%E5%BE%AE%E4%BF%A1%E6%88%AA%E5%9B%BE_20160329210422.png)

# 自定义属性

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <declare-styleable name="TimeLineView">
        <attr name="beginLine" format="color|reference" />
        <attr name="endLine" format="color|reference" />
        <attr name="lineWidth" format="dimension" />
        <attr name="timeLineMarker" format="color|reference" />
        <attr name="timeLineMarkerSize" format="dimension" />
    </declare-styleable>
</resources>
```
> * beginLine:开始的线条
* endLine:下面的线条
* lineWidth:线条的宽度
* timeLineMarker:中间的圆形
* timeLineMarkerSize:中间的圆形的大小，这里默认他的宽高一致

# item实体数据封装

由效果图，可以看出我们需要封装的是：
> * timeLineIcon:事件对应的icon，比如生日对应的icon
* timeLineName:事件的名称，比如生日
* timeLineDate:事件的日期
* timeLineTime:事件的日期距离现在的时间


```java
package me.jarvischen.timeline.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by chenfuduo on 2016/3/27.
 */
public class TimeLineItem {
    private Drawable timeLineIcon;
    private String timeLineName;
    private String timeLineDate;
    private String timeLineTime;

    public TimeLineItem() {
    }

    public TimeLineItem(String timeLineDate, Drawable timeLineIcon,
                        String timeLineName, String timeLineTime) {
        this.timeLineDate = timeLineDate;
        this.timeLineIcon = timeLineIcon;
        this.timeLineName = timeLineName;
        this.timeLineTime = timeLineTime;
    }

    public String getTimeLineDate() {
        return timeLineDate;
    }

    public void setTimeLineDate(String timeLineDate) {
        this.timeLineDate = timeLineDate;
    }

    public Drawable getTimeLineIcon() {
        return timeLineIcon;
    }

    public void setTimeLineIcon(Drawable timeLineIcon) {
        this.timeLineIcon = timeLineIcon;
    }

    public String getTimeLineName() {
        return timeLineName;
    }

    public void setTimeLineName(String timeLineName) {
        this.timeLineName = timeLineName;
    }

    public String getTimeLineTime() {
        return timeLineTime;
    }

    public void setTimeLineTime(String timeLineTime) {
        this.timeLineTime = timeLineTime;
    }
}
```

# 主布局和数据填充

主布局就是一个RecyclerView，

```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <android.support.v7.widget.RecyclerView
        android:id="@+id/timeLineRecyclerView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:descendantFocusability="afterDescendants"
        android:fadeScrollbars="true"
        android:fadingEdge="none"
        android:overScrollMode="never"
        android:scrollbarSize="2dp"
        android:scrollbarThumbVertical="@color/cyan_500"
        android:scrollbars="vertical" />

</FrameLayout>
```

```java
package me.jarvischen.timeline;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import me.jarvischen.timeline.adapter.TimeLineAdapter;
import me.jarvischen.timeline.bean.TimeLineItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView timeLineRecyclerView = (RecyclerView) findViewById(R.id.timeLineRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        timeLineRecyclerView.setLayoutManager(linearLayoutManager);
        TimeLineAdapter adapter = new TimeLineAdapter(initDatas());
        timeLineRecyclerView.setAdapter(adapter);
    }

    private List<TimeLineItem> initDatas() {
        List<TimeLineItem> timeLineItems = new ArrayList<>();
        timeLineItems.add(new TimeLineItem("2016年3月27日",
                getResources().getDrawable(R.drawable.ic_icon_birthday_min),
                "爸爸的生日",
                "1天"));

        timeLineItems.add(new TimeLineItem("2016年3月28日",
                getResources().getDrawable(R.drawable.ic_icon_birthday_min),
                "弟弟的生日",
                "2天"));


        timeLineItems.add(new TimeLineItem("2016年3月29日",
                getResources().getDrawable(R.drawable.ic_icon_birthday_min),
                "老婆的生日",
                "3天"));


        timeLineItems.add(new TimeLineItem("2016年3月30日",
                getResources().getDrawable(R.drawable.ic_icon_memorial_min),
                "结婚纪念日",
                "4天"));


        timeLineItems.add(new TimeLineItem("2016年3月31日",
                getResources().getDrawable(R.drawable.ic_icon_future_min),
                "去看美人鱼",
                "5天"));


        timeLineItems.add(new TimeLineItem("2016年4月1日",
                getResources().getDrawable(R.drawable.ic_icon_future_min),
                "植物园赏花",
                "6天"));


        timeLineItems.add(new TimeLineItem("2016年4月2日",
                getResources().getDrawable(R.drawable.ic_icon_memorial_min),
                "恋爱纪念日",
                "7天"));


        timeLineItems.add(new TimeLineItem("2016年4月3日",
                getResources().getDrawable(R.drawable.ic_icon_future_min),
                "自定义View练习",
                "8天"));

        timeLineItems.add(new TimeLineItem("2016年4月4日",
                getResources().getDrawable(R.drawable.ic_icon_memorial_min),
                "结婚十周年纪念日",
                "9天"));
        return timeLineItems;
    }
}
```

RecyclerView需要一个LayoutManager，然后设置设配器即可。

# 自定义TimeLineMarker

* 第一步，构造器找到定义的属性值
```java
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
```

* 第二步，重写onMeasure方法
```java
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = timeLineMarkerSize + getPaddingLeft() + getPaddingRight();
        int h = timeLineMarkerSize + getPaddingTop() + getPaddingBottom();
        int widthSize = resolveSizeAndState(w, widthMeasureSpec, 0);
        int heightSize = resolveSizeAndState(h, heightMeasureSpec, 0);
        setMeasuredDimension(widthSize, heightSize);
    }
```
其中`resolveSizeAndState`的源码为，
```java
public static int resolveSizeAndState(int size, int measureSpec, int childMeasuredState) {
        final int specMode = MeasureSpec.getMode(measureSpec);
        final int specSize = MeasureSpec.getSize(measureSpec);
        final int result;
        switch (specMode) {
            case MeasureSpec.AT_MOST:
                if (specSize < size) {
                    result = specSize | MEASURED_STATE_TOO_SMALL;
                } else {
                    result = size;
                }
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
            case MeasureSpec.UNSPECIFIED:
            default:
                result = size;
        }
        return result | (childMeasuredState & MEASURED_STATE_MASK);
    }
```
哈，好熟悉的感觉，有没有！
关于此，更多请参考[这里](https://opticalix.github.io/2015/10/25/custom-view-summary/)。

* 第三步，绘制
```java
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
```

* 第四步，重写onSizeChanged方法
```java
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
```
逻辑很简单，不做过多的解释。initDrawableSize 方法进行具体的运算，而运算的时间点就是当控件的大小改变(onSizeChanged)的时候。

在初始化中采用了一定的投机取巧；这里利用了上内边距与下内边距分别作为上线条与下线条的长度；而线条与中间的标识都采用了水平距中。

整体的代码，
```java
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
```

# Adapter和ViewHolder
```java
package me.jarvischen.timeline.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import me.jarvischen.timeline.R;
import me.jarvischen.timeline.bean.TimeLineItem;
import me.jarvischen.timeline.bean.TimeLineItemType;

/**
 * Created by chenfuduo on 2016/3/27.
 */
public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineViewHolder> {

    private List<TimeLineItem> timeLineItems;

    public TimeLineAdapter(List<TimeLineItem> timeLineItems) {
        this.timeLineItems = timeLineItems;
    }

    @Override
    public TimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_timeline, null);
        return new TimeLineViewHolder(view, viewType,parent.getContext());
    }

    @Override
    public void onBindViewHolder(TimeLineViewHolder holder, int position) {
        holder.setData(timeLineItems.get(position));
    }

    @Override
    public int getItemCount() {
        return timeLineItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        int size = timeLineItems.size() - 1;
        if (size == 0) {
            return TimeLineItemType.ATOM;
        } else if (position == 0) {
            return TimeLineItemType.START;
        } else if (position == size) {
            return TimeLineItemType.END;
        } else {
            return TimeLineItemType.NORMAL;
        }
    }
}
```
需要注意的是`getItemViewType`方法，在`TimeLineItemType`类中，封装了上述所述的四种情形。

```java
package me.jarvischen.timeline.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

import me.jarvischen.timeline.R;
import me.jarvischen.timeline.bean.TimeLineItem;
import me.jarvischen.timeline.bean.TimeLineItemType;
import me.jarvischen.timeline.view.TimeLineView;

/**
 * Created by chenfuduo on 2016/3/27.
 */
public class TimeLineViewHolder extends RecyclerView.ViewHolder {
    private TextView name;
    private ImageView icon;
    private TextView date;
    private TextView time;

    public TimeLineViewHolder(View itemView, int type,Context context) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.timeLineName);
        date = (TextView) itemView.findViewById(R.id.timeLineDate);
        time = (TextView) itemView.findViewById(R.id.timeLineTime);
        icon = (ImageView) itemView.findViewById(R.id.timeLineIcon);
        final TimeLineView timeLineView = (TimeLineView) itemView.findViewById(R.id.timeLineView);
        Drawable drawable = context.getResources().getDrawable(R.drawable.timeline_marker);
        Drawable drawable2 = context.getResources().getDrawable(R.drawable.timeline_marker2);
        Drawable drawable3 = context.getResources().getDrawable(R.drawable.timeline_marker3);
        Drawable drawable4 = context.getResources().getDrawable(R.drawable.timeline_marker4);
        Drawable drawable5 = context.getResources().getDrawable(R.drawable.timeline_marker5);
        Drawable drawable6 = context.getResources().getDrawable(R.drawable.timeline_marker6);
        Drawable drawable7 = context.getResources().getDrawable(R.drawable.timeline_marker7);
        Random random = new Random();
        final int i = random.nextInt(7);
        final Drawable drawables[] = {drawable,drawable2,drawable3,drawable4,drawable5,drawable6,drawable7};
        timeLineView.setTimeLineMarker(drawables[i]);
        if (type == TimeLineItemType.ATOM) {
            timeLineView.setBeginLine(null);
            timeLineView.setEndLine(null);
        } else if (type == TimeLineItemType.START) {
            timeLineView.setBeginLine(null);
        } else if (type == TimeLineItemType.END) {
            timeLineView.setEndLine(null);
        }
    }

    public void setData(TimeLineItem timeLineItem) {
        name.setText(timeLineItem.getTimeLineName());
        icon.setImageDrawable(timeLineItem.getTimeLineIcon());
        date.setText(timeLineItem.getTimeLineDate());
        time.setText(timeLineItem.getTimeLineTime());
    }

}
```
根据Item类型的不同去设置数据。



参考文档：
> * [Drawable](http://developer.android.com/reference/android/graphics/drawable/Drawable.html)
* [Canvas and Drawables](http://developer.android.com/guide/topics/graphics/2d-graphics.html)
* [Tutorial: Custom Drawable - Part 3](http://rey5137.com/blog/tutorial-custom-drawable-part3/)
* [Custom Drawables](http://ryanharter.com/blog/2015/04/03/custom-drawables/)
* [Implementing and Using Custom Drawable States](http://ptrprograms.blogspot.sg/2015/01/implementing-and-using-custom-drawable.html)




