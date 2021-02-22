package cz.fjerabek.thr10controller.ui

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.animation.doOnEnd
import androidx.core.content.res.use
import cz.fjerabek.thr10controller.R
import timber.log.Timber
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin


open class Knob(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    var outerColor = Color.parseColor("#1b1b1b")
    var innerColor = Color.parseColor("#fbc02d")
    var outerRelativeSize = 0.8f
    var innerRelativeSize = 0.8f
    var pointerColor = Color.WHITE
    var valueTextColor = Color.BLACK
    var nameTextColor = Color.BLACK
    var pointerRelativeLength = 0.25f
    var pointerWidth = 5f
    var nameTextSize = 48f
    var valueTextSize = 48f
    var maxAngle = Math.toRadians(25.0)
    var minAngle = Math.toRadians(335.0)
    var name = ""
    var markerRelativePadding = 0.1f
    var markerRelativeLength = 0.12f
    var markerWidth = 2f
    var relativeMove = true
    var continuous = false
    var namePadding = 25f
    var selectedColor = Color.parseColor("#fbc02d")
    var valueStringConverter: ((value: Int) -> String) = { it -> it.toString() }
    var onValueChangeListener: ((value: Int) -> Unit) = {}
    var markerColor = Color.DKGRAY

    private var _value = 0
    var value: Int
        set(value) {
            if(!swipe) {
                angle = valueToAngle(value)
            }
            _value = value
            postInvalidate()
        }
        get() = _value

    var minValue = 0
        set(value) {
            field = value
            anglePerMarker = (minAngle - maxAngle) / (maxValue - minValue)
        }

    var maxValue = 100
        set(value) {
            field = value
            anglePerMarker = (minAngle - maxAngle) / (maxValue - minValue)
        }

    private var angle = (6 / 4) * Math.PI
    private var centerX = 0f
    private var centerY = 0f
    private var swipe = false
    private var anglePerMarker = 0.0
    private var startAngle = 0.0
    private var animator: ValueAnimator? = null
    private var radius = 0f
    private val linePadding: Int = 10
    private var paint = Paint()

    init {
        context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.Knob,
            0,
            0
        ).use {
            outerColor = it.getColor(R.styleable.Knob_outer_color, outerColor)
            innerColor = it.getColor(R.styleable.Knob_inner_color, innerColor)
            outerRelativeSize = it.getFloat(R.styleable.Knob_outer_relative_size, outerRelativeSize)
            innerRelativeSize = it.getFloat(R.styleable.Knob_inner_relative_size, innerRelativeSize)
            pointerColor = it.getColor(R.styleable.Knob_pointer_color, pointerColor)
            pointerRelativeLength =
                it.getFloat(R.styleable.Knob_pointer_relative_length, pointerRelativeLength)
            pointerWidth = it.getFloat(R.styleable.Knob_pointer_width, pointerWidth)
            nameTextSize = it.getDimension(R.styleable.Knob_name_text_size, nameTextSize)
            valueTextSize = it.getDimension(R.styleable.Knob_value_text_size, valueTextSize)
            name = it.getString(R.styleable.Knob_name) ?: name
            valueTextColor = it.getColor(R.styleable.Knob_value_text_color, valueTextColor)
            nameTextColor = it.getColor(R.styleable.Knob_name_text_color, nameTextColor)
            maxValue = it.getInt(R.styleable.Knob_max_value, maxValue)
            minValue = it.getInt(R.styleable.Knob_min_value, minValue)
            markerRelativeLength =
                it.getFloat(R.styleable.Knob_marker_relative_length, markerRelativeLength)
            markerRelativePadding =
                it.getFloat(R.styleable.Knob_marker_relative_padding, markerRelativePadding)
            markerColor = it.getColor(R.styleable.Knob_marker_color, markerColor)
            markerWidth = it.getDimension(R.styleable.Knob_marker_width, markerWidth)
            minAngle = Math.toRadians(it.getFloat(R.styleable.Knob_min_angle, 25f).toDouble())
            maxAngle = Math.toRadians(it.getFloat(R.styleable.Knob_max_angle, 335f).toDouble())
            _value = it.getInt(R.styleable.Knob_value, 0)
            continuous = it.getBoolean(R.styleable.Knob_continuous, false)
            selectedColor = it.getColor(R.styleable.Knob_selected_color, selectedColor)
            namePadding = it.getDimension(R.styleable.Knob_name_padding, namePadding)


            if (value > maxValue || value < minValue) error("Value must be between minValue and maxValue")
            if (minValue > maxValue) error("Min value must be less than max value")
            if (maxValue < minValue) error("Max value must be more than min value")
            if (maxAngle < 0) error("Min angle cannot be less than 0")
            if (minAngle > Math.PI * 2) error("Max angle cannot be more than 360")

        }
        anglePerMarker = (minAngle - maxAngle) / (maxValue - minValue)
        angle = valueToAngle(value)

        setupListener()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if(canvas == null) return

        centerX = width / 2f
        centerY = height / 2f

        radius = ((if (centerY > centerX) centerX else centerY) * 0.8f)

        paintName(canvas)
        paintCenter(canvas)
        paintPointer(canvas)

        if (!continuous) {
            paintMarkers(canvas)
        } else {
            paintArc(canvas)
        }
    }

    private fun paintName(canvas: Canvas) {
        paint.isAntiAlias = true
        paint.color = nameTextColor
        paint.textSize = nameTextSize
        paint.style = Paint.Style.FILL
        paint.textAlign = Paint.Align.CENTER
        paint.strokeWidth = 2f

        canvas.drawText(name, centerX, centerY + radius + namePadding, paint)
    }

    private fun paintArc(canvas: Canvas) {
        val rectangle = RectF(
            centerX - (outerRelativeSize * radius) - (markerRelativePadding * radius),
            centerY - (outerRelativeSize * radius) - (markerRelativePadding * radius),
            centerX + (outerRelativeSize * radius) + (markerRelativePadding * radius),
            centerY + (outerRelativeSize * radius) + (markerRelativePadding * radius)
        )

        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = markerRelativeLength * radius

        val startAngle = Math.toDegrees(minAngle).toFloat()
        val stopAngle = Math.toDegrees(maxAngle).toFloat()


        paint.color = selectedColor
        canvas.drawArc(
            rectangle,
            90 - stopAngle,
            stopAngle - Math.toDegrees(angle).toFloat(),
            false,
            paint
        )
        paint.color = markerColor
        canvas.drawArc(
            rectangle,
            -Math.toDegrees(angle).toFloat() + 90,
            Math.toDegrees(angle).toFloat() - startAngle,
            false,
            paint
        )
    }

    private fun paintCenter(canvas: Canvas) {
        val outerRadius = outerRelativeSize * radius
        val innerRadius = innerRelativeSize * outerRadius

        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        paint.color = outerColor

        canvas.drawCircle(centerX, centerY, outerRadius, paint)

        paint.color = innerColor
        canvas.drawCircle(centerX, centerY, innerRadius, paint)

        paint.color = valueTextColor
        paint.textSize = valueTextSize
        paint.textAlign = Paint.Align.CENTER

        val textSplit = valueStringConverter(value).split("\n")
        var textY =
            (centerY - ((paint.descent() + paint.ascent()) / 2)) + (textSplit.size - 1) * (((paint.descent() + paint.ascent() + linePadding) / 2))



        textSplit.forEach {
            canvas.drawText(
                it,
                centerX,
                textY,
                paint
            )
            textY -= (paint.descent() + paint.ascent()) - linePadding
        }
    }

    private fun paintPointer(canvas: Canvas) {
        val outerRadius = outerRelativeSize * radius
        val startX = centerX + (outerRadius * (1 - pointerRelativeLength) * sin(angle))
        val startY = centerY + (outerRadius * (1 - pointerRelativeLength) * cos(angle))
        val endX = centerX + (outerRadius * sin(angle))
        val endY = centerY + (outerRadius * cos(angle))

        paint.color = pointerColor
        paint.strokeWidth = pointerWidth
        canvas.drawLine(startX.toFloat(), startY.toFloat(), endX.toFloat(), endY.toFloat(), paint)
    }

    private fun paintMarkers(canvas: Canvas) {
        paint.strokeWidth = markerWidth
        paint.color = markerColor

        val outerRadius = outerRelativeSize * radius
        var markerAngle = maxAngle
        val statusCount = maxValue - minValue

        for (i in 0..statusCount) {
            val startX =
                centerX + ((outerRadius + (radius * markerRelativePadding)) * sin(markerAngle))
            val startY =
                centerY + ((outerRadius + (radius * markerRelativePadding)) * cos(markerAngle))
            val endX =
                centerX + ((outerRadius + (radius * markerRelativePadding + radius * markerRelativeLength)) * sin(
                    markerAngle
                ))
            val endY =
                centerY + ((outerRadius + (radius * markerRelativePadding + radius * markerRelativeLength)) * cos(
                    markerAngle
                ))

            canvas.drawLine(
                startX.toFloat(),
                startY.toFloat(),
                endX.toFloat(),
                endY.toFloat(),
                paint
            )

            markerAngle += anglePerMarker
        }
    }

    private fun setupListener() {
        setOnTouchListener { view, motionEvent ->
            val distanceX = motionEvent.x - centerX
            val distanceY = motionEvent.y - centerY

            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (distanceX.toDouble().pow(2) + distanceY.toDouble().pow(2) > radius.pow(2)) {
                        return@setOnTouchListener false
                    }
                    animator?.cancel()
                    swipe = false

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        view.requestPointerCapture()
                    }

                    if (view.parent != null) {
                        view.parent.requestDisallowInterceptTouchEvent(true)
                    }
                    return@setOnTouchListener true
                }
                MotionEvent.ACTION_MOVE -> {
                    val x = motionEvent.x
                    val y = motionEvent.y

                    if (relativeMove) {
                        if (!swipe) {
                            startAngle = -atan2(
                                y - centerY.toDouble(),
                                x - centerX.toDouble()
                            ) + (Math.PI / 2)
                        } else {
                            val currAngle = -atan2(
                                y - centerY.toDouble(),
                                x - centerX.toDouble()
                            ) + (Math.PI / 2)
                            angle += currAngle - startAngle
                            startAngle = currAngle
                        }
                    } else {
                        angle = -atan2(
                            y - centerY.toDouble(),
                            x - centerX.toDouble()
                        ) + (Math.PI / 2)
                    }
                    swipe = true
                    angleToValue(true)
                    postInvalidate()
                    return@setOnTouchListener true
                }

                MotionEvent.ACTION_UP -> {
                    if (!swipe) {
                        this.performClick()
                        _value++
                        if (value > maxValue)
                            _value = maxValue

                        if (value < minValue)
                            _value = minValue

                        onValueChangeListener(_value)
                        animateValue(_value)

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            view.requestPointerCapture()
                        }
                    } else {
                        animateValue(value)
                    }
                    swipe = false
                    return@setOnTouchListener true
                }
                else -> {
                    return@setOnTouchListener false
                }
            }
        }
    }

    private fun valueToAngle(value: Int): Double {
        return (value - minValue) * anglePerMarker + maxAngle
    }


    private fun normalizeAngle(angle: Double): Double {
        var normalized = angle
        while (normalized < 0) normalized += Math.PI * 2
        while (normalized > Math.PI * 2) normalized -= Math.PI * 2
        return normalized
    }

    private fun angleToValue(callback: Boolean) {
        angle = normalizeAngle(angle)

        val min = normalizeAngle(maxAngle)
        val max = normalizeAngle(minAngle)

        if (angle < max) {
            angle = max
        }

        if (angle > min) {
            angle = min
        }

        val newValue =
            (minValue + (((angle - maxAngle) + (anglePerMarker / 2)) / anglePerMarker)).toInt()

        if (_value != newValue) {
            _value = newValue
            if(callback)
                onValueChangeListener(_value)
        }

    }

    fun animateValue(value: Int) {
        animator?.cancel()
        animator = ValueAnimator.ofFloat(angle.toFloat(), valueToAngle(value).toFloat())
        animator?.addUpdateListener {
            angle = (it.animatedValue as Float).toDouble()
            angleToValue(false)
            invalidate()
        }
        animator?.duration = 100
        animator?.start()
    }
}