package com.example.jinhui.progressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ProgressBar;

/**
 * Email: 1004260403@qq.com
 * Created by jinhui on 2018/11/15.
 * <p>
 * 横向进度条
 * <p>
 * 需要找绘制的图形详解图，带文字讲解的那种
 */
public class HorizontalProgressBarWithProgress extends ProgressBar {

    private final String TAG = this.getContext().getClass().getSimpleName();

    private int DEFAULT_TEXT_SIZE = 10;  // 默认字体大小sp
    private int DEFAULT_TEXT_COLOR = 0xFFFC00D1;  // 默认字体颜色
    private int DEFAULT_COLOR_UNREACH = 0xFFD3D6DA;  // 默认未达到的颜色值
    private int DEFAULT_HEIGHT_UNREACH = 2;  // 默认未达到的高度dp
    private int DEFAULT_COLOR_REACH = DEFAULT_TEXT_COLOR;
    private int DEFAULT_HEIGHT_REACH = 2;  // 默认字体大小sp
    private int DEFAULT_TEXT_OFFSET = 10;  // 字体左右偏移量dp

    protected int mTextSize = sp2px(DEFAULT_TEXT_SIZE);
    protected int mTextColor = DEFAULT_TEXT_COLOR;
    protected int mUnReachColor = DEFAULT_COLOR_UNREACH;
    protected int mUnReachHeight = dp2px(DEFAULT_HEIGHT_UNREACH);
    protected int mReachColor = DEFAULT_COLOR_REACH;
    protected int mReachHeight = dp2px(DEFAULT_HEIGHT_REACH);
    protected int mTextOffset = dp2px(DEFAULT_TEXT_OFFSET);

    protected Paint mPaint = new Paint();
    protected int mRealWidth;


    public HorizontalProgressBarWithProgress(Context context) {
        this(context, null);
    }

    public HorizontalProgressBarWithProgress(Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
        this(context, attrs, 0);
//        obtainStyleAttrs(attrs);  // 注意: 这里直接解析不是我们要的效果，需要加上第三个构造函数
    }

    public HorizontalProgressBarWithProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainStyleAttrs(attrs);
    }

    /**
     * 获取自定义属性
     *
     * @param attrs
     */
    private void obtainStyleAttrs(AttributeSet attrs) {

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs,
                R.styleable.HorizontalProgressBarWithProgress);

        mTextSize = (int) typedArray.getDimension(
                R.styleable.HorizontalProgressBarWithProgress_progress_text_size, mTextSize);
        mTextColor = typedArray.getColor(
                R.styleable.HorizontalProgressBarWithProgress_progress_text_color, mTextColor);
        mTextOffset = (int) typedArray.getDimension(
                R.styleable.HorizontalProgressBarWithProgress_progress_text_offset, mTextOffset);

        mReachColor = typedArray.getColor(
                R.styleable.HorizontalProgressBarWithProgress_progress_reach_color, mReachColor);
        mReachHeight = (int) typedArray.getDimension(
                R.styleable.HorizontalProgressBarWithProgress_progress_reach_height, mReachHeight);
        mUnReachColor = typedArray.getColor(
                R.styleable.HorizontalProgressBarWithProgress_progress_unreach_color, mUnReachColor);
        mUnReachHeight = (int) typedArray.getDimension(
                R.styleable.HorizontalProgressBarWithProgress_progress_unreach_height, mUnReachHeight);
        typedArray.recycle();

        mPaint.setTextSize(mTextSize);  // 设置字体的size

    }

    /**
     * 控件的测量
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e(TAG, "onMeasure: ");
        // 拿到值与模式
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        // 高度的测量
        int height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(widthSize, height);  // 确定view的宽高

        mRealWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    }

    private int measureHeight(int heightMeasureSpec) {
        int result;
        // 高度模式
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        // 高度值
        int size = MeasureSpec.getSize(heightMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {   // 精确值
            result = size;
        } else {
            int textHeight = (int) (mPaint.descent() - mPaint.ascent());  // 文本高度
            result = getPaddingTop() + getPaddingBottom() +
                    Math.max(
                            Math.max(mReachHeight, mUnReachHeight),
                            Math.abs(textHeight));  // getPaddingTop() + getPaddingBottom()为必加项
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.e(TAG, "onLayout: ");
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e(TAG, "onDraw: ");
        canvas.save();

        canvas.translate(getPaddingLeft(), getHeight() / 2);

        boolean noNeedUnReach = false;

        // draw reach bar
        String text = getProgress() + "%";
        int textWidth = (int) mPaint.measureText(text);
        // radio 进度条绘制的长度
        float radio = getProgress() * 1.0f / getMax();
        float progressX = radio * mRealWidth;
        if (progressX + textWidth > mRealWidth) {
            progressX = mRealWidth - textWidth;
            noNeedUnReach = true;
        }

        float endX = progressX - mTextOffset / 2;
        if (endX > 0) {
            mPaint.setColor(mReachColor);
            mPaint.setStrokeWidth(mReachHeight);
            canvas.drawLine(0, 0, endX, 0, mPaint);
        }

        // draw text
        mPaint.setColor(mTextColor);
        int y = (int) (-(mPaint.descent() + mPaint.ascent()) / 2);
        canvas.drawText(text, progressX, y, mPaint);

        // draw unreach bar
        if (!noNeedUnReach) {
            float start = progressX + mTextOffset / 2 + textWidth;
            mPaint.setColor(mUnReachColor);
            mPaint.setStrokeWidth(mUnReachHeight);
            canvas.drawLine(start, 0, mRealWidth, 0, mPaint);
        }
        // 恢复canvas
        canvas.restore();

    }

    /**
     * dp转px
     *
     * @param dpValue
     * @return
     */
    protected int dp2px(int dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue,
                getResources().getDisplayMetrics());
    }

    /**
     * px转dp
     *
     * @param spValue
     * @return
     */
    protected int sp2px(int spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue,
                getResources().getDisplayMetrics());
    }

}
