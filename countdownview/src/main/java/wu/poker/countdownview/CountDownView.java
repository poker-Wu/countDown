package wu.poker.countdownview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by pokerwu on 16-8-5.
 *
 * 一个简单的倒计时view
 */
public class CountDownView extends View {
    //4dp
    private int padding = 4;

    private int strokeColor = 0xff4caf50;
    private int numberStartColor = 0xff4caf50;
    private int numberEndColor = 0xfff44336;
    private int color;
    private int number = 60;
    private Rect textRect;
    //24sp
    private float numberSize = 24;
    private int delay = 1000;
    //2dp
    private float strokeWidth = 2;
    private float progress = 0;

    private Paint strokePaint;
    private Paint numberPaint;
    private Paint bgPaint;

    private Context context;

    private float centerX;
    private float centerY;
    private float radius;
    private RectF strokeOval;

    public CountDownView(Context context) {
        this(context,null);
    }

    public CountDownView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CountDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CountDownView);
        strokeColor = array.getColor(R.styleable.CountDownView_cd_stroke_color,strokeColor);
        numberStartColor = array.getColor(R.styleable.CountDownView_cd_number_start_color,numberStartColor);
        numberEndColor = array.getColor(R.styleable.CountDownView_cd_number_end_color,numberEndColor);
        number = array.getInt(R.styleable.CountDownView_cd_number, number);
        numberSize = array.getDimension(R.styleable.CountDownView_cd_number_size, DensityUtil.sp2px(context,numberSize));
        delay = array.getInt(R.styleable.CountDownView_cd_delay,delay);
        strokeWidth = array.getDimension(R.styleable.CountDownView_cd_stroke_width, DensityUtil.dip2px(context,strokeWidth));
        array.recycle();
        padding = DensityUtil.dip2px(context,padding);
        color = numberStartColor;
        this.context = context;
        init();
    }

    private void init() {

        textRect = new Rect();

        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setStyle(Paint.Style.STROKE);

        numberPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        numberPaint.setTypeface(Typeface.DEFAULT_BOLD);
        numberPaint.setTextAlign(Paint.Align.CENTER);

        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setColor(Color.WHITE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specModeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int specModeHeight = MeasureSpec.getMode(heightMeasureSpec);
        int suggestWidth = MeasureSpec.getSize(widthMeasureSpec);
        int suggestHeight = MeasureSpec.getSize(heightMeasureSpec);

        int measureWidth;
        int measureHeight;
        numberPaint.setTextSize(numberSize);
        numberPaint.getTextBounds(String.valueOf(number),0,String.valueOf(number).length(), textRect);
        int textWidth = textRect.width();
        int textHeight = textRect.height();
        //文字和边距有16dp,draw的位置有4dp不draw任何东西
        measureHeight = measureWidth = Math.max(textHeight,textWidth)
                + DensityUtil.dip2px(context,16) + (int)strokeWidth + padding;

        if (specModeWidth == MeasureSpec.AT_MOST && specModeHeight == MeasureSpec.AT_MOST)
            setMeasuredDimension(measureWidth, measureHeight);
        else if (specModeWidth == MeasureSpec.AT_MOST)
            setMeasuredDimension(measureWidth,suggestHeight);
        else if(specModeHeight == MeasureSpec.AT_MOST)
            setMeasuredDimension(suggestWidth,measureHeight);
        else
            setMeasuredDimension(suggestWidth,suggestHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        strokePaint.setColor(strokeColor);
        strokePaint.setStrokeWidth(strokeWidth);

        numberPaint.setColor(color);

        canvas.drawCircle(centerX,centerY,radius,bgPaint);
        //draw number
        Paint.FontMetrics fontMetrics = numberPaint.getFontMetrics();
        canvas.drawText(String.valueOf(number - (int) progress),centerX,
               centerY - (fontMetrics.bottom -fontMetrics.top)/2 -fontMetrics.top,numberPaint);
        float endAngle = progress/ number * 360.0f;

        canvas.drawArc(strokeOval,0,endAngle,false,strokePaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        radius = Math.min(w,h) / 2.0f - padding;
        centerX = w / 2.0f;
        centerY = h / 2.0f;
        strokeOval = new RectF(centerX-radius-strokeWidth,centerY-radius-strokeWidth,
                centerX+radius+strokeWidth,centerY+radius+strokeWidth);
    }

    public void startCount(){
        new ViewAnimatorWrap(this).star();
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
    }

    public int getNumberStartColor() {
        return numberStartColor;
    }

    public void setNumberStartColor(int numberStartColor) {
        this.numberStartColor = numberStartColor;
    }

    public int getNumberEndColor() {
        return numberEndColor;
    }

    public void setNumberEndColor(int numberEndColor) {
        this.numberEndColor = numberEndColor;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        invalidate();
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public float getNumberSize() {
        return numberSize;
    }

    public void setNumberSize(float numberSize) {
        this.numberSize = numberSize;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        requestLayout();
    }

    public class ViewAnimatorWrap {
        private ObjectAnimator strokeAnimator;
        private ObjectAnimator numberColorAnimator;
        private AnimatorSet animatorSet;
        private CountDownView view;

        public ViewAnimatorWrap (CountDownView view){
            this.view = view;
            strokeAnimator = ObjectAnimator.ofFloat(view,"progress",0,number);
            strokeAnimator.setInterpolator(new LinearInterpolator());

            numberColorAnimator = ObjectAnimator.ofArgb(view,"color",numberStartColor,numberEndColor);
            animatorSet = new AnimatorSet();
            animatorSet.playTogether(strokeAnimator,numberColorAnimator);
            animatorSet.setDuration(number * delay);
            animatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {}

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (listener != null)
                        listener.onComplete();
                }

                @Override
                public void onAnimationCancel(Animator animation) {}

                @Override
                public void onAnimationRepeat(Animator animation) {}
            });
        }

        public void star(){
            animatorSet.start();
        }
    }

    public interface CompleteListener{
        void onComplete();
    }

    private CompleteListener listener;

    public void setListener(CompleteListener listener){
        this.listener = listener;
    }
}
