package com.example.jinhui.progressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by Apfel on 2017/3/5.
 */

public class CustomTitleView extends View {

    //  文本
    private String mTitleText;
    //  颜色
    private int mTitleTextColor;
    //  大小
    private int mTitleTextSize;
    //
    private Rect mRect;
    //
    private Paint mPaint;

    public CustomTitleView(Context context) {
        //super(context);
        this(context, null);
    }

    public CustomTitleView(Context context, @Nullable AttributeSet attrs) {
        //super(context, attrs);
        this(context, attrs, 0);
    }

    /**
     * 自定义视图的属性
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public CustomTitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        //
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomTitleView, defStyleAttr, 0);
        //
        int count = typedArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.CustomTitleView_titleText:
                    mTitleText = typedArray.getString(attr);
                    break;
                case R.styleable.CustomTitleView_titleTextColor:
                    mTitleTextColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CustomTitleView_titleTextSize:
                    //  设置字体大小，默认16sp
                    mTitleTextSize = typedArray.getDimensionPixelSize(attr,
                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
            }
        }
        typedArray.recycle();

        //
        mPaint = new Paint();
        mPaint.setTextSize(mTitleTextSize);
        mRect = new Rect();
        mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mRect);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mTitleText = randomText();
                postInvalidate();
            }
        });
    }

    //  随机文本
    private String randomText() {

        Random random = new Random();
        Set<Integer> set = new HashSet<Integer>();
        while (set.size() < 4) {
            int randomInt = random.nextInt(10);
            set.add(randomInt);
        }
        StringBuilder sb = new StringBuilder();
        for (Integer i : set) {
            sb.append("").append(i);
        }
        return sb.toString();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            mPaint.setTextSize(mTitleTextSize);
            mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mRect);
            float textWidth = mRect.width();
            int desired = (int) (getPaddingLeft() + textWidth + getPaddingRight());
            width = desired;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            mPaint.setTextSize(mTitleTextSize);
            mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mRect);
            float textHeight = mRect.height();
            int desired = (int) (getPaddingTop() + textHeight + getPaddingBottom());
            height = desired;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.BLUE);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);
        mPaint.setColor(mTitleTextColor);
        canvas.drawText(mTitleText, getWidth() / 2 - mRect.width() / 2, getHeight() / 2 + mRect.height() / 2, mPaint);
    }
}
