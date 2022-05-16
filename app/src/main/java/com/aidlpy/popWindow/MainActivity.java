package com.aidlpy.popWindow;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, PopupWindow.OnDismissListener{

    private Button bt_open;
    private PopupWindow popupWindow;
    private int navigationHeight;
    /**
     * 目的地名
     */
     private String locationName = "杭州市西湖文化广场";

    /**
     *  经度
     */
     private Double longitude = 120.162979;

    /**
     * 纬度
     */
     private Double latitude = 30.276701;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_open = (Button) findViewById(R.id.button);
        bt_open.setOnClickListener(this);

        int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        navigationHeight = getResources().getDimensionPixelSize(resourceId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                openPopupWindow(v);
                break;
            case R.id.tv_pick_phone:
                Toast.makeText(this, "高德地图导航", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
                openGaoDeMap();
                break;
            case R.id.tv_pick_zone:
                Toast.makeText(this, "百度地图导航", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
                openBaiDuMap();
                break;
            case R.id.tv_pick_tu:
                Toast.makeText(this, "腾讯地图导航", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
                openTencentMap();
                break;
            case R.id.tv_cancel:
                popupWindow.dismiss();
                break;
        }
    }

    //高德
    private void openGaoDeMap() {
        if (MapUtil.isGdMapInstalled()) {
            MapUtil.openGaoDeNavi(this, 0, 0, null, latitude, longitude, locationName);
        } else {
            Toast.makeText(this, "尚未安装高德地图", Toast.LENGTH_SHORT).show();
        }
    }

    //百度
    private void openBaiDuMap() {
        if (MapUtil.isBaiduMapInstalled()) {
            MapUtil.openBaiDuNavi(this, 0, 0, null, latitude, longitude, locationName);
        } else {
            //这里必须要写逻辑，不然如果手机没安装该应用，程序会闪退，这里可以实现下载安装该地图应用
            Toast.makeText(this, "尚未安装高德地图", Toast.LENGTH_SHORT).show();
        }
    }

    //腾讯
    private void openTencentMap() {
        if (MapUtil.isTencentMapInstalled()) {
            MapUtil.openTencentMap(this, 0, 0, null, latitude, longitude, locationName);
        } else {
            //这里必须要写逻辑，不然如果手机没安装该应用，程序会闪退，这里可以实现下载安装该地图应用
            Toast.makeText(this, "尚未安装高德地图", Toast.LENGTH_SHORT).show();
        }
    }

    private void openPopupWindow(View v) {
        //防止重复按按钮
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        //设置PopupWindow的View
        View view = LayoutInflater.from(this).inflate(R.layout.view_popupwindow, null);
        popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //设置背景,这个没什么效果，不添加会报错
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置点击弹窗外隐藏自身
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        //设置动画
        popupWindow.setAnimationStyle(R.style.PopupWindow);
        //设置位置
        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, navigationHeight);
        //设置消失监听
        popupWindow.setOnDismissListener(this);
        //设置PopupWindow的View点击事件
        setOnPopupViewClick(view);
        //设置背景色
        setBackgroundAlpha(0.5f);
    }

    private void setOnPopupViewClick(View view) {
        TextView tv_pick_phone, tv_pick_zone, tv_pick_tu,tv_cancel;
        tv_pick_phone = (TextView) view.findViewById(R.id.tv_pick_phone);
        tv_pick_zone = (TextView) view.findViewById(R.id.tv_pick_zone);
        tv_pick_tu = (TextView) view.findViewById(R.id.tv_pick_tu);
        tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_pick_phone.setOnClickListener(this);
        tv_pick_zone.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        tv_pick_tu.setOnClickListener(this);
    }

    //设置屏幕背景透明效果
    public void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha;
        getWindow().setAttributes(lp);
    }

    @Override
    public void onDismiss() {
        setBackgroundAlpha(1);
    }
}