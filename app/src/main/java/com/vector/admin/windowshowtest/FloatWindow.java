package com.vector.admin.windowshowtest;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;


/**
 * ================================================
 * 作    者： 崔远程 cuiyuancheng@9086.cn
 * 版    本： 1.0
 * 创建日期： 2017/11/15 上午10:58
 * 描    述：
 * 修订历史：
 * 版    权： © 海纳科技
 * ================================================
 */
public class FloatWindow extends View {

    protected Context mContext;
    //用户手势监听
    protected GestureDetector gestureDetector;

    protected WindowManager mWindowManager;
    protected WindowManager.LayoutParams wmParams;
    protected RelativeLayout mFloatLayout;

    public boolean isVideo = false;
    public boolean isShowed = false;

    public FloatWindow(Context context) {
        this(context, null);
    }

    public FloatWindow(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatWindow(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    /**
     * 初始化
     *
     * @param context
     */
    protected void initView(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        if (wmParams == null) {
            wmParams = new WindowManager.LayoutParams();
        }
        mContext = context;



    }

    /**
     * 初始化layout
     *
     * @param layoutId
     */
    public void setLayout(int layoutId) {
        mFloatLayout = (RelativeLayout) LayoutInflater.from(mContext).inflate(layoutId, null);
    }

    /**
     * 初始化手势事件
     */
    protected void initEvents() {
        //设置监听浮动窗口的触摸移动
        mFloatLayout.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //这一段代码保证悬浮窗会回到屏幕右侧
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (mFloatLayout != null) {
                        wmParams.x = mWindowManager.getDefaultDisplay().getWidth();
                        //减25为状态栏的高度
                        wmParams.y = (int) event.getRawY() - mFloatLayout.getMeasuredHeight() / 2 - 25;
                        //刷新
                        mWindowManager.updateViewLayout(mFloatLayout, wmParams);
                    }
                }
                //注册手势监听
                return gestureDetector.onTouchEvent(event);
            }
        });
    }

    public void show() {
//        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
//        wmParams = new WindowManager.LayoutParams();
        //获取的是WindowManagerImpl.CompatModeWrapper
        //设置window type
        //原因1:type为"TYPE_TOAST"在sdk19之前不接收事件,之后可以.
        //原因2:type为"TYPE_PHONE"需要"SYSTEM_ALERT_WINDOW"权限.在sdk19之前不可以直接申明使用,之后不能直接申明使用.
        //ype : 窗口的显示类型，常用的类型说明如下：
//        --TYPE_SYSTEM_ALERT : 系统警告提示。
//        --TYPE_SYSTEM_ERROR : 系统错误提示。
//        --TYPE_SYSTEM_OVERLAY : 页面顶层提示。
//        --TYPE_SYSTEM_DIALOG : 系统对话框。
//        --TYPE_STATUS_BAR : 状态栏
//                --TYPE_TOAST : 短暂通知Toast
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            wmParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        } else {
            wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }

//        wmParams.type = getWindowType();

        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        //FLAG_NOT_FOCUSABLE设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;


        wmParams.x = mWindowManager.getDefaultDisplay().getHeight() -120;//减的值与wmParams.height相等，作用是使用户点击悬浮窗正中间时悬浮窗不会移动
//        wmParams.y = mWindowManager.getDefaultDisplay().getWidth();
        wmParams.y = mWindowManager.getDefaultDisplay().getWidth() / 4 + mWindowManager.getDefaultDisplay().getWidth() + 15;



        wmParams.width = 462;
        wmParams.height = 120;
//        mFloatLayout = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.float_picture_layout, null);

        //添加mFloatLayout
        mWindowManager.addView(mFloatLayout, wmParams);
    }

    public void close() {
        //移除悬浮窗口
        if (mFloatLayout != null) {

            mWindowManager.removeView(mFloatLayout);
//            mWindowManager.removeViewImmediate(mFloatLayout);
            mFloatLayout = null;
        }
        isShowed = false;
    }


//    /**
//     * 设置WindowManager.LayoutParams()的type值
//     * @return
//     */
//    private int getWindowType() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            //此type类型会爆红，暂时不用理会
//            return WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
//            return WindowManager.LayoutParams.TYPE_PHONE;
//        }
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (RomUtil.isMiui()) {
//                return WindowManager.LayoutParams.TYPE_PHONE;
//            }
//            return WindowManager.LayoutParams.TYPE_TOAST;
//        }
//
//        if (RomUtil.isMiui() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            return WindowManager.LayoutParams.TYPE_TOAST;
//        } else {
//            return WindowManager.LayoutParams.TYPE_PHONE;
//        }
//    }
}
