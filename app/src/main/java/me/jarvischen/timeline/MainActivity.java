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
