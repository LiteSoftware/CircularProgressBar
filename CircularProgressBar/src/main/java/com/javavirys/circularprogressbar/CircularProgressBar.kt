/*
 * Copyright 2021 Vitaliy Sychov. All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.javavirys.circularprogressbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.FrameLayout
import kotlin.math.min

class CircularProgressBar
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val outerArcPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val progressPaint: Paint

    private val arrowPaint: Paint

    private val smallPointPaint: Paint

    private val bigPointPaint: Paint

    private var pProgress: Int

    init {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.CircularProgressBar,
            defStyleAttr,
            defStyleRes
        )

        val outerArcWidth =
            typedArray.getDimension(R.styleable.CircularProgressBar_outerArcWidth, 12f)

        val outerArcColor =
            typedArray.getColor(R.styleable.CircularProgressBar_outerArcColor, 0x33FFFFFF)

        val progressColor =
            typedArray.getColor(R.styleable.CircularProgressBar_outerArcColor, 0xAAFFFFFF.toInt())

        val lineWidth =
            typedArray.getDimension(R.styleable.CircularProgressBar_lineWidth, 8f)

        val lineColor =
            typedArray.getColor(R.styleable.CircularProgressBar_lineColor, 0xFFFFFFFF.toInt())

        val bigPointSize =
            typedArray.getDimension(R.styleable.CircularProgressBar_bigPointSize, 20f)

        val smallPointSize =
            typedArray.getDimension(R.styleable.CircularProgressBar_smallPointSize, 8f)

        pProgress =
            typedArray.getInt(R.styleable.CircularProgressBar_progress, 0)
        if (pProgress > 100) pProgress = 100
        else if (pProgress < 0) pProgress = 0

        outerArcPaint.style = Paint.Style.STROKE
        outerArcPaint.strokeWidth = outerArcWidth
        outerArcPaint.strokeCap = Paint.Cap.ROUND
        outerArcPaint.color = outerArcColor

        progressPaint = Paint(outerArcPaint)
        progressPaint.color = progressColor

        arrowPaint = Paint(progressPaint)
        arrowPaint.color = lineColor
        arrowPaint.strokeWidth = lineWidth

        smallPointPaint = Paint(arrowPaint)
        smallPointPaint.strokeWidth = smallPointSize

        bigPointPaint = Paint(arrowPaint)
        bigPointPaint.strokeWidth = bigPointSize

        typedArray.recycle()

        setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawCenterCircles(canvas)
        drawOuterArc(canvas)
    }

    private fun drawCenterCircles(canvas: Canvas) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL_AND_STROKE
            strokeCap = Paint.Cap.ROUND
            color = outerArcPaint.color
        }

        var radius = min(width, height) / 8f
        canvas.drawCircle(width / 2f, height / 2f, radius, paint)
        radius = min(width, height) / 5f
        canvas.drawCircle(width / 2f, height / 2f, radius, paint)
    }

    private fun drawOuterArc(canvas: Canvas) {
        val radius =
            min(width / 2f - outerArcPaint.strokeWidth, height / 2f - outerArcPaint.strokeWidth)
        val centerX = width / 2f
        val centerY = height / 2f
        val oval = RectF()
        oval.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius)
        canvas.drawArc(oval, 135f, 270f, false, outerArcPaint)
        val progressAngle = 270f * pProgress / 100f
        canvas.drawArc(oval, 135f, progressAngle, false, progressPaint)

        canvas.save()
        canvas.translate(width / 2f, height / 2f)
        canvas.rotate(45f + progressAngle)
        canvas.drawLine(0f, 0f, 0f, radius, arrowPaint)
        canvas.drawPoint(0f, 0f, bigPointPaint)
        canvas.drawPoint(0f, radius, smallPointPaint)

        canvas.restore()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val defaultWidth = 190 + paddingLeft + paddingRight
        val defaultHeight = 190 + paddingTop + paddingBottom
        val measuredWidth = reconcileSize(defaultWidth, widthMeasureSpec)
        val measuredHeight = reconcileSize(defaultHeight, heightMeasureSpec)

        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    private fun reconcileSize(size: Int, measureSpec: Int): Int {
        val measureSpecMode = MeasureSpec.getMode(measureSpec)
        val measureSpecSize = MeasureSpec.getSize(measureSpec)
        return when (measureSpecMode) {
            MeasureSpec.EXACTLY -> measureSpecSize
            MeasureSpec.UNSPECIFIED -> size
            MeasureSpec.AT_MOST -> if (measureSpecSize < size) measureSpecSize else size
            else -> size
        }
    }

    fun getProgress() = pProgress

    fun setProgress(progress: Int) {
        if (progress != pProgress) {
            pProgress = when {
                pProgress > 100 -> 100
                pProgress < 0 -> 0
                else -> progress
            }
            invalidate()
        }
    }
}