package cz.fjerabek.thr10controller.ui

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
import androidx.core.content.res.use
import cz.fjerabek.thr10controller.R
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin


/**
 * UI Knob component
 * @param context application context
 * @param attributeSet view attribute set
 */
open class Knob(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    /**
     * Outer circle color
     */
    var outerColor = Color.parseColor("#1b1b1b")

    /**
     * Outer circle relative size
     */
    var outerRelativeSize = 0.8f

    /**
     * Inner circle color
     */
    var innerColor = Color.parseColor("#fbc02d")

    /**
     * Inner circle relative size
     */
    var innerRelativeSize = 0.8f

    /**
     * Pointer color
     */
    var pointerColor = Color.WHITE

    /**
     * Pointer relative length
     */
    var pointerRelativeLength = 0.25f

    /**
     * Pointre width
     */
    var pointerWidth = 5f

    /**
     * Name padding from knob
     */
    var namePadding = 25f

    /**
     * Name text color
     */
    var nameTextColor = Color.BLACK

    /**
     * Scale marker color
     */
    var markerColor = Color.DKGRAY

    /**
     * Name text size
     */
    var nameTextSize = 48f

    /**
     * Knob name
     */
    var name = ""

    /**
     * Scale type. Continuous is solid line. Non continuous shows scale with short lines
     */
    var continuous = false

    /**
     * Scale markings distance from knob
     */
    var markerRelativePadding = 0.1f

    /**
     * Scale markers relative length
     */
    var markerRelativeLength = 0.12f

    /**
     * Scale markers width
     */
    var markerWidth = 2f

    /**
     * Continuous scale. Color of selected portion
     */
    var selectedColor = Color.parseColor("#fbc02d")

    /**
     * Scale max angle
     */
    var maxAngle = Math.toRadians(25.0)

    /**
     * Scale min angle
     */
    var minAngle = Math.toRadians(335.0)

    /**
     * Move type. Relative moves relative to touch. Absolute points to touch position
     */
    var relativeMove = true

    /**
     * Size of value text inside knob
     */
    var valueTextSize = 48f

    /**
     * Value text color
     */
    var valueTextColor = Color.BLACK

    /**
     * Value to string converter. Allows showing other than integer values in value text
     */
    var valueStringConverter: ((value: Int) -> String) = { it -> it.toString() }

    /**
     * Value change callback
     */
    var onValueChangeListener: ((value: Int) -> Unit) = {}

    /**
     * Is knob being changed by swiping
     */
    var swipe = false
        private set

    /**
     * Knob value
     */
    var value: Int
        set(value) {
            if(!swipe) {
                angle = valueToAngle(value)
            }
            _value = value
            postInvalidate()
        }
        get() = _value

    /**
     * Minimal value
     */
    var minValue = 0
        set(value) {
            field = value
            anglePerMarker = (minAngle - maxAngle) / (maxValue - minValue)
        }

    /**
     * Maximum value
     */
    var maxValue = 100
        set(value) {
            field = value
            anglePerMarker = (minAngle - maxAngle) / (maxValue - minValue)
        }

    /**
     * Private non animated value.
     */
    private var _value = 0

    /**
     * Angle of knob
     */
    private var angle = (6 / 4) * Math.PI

    /**
     * Center position x value
     */
    private var centerX = 0f

    /**
     * Center position y value
     */
    private var centerY = 0f

    /**
     * Angle per scale marker
     */
    private var anglePerMarker = 0.0

    /**
     * Scale start angle
     */
    private var startAngle = 0.0

    /**
     * Value animator
     */
    private var animator: ValueAnimator? = null

    /**
     * Knob max radius
     */
    private var radius = 0f

    /**
     *  Value text line padding
     */
    private val linePadding: Int = 10

    /**
     * Drawing paint
     */
    private var paint = Paint()

    init {
        context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.Knob,
            0,
            0
        ).use {
            outerRelativeSize = it.getFloat(R.styleable.Knob_outer_relative_size, outerRelativeSize)
            innerRelativeSize = it.getFloat(R.styleable.Knob_inner_relative_size, innerRelativeSize)
            pointerRelativeLength =
                it.getFloat(R.styleable.Knob_pointer_relative_length, pointerRelativeLength)
            pointerWidth = it.getFloat(R.styleable.Knob_pointer_width, pointerWidth)
            markerRelativeLength =
                it.getFloat(R.styleable.Knob_marker_relative_length, markerRelativeLength)
            markerRelativePadding =
                it.getFloat(R.styleable.Knob_marker_relative_padding, markerRelativePadding)
            minAngle = Math.toRadians(it.getFloat(R.styleable.Knob_min_angle, 25f).toDouble())
            maxAngle = Math.toRadians(it.getFloat(R.styleable.Knob_max_angle, 335f).toDouble())

            continuous = it.getBoolean(R.styleable.Knob_continuous, false)
            markerWidth = it.getDimension(R.styleable.Knob_marker_width, markerWidth)
            namePadding = it.getDimension(R.styleable.Knob_name_padding, namePadding)
            nameTextSize = it.getDimension(R.styleable.Knob_name_text_size, nameTextSize)
            valueTextSize = it.getDimension(R.styleable.Knob_value_text_size, valueTextSize)

            name = it.getString(R.styleable.Knob_name) ?: name

            maxValue = it.getInt(R.styleable.Knob_max_value, maxValue)
            minValue = it.getInt(R.styleable.Knob_min_value, minValue)
            _value = it.getInt(R.styleable.Knob_value, 0)

            valueTextColor = it.getColor(R.styleable.Knob_value_text_color, valueTextColor)
            nameTextColor = it.getColor(R.styleable.Knob_name_text_color, nameTextColor)
            outerColor = it.getColor(R.styleable.Knob_outer_color, outerColor)
            innerColor = it.getColor(R.styleable.Knob_inner_color, innerColor)
            markerColor = it.getColor(R.styleable.Knob_marker_color, markerColor)
            selectedColor = it.getColor(R.styleable.Knob_selected_color, selectedColor)
            pointerColor = it.getColor(R.styleable.Knob_pointer_color, pointerColor)

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

    /**
     * Draws knob name
     * @param canvas canvas to draw to
     */
    private fun paintName(canvas: Canvas) {
        paint.isAntiAlias = true
        paint.color = nameTextColor
        paint.textSize = nameTextSize
        paint.style = Paint.Style.FILL
        paint.textAlign = Paint.Align.CENTER
        paint.strokeWidth = 2f

        canvas.drawText(name, centerX, centerY + radius + namePadding, paint)
    }

    /**
     * Paints arc scale
     * @param canvas canvas to draw arc scale to
     */
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

    /**
     * Paints center piece of knob (two circles)
     * @param canvas canvas to draw to
     */
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

    /**
     * Paints knob pointer
     * @param canvas canvas to draw to
     */
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

    /**
     * Paints marker scale
     * @param canvas canvas to draw to
     */
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

    /**
     * Sets up motion event listener
     */
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

    /**
     * Converts knob value to angle
     * @param value knob value
     * @return knob angle
     */
    private fun valueToAngle(value: Int): Double {
        return (value - minValue) * anglePerMarker + maxAngle
    }


    /**
     * Normalizes angle to max 2*pi value
     * @param angle value
     * @return normalized value
     */
    private fun normalizeAngle(angle: Double): Double {
        var normalized = angle
        while (normalized < 0) normalized += Math.PI * 2
        while (normalized > Math.PI * 2) normalized -= Math.PI * 2
        return normalized
    }

    /**
     * Converts knob angle to value and calls callback if value has changed
     * @param callback value changed callback
     */
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

    /**
     * Animates knob to new value
     * @param value value to animate knob to
     */
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