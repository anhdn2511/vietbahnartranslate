package com.vietbahnartranslate.view.favourite

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.vietbahnartranslate.R
import com.vietbahnartranslate.model.data.Translation

class FavouriteItemCustomView(private val adapter: FavouriteItemAdapter, context: Context, attrs: AttributeSet? = null) : View(context, attrs){

    private val mHeight = (100 * resources.displayMetrics.density).toInt()

    private val mTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)

    private val mIconSize = (30 * resources.displayMetrics.density).toInt()

    private var mStar = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_star_24, null)

    init {
        mTextPaint.color = Color.BLACK
        mTextPaint.textSize = 16 * resources.displayMetrics.scaledDensity
        mTextPaint.textAlign = Paint.Align.LEFT
    }

    private var mVietnamese: String = ""
    private var mBahnaric : String = ""
    private var mId: Int? = 0


    private var mIsFavourite : Boolean = true
    private fun setIsFavourite(value: Boolean) {
        mIsFavourite = value
        mStar =
            if(value) ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_star_24, null)
            else ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_star_outline_24, null)
        invalidate()
    }

    fun setData(data: Translation) {
        mVietnamese = data.vietnamese.toString()
        mBahnaric = data.bahnaric.toString()
        mIsFavourite = data.isFavourite == true
        mId = data.id
        mStar =
            if(mIsFavourite) ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_star_24, null)
            else ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_star_outline_24, null)
        invalidate()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = mHeight

        val resolvedWidth = resolveSize(width, widthMeasureSpec)
        val resolvedHeight = resolveSize(height, heightMeasureSpec)

        val measuredWidth = MeasureSpec.makeMeasureSpec(resolvedWidth, widthMeasureSpec)
        val measuredHeight = MeasureSpec.makeMeasureSpec(resolvedHeight, heightMeasureSpec)

        setMeasuredDimension(measuredWidth, measuredHeight)
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawVietnamese(canvas)
        drawBahnaric(canvas)
        //drawSpeakButton(canvas)
        drawStarButton(canvas)
    }

    private fun drawVietnamese(canvas: Canvas?) {
        val x = 20 * resources.displayMetrics.density
        val y = mHeight/2 - mTextPaint.textSize
        mTextPaint.typeface = Typeface.DEFAULT_BOLD
        canvas?.drawText(mVietnamese, x, y, mTextPaint)
    }
    private fun drawBahnaric(canvas: Canvas?){
        val x = 20 * resources.displayMetrics.density
        val y = mHeight/2 + mTextPaint.textSize
        mTextPaint.typeface = Typeface.DEFAULT
        canvas?.drawText(mBahnaric, x, y, mTextPaint)
    }
    private fun drawStarButton(canvas: Canvas?) {
        val left = width - mIconSize*2
        val right = left + mIconSize
        val top = mHeight/2 - mIconSize/2
        val bottom = top + mIconSize
        mStar?.setBounds(
            left,
            top,
            right,
            bottom
        )
        if (canvas != null) {
            mStar?.draw(canvas)
        }
    }
    private fun drawSpeakButton(canvas: Canvas?) {}

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event?.x
        val y = event?.y
        var checkX = false
        var checkY = false
        val starXPosition = (width - mIconSize*2).toFloat()
        val starYPosition = (mHeight/2 - mIconSize/2).toFloat()
        if (x != null) {
            if (x >= starXPosition && x <= (starXPosition + mIconSize)) checkX = true
        }
        if (y != null) {
            if (y >= starYPosition && y <= (starYPosition + mIconSize)) checkY = true
        }
        when(event?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                if (checkX && checkY) {
                    setIsFavourite(!mIsFavourite)
                    // TODO: handle save liked word to Favourite List
                    adapter.onFavouriteClick(mId, mIsFavourite)
                }
            }
        }
        return false
    }
}