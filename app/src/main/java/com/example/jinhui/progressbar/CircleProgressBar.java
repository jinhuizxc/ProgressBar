package com.example.jinhui.progressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Email: 1004260403@qq.com
 * Created by jinhui on 2018/11/18.
 * <p>
 * 圆形进度条
 * <p>
 * android绘图Paint.setAntiAlias()和Paint.setDither()方法的作用
 * https://blog.csdn.net/lovexieyuan520/article/details/50732023
 */
public class CircleProgressBar extends HorizontalProgressBarWithProgress {

    // 半径
    private int mRadius = dp2px(30);

    private int mMaxPaintWidth;

    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mReachHeight = (int) (mUnReachHeight * 2.5f); // 美观

//        TypedArray typedArray = context.obtainStyledAttributes(attrs,
//                R.styleable.CircleProgressBarWithProgress);
//
//        mRadius = (int) typedArray.getDimension(
//                R.styleable.CircleProgressBarWithProgress_radius,
//                mRadius);
//        typedArray.recycle();
        // 获取自定义属性
        TypedArray ta = context.obtainStyledAttributes(
                attrs,
                R.styleable.CircleProgressBarWithProgress);
        mRadius = (int) ta.getDimension(
                R.styleable.CircleProgressBarWithProgress_radius,
                mRadius);
        ta.recycle();


        // 设置属性
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true); // 抗锯齿
        mPaint.setDither(true); // 设置防抖动
        mPaint.setStrokeCap(Paint.Cap.ROUND); // 连接处弧形

    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mMaxPaintWidth = Math.max(mReachHeight, mUnReachHeight);
        // 默认四个padding一致
        int expect = mRadius * 2 + mMaxPaintWidth + getPaddingLeft() + getPaddingRight();

        int width = resolveSize(expect, widthMeasureSpec);
        int height = resolveSize(expect, heightMeasureSpec);

        int realWidth = Math.min(width, height);

        mRadius = (realWidth - getPaddingLeft() - getPaddingRight() - mMaxPaintWidth) / 2;

        setMeasuredDimension(realWidth, realWidth);

    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
//        super.onDraw(canvas);  // 这里切记要注释掉，不然会重写父控件

        String text = getProgress() + "%";
        float textWidth = mPaint.measureText(text);
        float textHeight = (mPaint.descent() + mPaint.ascent()) / 2;

        canvas.save();

        canvas.translate(getPaddingLeft() + mMaxPaintWidth / 2, getPaddingTop() + mMaxPaintWidth / 2);
        mPaint.setStyle(Paint.Style.STROKE);
        // 绘制 UnReachBar
        mPaint.setColor(mUnReachColor);
        mPaint.setStrokeWidth(mUnReachHeight);
        canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
        // 绘制 ReachBar
        mPaint.setColor(mReachColor);
        mPaint.setStrokeWidth(mReachHeight);
        float sweepAngle = getProgress() * 1.0f / getMax() * 360; // 计算弧度
        canvas.drawArc(
                new RectF(0, 0, mRadius * 2, mRadius * 2),
                0,
                sweepAngle,
                false,
                mPaint);
        // 绘制Text
        mPaint.setColor(mTextColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawText(text, mRadius - textWidth / 2, mRadius - textHeight, mPaint);

        // 恢复canvas
        canvas.restore();

    }
}
