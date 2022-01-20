/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 Arman Sargsyan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.github.kabirnayeem99.resumade.common.views

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Point
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper
import com.zerobranch.layout.R

/**
 * This project provides an opportunity to perform swipe for any layout,
 * in the direction specified by you.
 *
 *
 * Date: 2018-09-27
 * Repository #https://github.com/zerobranch/SwipeLayout
 *
 * @author Arman Sargsyan
 */
class SwipeLayout(context: Context?, attrs: AttributeSet?) :
    FrameLayout((context)!!, attrs) {
    /**
     * Get current direction of a swipe
     */
    /**
     * Current direction of a swipe
     */
    var currentDirection: Int
        private set
    /**
     * Is move the secondary view along with the main view
     */
    /**
     * The secondary view will move along with the main view
     */
    var isTogether: Boolean
        private set
    /**
     * Is enabled Swipe
     *
     * @return True if swipe is enabled, false otherwise.
     */
    /**
     * Set the enabled swipe.
     *
     * @param enabledSwipe True if swipe is enabled, false otherwise.
     */
    /**
     * Is enabled Swipe
     */
    var isEnabledSwipe: Boolean
    /**
     * Swipe to the end of the screen.
     * Can work without a secondary view [.staticLeftView] and [.staticRightView]
     *
     *
     * If a particular direction of the swipe is used ([.LEFT] or [.RIGHT]),
     * and this flag is set, then [.isFreeDragAfterOpen] always will be true.
     *
     *
     * If the left and right directions of the swipe are used simultaneously ([.HORIZONTAL]),
     * then this flag will be ignored
     */
    /**
     * Swipe to the end of the screen.
     * Can work without a secondary view [.staticLeftView] and [.staticRightView]
     *
     *
     * If a particular direction of the swipe is used ([.LEFT] or [.RIGHT]),
     * and this flag is set, then [.isFreeDragAfterOpen] always will be true.
     *
     *
     * If the left and right directions of the swipe are used simultaneously ([.HORIZONTAL]),
     * then this flag will be ignored
     */
    var isContinuousSwipe: Boolean
        private set
    /**
     * Moving the main view after it was open.
     *
     *
     * if [.isEmptyLeftView] or [.isEmptyRightView],
     * then this flag will be ignored
     */
    /**
     * Moving the main view after it was open.
     *
     *
     * if [.isEmptyLeftView] or [.isEmptyRightView],
     * then this flag will be ignored
     */
    var isFreeDragAfterOpen: Boolean
        private set
    /**
     * If a particular direction of the swipe is used ([.LEFT] or [.RIGHT]),
     * then this flag allows you to do the swipe in the opposite direction.
     *
     *
     * If the horizontal direction is used ([.HORIZONTAL]),
     * this flag allows you to move the main view continuously in both directions
     */
    /**
     * If a particular direction of the swipe is used ([.LEFT] or [.RIGHT]),
     * then this flag allows you to do the swipe in the opposite direction.
     *
     *
     * If the horizontal direction is used ([.HORIZONTAL]),
     * this flag allows you to move the main view continuously in both directions
     */
    var isFreeHorizontalDrag: Boolean
        private set
    /**
     * Get the right bounding border of the swipe for the main view
     */
    /**
     * The right bounding border of the swipe for the main view
     */
    var rightDragViewPadding: Int
        private set
    /**
     * Get the left bounding border of the swipe for the main view
     */
    /**
     * The left bounding border of the swipe for the main view
     */
    var leftDragViewPadding: Int
        private set

    /**
     * Sensitivity of automatic closing of the main view
     */
    private val autoOpenSpeed: Double

    /**
     * Disable intercept touch event for draggable view
     */
    private var disallowIntercept: Boolean = false
    private var currentDraggingState: Int = ViewDragHelper.STATE_IDLE
    private var dragHelper: ViewDragHelper? = null
    private var gestureDetector: GestureDetectorCompat? = null
    private var draggingViewLeft: Int = 0
    private var horizontalWidth: Int = 0

    /**
     * Is open left view
     */
    var isLeftOpen: Boolean = false
        private set

    /**
     * Is open right view
     */
    var isRightOpen: Boolean = false
        private set
    private val staticRightViewId: Int
    private val staticLeftViewId: Int
    private val draggedViewId: Int
    private var draggedView: View? = null
    private var staticRightView: View? = null
    private var staticLeftView: View? = null
    private var actionsListener: SwipeActionsListener? = null

    init {
        val typedArray: TypedArray =
            getContext().obtainStyledAttributes(attrs, R.styleable.SwipeLayout)
        currentDirection = typedArray.getInteger(R.styleable.SwipeLayout_swipeDirection, LEFT)
        isFreeDragAfterOpen =
            typedArray.getBoolean(R.styleable.SwipeLayout_isFreeDragAfterOpen, false)
        isFreeHorizontalDrag =
            typedArray.getBoolean(R.styleable.SwipeLayout_isFreeHorizontalDrag, false)
        isContinuousSwipe = typedArray.getBoolean(R.styleable.SwipeLayout_isContinuousSwipe, false)
        isTogether = typedArray.getBoolean(R.styleable.SwipeLayout_isTogether, false)
        isEnabledSwipe = typedArray.getBoolean(R.styleable.SwipeLayout_isEnabledSwipe, true)
        staticLeftViewId = typedArray.getResourceId(R.styleable.SwipeLayout_leftItem, 0)
        staticRightViewId = typedArray.getResourceId(R.styleable.SwipeLayout_rightItem, 0)
        draggedViewId = typedArray.getResourceId(R.styleable.SwipeLayout_draggedItem, 0)
        autoOpenSpeed = typedArray.getInt(
            R.styleable.SwipeLayout_autoMovingSensitivity,
            DEFAULT_AUTO_OPEN_SPEED
        ).toDouble()
        rightDragViewPadding =
            typedArray.getDimension(R.styleable.SwipeLayout_rightDragViewPadding, 0f).toInt()
        leftDragViewPadding =
            typedArray.getDimension(R.styleable.SwipeLayout_leftDragViewPadding, 0f).toInt()
        parametersAdjustment()
        typedArray.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldW: Int, oldH: Int) {
        horizontalWidth = w
        super.onSizeChanged(w, h, oldW, oldH)
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        if (disallowIntercept && isViewGroup(draggedView)) {
            val neededScrollView: View? = getNeededTouchView(event, draggedView as ViewGroup?)
            val touchPoint = Point(event.x.toInt(), event.y.toInt())
            if (neededScrollView != null && isViewTouchTarget(neededScrollView, touchPoint)) {
                return false
            }
        }
        return isSwipeViewTarget(event) && dragHelper!!.shouldInterceptTouchEvent(event)
    }

    override fun requestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        super.requestDisallowInterceptTouchEvent(disallowIntercept)
        this.disallowIntercept = disallowIntercept
    }

    override fun onFinishInflate() {
        if (draggedViewId != 0) {
            draggedView = findViewById(draggedViewId)
        }
        if (staticLeftViewId != 0) {
            staticLeftView = findViewById(staticLeftViewId)
        }
        if (staticRightViewId != 0) {
            staticRightView = findViewById(staticRightViewId)
        }
        if (draggedView == null) {
            throw RuntimeException("'draggedItem' must be specified")
        } else if (isTogether && (currentDirection == LEFT) && (staticRightView == null)) {
            throw RuntimeException("If 'isTogether = true' 'rightItem' must be specified")
        } else if (isTogether && (currentDirection == RIGHT) && (staticLeftView == null)) {
            throw RuntimeException("If 'isTogether = true' 'leftItem' must be specified")
        } else if ((currentDirection == LEFT) && !isContinuousSwipe && (staticRightView == null)) {
            throw RuntimeException("Must be specified 'rightItem' or flag isContinuousSwipe = true")
        } else if ((currentDirection == RIGHT) && !isContinuousSwipe && (staticLeftView == null)) {
            throw RuntimeException("Must be specified 'leftItem' or flag isContinuousSwipe = true")
        } else if (currentDirection == HORIZONTAL && (staticRightView == null || staticLeftView == null)) {
            throw RuntimeException("'leftItem' and 'rightItem' must be specified")
        }
        dragHelper = ViewDragHelper.create(this, 1.0f, dragHelperCallback)
        gestureDetector = GestureDetectorCompat(context, gestureDetectorCallBack)
        setupPost()
        super.onFinishInflate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isSwipeViewTarget(event) || isMoving) {
            gestureDetector!!.onTouchEvent(event)
            dragHelper!!.processTouchEvent(event)
            return true
        } else {
            return super.onTouchEvent(event)
        }
    }

    override fun computeScroll() {
        if (dragHelper!!.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    /**
     * Performs manual swipe to the left
     *
     * @param animated - flag to animate opening
     */
    fun openRight(animated: Boolean) {
        if (animated) {
            openRight()
        } else if (isDragIdle(currentDraggingState) && (((currentDirection == LEFT && !isEmptyRightView)
                    || currentDirection == HORIZONTAL)) && !isRightOpen
        ) {
            if (isTogether) {
                staticRightView!!.offsetLeftAndRight(-1 * (if (isLeftOpen) rightViewWidth * 2 else rightViewWidth))
            }
            draggedView!!.offsetLeftAndRight(-1 * (if (isLeftOpen) rightViewWidth * 2 else rightViewWidth))
            draggingViewLeft -= (if (isLeftOpen) rightViewWidth * 2 else rightViewWidth)
            updateState()
        }
    }

    /**
     * Performs a full manual swipe to the left
     *
     * @param animated - flag to animate opening
     */
    fun openRightCompletely(animated: Boolean) {
        if (animated) {
            openRightCompletely()
        } else {
            if (isDragIdle(currentDraggingState) && currentDirection == LEFT) {
                if (isTogether) {
                    staticRightView!!.offsetLeftAndRight(-horizontalWidth)
                }
                draggedView!!.offsetLeftAndRight(-horizontalWidth)
                draggingViewLeft -= horizontalWidth
                updateState()
            }
        }
    }

    /**
     * Performs manual swipe to the right
     *
     * @param animated - flag to animate opening
     */
    fun openLeft(animated: Boolean) {
        if (animated) {
            openLeft()
        } else if (isDragIdle(currentDraggingState) && (((currentDirection == RIGHT && !isEmptyLeftView)
                    || currentDirection == HORIZONTAL)) && !isLeftOpen
        ) {
            if (isTogether) {
                staticLeftView!!.offsetLeftAndRight((if (isRightOpen) leftViewWidth * 2 else leftViewWidth))
            }
            draggedView!!.offsetLeftAndRight((if (isRightOpen) leftViewWidth * 2 else leftViewWidth))
            draggingViewLeft += (if (isRightOpen) leftViewWidth * 2 else leftViewWidth)
            updateState()
        }
    }

    /**
     * Performs a full manual swipe to the right
     *
     * @param animated - flag to animate opening
     */
    fun openLeftCompletely(animated: Boolean) {
        if (animated) {
            openRightCompletely()
        } else {
            if (isDragIdle(currentDraggingState) && currentDirection == RIGHT) {
                if (isTogether) {
                    staticRightView!!.offsetLeftAndRight(horizontalWidth)
                }
                draggedView!!.offsetLeftAndRight(horizontalWidth)
                draggingViewLeft += horizontalWidth
                updateState()
            }
        }
    }

    /**
     * Performs manual close
     *
     * @param animated - flag to animate closing
     */
    fun close(animated: Boolean) {
        if (animated) {
            close()
        } else {
            if (isTogether) {
                if (staticLeftView != null && currentDirection == RIGHT) {
                    staticLeftView!!.layout(
                        CLOSE_POSITION, staticLeftView!!.top,
                        staticLeftView!!.width, staticLeftView!!.bottom
                    )
                } else if (staticRightView != null && currentDirection == LEFT) {
                    staticRightView!!.layout(
                        horizontalWidth - staticRightView!!.width, staticRightView!!.top,
                        horizontalWidth, staticRightView!!.bottom
                    )
                } else if ((currentDirection == HORIZONTAL) && (staticRightView != null) && (staticLeftView != null)) {
                    staticLeftView!!.layout(
                        CLOSE_POSITION, staticLeftView!!.top,
                        staticLeftView!!.width, staticLeftView!!.bottom
                    )
                    staticRightView!!.layout(
                        horizontalWidth - staticRightView!!.width, staticRightView!!.top,
                        horizontalWidth, staticRightView!!.bottom
                    )
                }
            }
            draggedView!!.layout(
                CLOSE_POSITION,
                draggedView!!.top,
                draggedView!!.width,
                draggedView!!.bottom
            )
            draggingViewLeft = CLOSE_POSITION
            updateState()
        }
    }

    /**
     * Performs manual swipe to the right
     */
    fun openLeft() {
        if (isDragIdle(currentDraggingState) && (((currentDirection == RIGHT && !isEmptyLeftView)
                    || currentDirection == HORIZONTAL))
        ) {
            moveTo(leftViewWidth)
        }
    }

    /**
     * Performs manual swipe to the left
     */
    fun openRight() {
        if (isDragIdle(currentDraggingState) && (((currentDirection == LEFT && !isEmptyRightView)
                    || currentDirection == HORIZONTAL))
        ) {
            moveTo(-rightViewWidth)
        }
    }

    /**
     * Performs a full manual swipe to the right
     */
    fun openLeftCompletely() {
        if (isDragIdle(currentDraggingState) && currentDirection == RIGHT) {
            moveTo(horizontalWidth)
        }
    }

    /**
     * Performs a full manual swipe to the left
     */
    fun openRightCompletely() {
        if (isDragIdle(currentDraggingState) && currentDirection == LEFT) {
            moveTo(-horizontalWidth)
        }
    }

    /**
     * Performs manual close
     */
    fun close() {
        moveTo(CLOSE_POSITION)
    }

    /**
     * Is moving main view
     */
    val isMoving: Boolean
        get() = (currentDraggingState == ViewDragHelper.STATE_DRAGGING ||
                currentDraggingState == ViewDragHelper.STATE_SETTLING)

    /**
     * Is closed main view
     */
    val isClosed: Boolean
        get() {
            return draggingViewLeft == CLOSE_POSITION
        }

    /**
     * Set current direction of a swipe
     */
    fun setCurrentDirection(currentDirection: Int): SwipeLayout {
        this.currentDirection = currentDirection
        return this
    }

    /**
     * The secondary view will move along with the main view
     */
    fun setTogether(together: Boolean): SwipeLayout {
        isTogether = together
        return this
    }

    /**
     * Swipe to the end of the screen.
     * Can work without a secondary view [.staticLeftView] and [.staticRightView]
     *
     *
     * If a particular direction of the swipe is used ([.LEFT] or [.RIGHT]),
     * and this flag is set, then [.isFreeDragAfterOpen] always will be true.
     *
     *
     * If the left and right directions of the swipe are used simultaneously ([.HORIZONTAL]),
     * then this flag will be ignored
     */
    fun setContinuousSwipe(continuousSwipe: Boolean): SwipeLayout {
        isContinuousSwipe = continuousSwipe
        parametersAdjustment()
        return this
    }

    /**
     * Moving the main view after it was open.
     *
     *
     * if [.isEmptyLeftView] or [.isEmptyRightView],
     * then this flag will be ignored
     */
    fun setFreeDragAfterOpen(freeDragAfterOpen: Boolean): SwipeLayout {
        isFreeDragAfterOpen = freeDragAfterOpen
        parametersAdjustment()
        return this
    }

    /**
     * If a particular direction of the swipe is used ([.LEFT] or [.RIGHT]),
     * then this flag allows you to do the swipe in the opposite direction.
     *
     *
     * If the horizontal direction is used ([.HORIZONTAL]),
     * this flag allows you to move the main view continuously in both directions
     */
    fun setFreeHorizontalDrag(freeHorizontalDrag: Boolean): SwipeLayout {
        isFreeHorizontalDrag = freeHorizontalDrag
        return this
    }

    /**
     * Set swipe actions listener
     */
    fun setOnActionsListener(actionsListener: SwipeActionsListener?): SwipeLayout {
        this.actionsListener = actionsListener
        return this
    }

    /**
     * Set the right bounding border of the swipe for the main view
     */
    fun setRightDragViewPadding(minRightDragViewPadding: Int): SwipeLayout {
        rightDragViewPadding = minRightDragViewPadding
        parametersAdjustment()
        return this
    }

    /**
     * Set the left bounding border of the swipe for the main view
     */
    fun setLeftDragViewPadding(minLeftDragViewPadding: Int): SwipeLayout {
        leftDragViewPadding = minLeftDragViewPadding
        parametersAdjustment()
        return this
    }

    /**
     * Enable touch for ViewGroup
     */
    fun enableTouchForViewGroup(viewGroup: ViewGroup) {
        viewGroup.setOnTouchListener(object : OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                requestDisallowInterceptTouchEvent(true)
                return false
            }
        })
    }

    private fun updateState() {
        if (isClosed) {
            isLeftOpen = false
            isRightOpen = false
            if (actionsListener != null) {
                actionsListener!!.onClose()
            }
        } else if (isLeftOpenCompletely || isLeftViewOpen) {
            isLeftOpen = true
            isRightOpen = false
            if (actionsListener != null) {
                actionsListener!!.onOpen(
                    RIGHT,
                    isLeftOpenCompletely
                )
            }
        } else if (isRightOpenCompletely || isRightViewOpen) {
            isLeftOpen = false
            isRightOpen = true
            if (actionsListener != null) {
                actionsListener!!.onOpen(
                    LEFT,
                    isRightOpenCompletely
                )
            }
        }
    }

    private val gestureDetectorCallBack: GestureDetector.OnGestureListener =
        object : SimpleOnGestureListener() {
            override fun onScroll(
                e1: MotionEvent,
                e2: MotionEvent,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(true)
                }
                return false
            }
        }
    private val dragHelperCallback: ViewDragHelper.Callback = object : ViewDragHelper.Callback() {
        override fun onViewDragStateChanged(state: Int) {
            if (state == currentDraggingState) return
            if (isIdleAfterMoving(state)) {
                updateState()
            }
            currentDraggingState = state
        }

        override fun onViewPositionChanged(
            changedView: View,
            left: Int,
            top: Int,
            dx: Int,
            dy: Int
        ) {
            draggingViewLeft = left
            if (isTogether) {
                if (currentDirection == LEFT) {
                    staticRightView!!.offsetLeftAndRight(dx)
                } else if (currentDirection == RIGHT) {
                    staticLeftView!!.offsetLeftAndRight(dx)
                } else if (currentDirection == HORIZONTAL) {
                    staticLeftView!!.offsetLeftAndRight(dx)
                    staticRightView!!.offsetLeftAndRight(dx)
                }
            }
        }

        override fun getViewHorizontalDragRange(child: View): Int {
            return horizontalWidth
        }

        override fun tryCaptureView(view: View, pointerId: Int): Boolean {
            return view.id == draggedView!!.id
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            if (!isEnabledSwipe) {
                return CLOSE_POSITION
            }
            when (currentDirection) {
                LEFT -> return clampLeftViewPosition(left)
                RIGHT -> return clampRightViewPosition(left)
                HORIZONTAL -> return clampHorizontalViewPosition(left, dx)
                else -> return CLOSE_POSITION
            }
        }

        override fun onViewReleased(releasedChild: View, xVel: Float, yVel: Float) {
            var finalXDraggingView: Int = CLOSE_POSITION
            if (currentDirection == LEFT) {
                finalXDraggingView = getFinalXLeftDirection(xVel)
            } else if (currentDirection == RIGHT) {
                finalXDraggingView = getFinalXRightDirection(xVel)
            } else if (currentDirection == HORIZONTAL) {
                finalXDraggingView = getFinalXHorizontalDirection(xVel)
                if (finalXDraggingView == NO_POSITION) {
                    finalXDraggingView = previousPosition
                }
            }
            if (dragHelper!!.settleCapturedViewAt(finalXDraggingView, draggedView!!.top)) {
                ViewCompat.postInvalidateOnAnimation(this@SwipeLayout)
            }
        }
    }

    private fun clampLeftViewPosition(left: Int): Int {
        if (isContinuousSwipe && isEmptyRightView) {
            if (isFreeHorizontalDrag) {
                return if (left > horizontalWidth) CLOSE_POSITION else Math.max(
                    left,
                    -horizontalWidth
                )
            } else {
                return if (left > CLOSE_POSITION) CLOSE_POSITION else Math.max(
                    left,
                    -horizontalWidth
                )
            }
        }
        if (isFreeDragAfterOpen) {
            if (isFreeHorizontalDrag) {
                return if (left > horizontalWidth) CLOSE_POSITION else Math.max(
                    left,
                    leftDragViewPadding - horizontalWidth
                )
            }
            return if (left > CLOSE_POSITION) CLOSE_POSITION else Math.max(
                left,
                leftDragViewPadding - horizontalWidth
            )
        }
        if (isFreeHorizontalDrag) {
            return if (left > horizontalWidth) CLOSE_POSITION else Math.max(left, -rightViewWidth)
        }
        return if (left > CLOSE_POSITION) CLOSE_POSITION else Math.max(left, -rightViewWidth)
    }

    private fun clampRightViewPosition(left: Int): Int {
        if (isContinuousSwipe && isEmptyLeftView) {
            if (isFreeHorizontalDrag) {
                return if (left < -horizontalWidth) -horizontalWidth else Math.min(
                    left,
                    horizontalWidth
                )
            } else {
                return if (left < CLOSE_POSITION) CLOSE_POSITION else Math.min(
                    left,
                    horizontalWidth
                )
            }
        }
        if (isFreeDragAfterOpen) {
            if (isFreeHorizontalDrag) {
                return if (left < -horizontalWidth) -horizontalWidth else Math.min(
                    left,
                    horizontalWidth - rightDragViewPadding
                )
            }
            return if (left < CLOSE_POSITION) CLOSE_POSITION else Math.min(
                left,
                horizontalWidth - rightDragViewPadding
            )
        }
        if (isFreeHorizontalDrag) {
            return if (left < -horizontalWidth) -horizontalWidth else Math.min(
                left,
                leftViewWidth
            )
        }
        return if (left < CLOSE_POSITION) CLOSE_POSITION else Math.min(
            left,
            leftViewWidth
        )
    }

    private fun clampHorizontalViewPosition(left: Int, dx: Int): Int {
        if (!isFreeHorizontalDrag && isLeftOpen && (dx < 0)) {
            return Math.max(left, CLOSE_POSITION)
        }
        if (!isFreeHorizontalDrag && isRightOpen && (dx > 0)) {
            return Math.min(left, CLOSE_POSITION)
        }
        if (!isFreeDragAfterOpen && left > CLOSE_POSITION) {
            return Math.min(left, leftViewWidth)
        }
        if (!isFreeDragAfterOpen && left < CLOSE_POSITION) {
            return Math.max(left, -rightViewWidth)
        }
        return if (left < CLOSE_POSITION) Math.max(
            left,
            leftDragViewPadding - horizontalWidth
        ) else Math.min(left, horizontalWidth - rightDragViewPadding)
    }

    private val previousPosition: Int
        private get() {
            if (isLeftOpen) {
                return leftViewWidth
            } else if (isRightOpen) {
                return -rightViewWidth
            } else {
                return CLOSE_POSITION
            }
        }

    private fun getFinalXLeftDirection(xVel: Float): Int {
        if (isContinuousSwipe) {
            if (isEmptyRightView) {
                if (((draggingViewLeft < CLOSE_POSITION && Math.abs(draggingViewLeft) > horizontalWidth / 2)
                            || xVel < -autoOpenSpeed)
                ) {
                    return -horizontalWidth
                }
                return CLOSE_POSITION
            }
            if (isContinuousSwipeToLeft(xVel)) {
                return -horizontalWidth
            }
        }
        val settleToOpen: Boolean
        if (xVel > autoOpenSpeed) {
            settleToOpen = false
        } else if (xVel < -autoOpenSpeed) {
            settleToOpen = true
        } else if (draggingViewLeft < CLOSE_POSITION && Math.abs(draggingViewLeft) > rightViewWidth / 2) {
            settleToOpen = true
        } else if (draggingViewLeft < CLOSE_POSITION && Math.abs(draggingViewLeft) < rightViewWidth / 2) {
            settleToOpen = false
        } else {
            settleToOpen = false
        }
        return if (settleToOpen) -rightViewWidth else CLOSE_POSITION
    }

    private fun getFinalXRightDirection(xVel: Float): Int {
        if (isContinuousSwipe) {
            if (isEmptyLeftView) {
                if (((draggingViewLeft > CLOSE_POSITION && Math.abs(draggingViewLeft) > horizontalWidth / 2)
                            || xVel > autoOpenSpeed)
                ) {
                    return horizontalWidth
                }
                return CLOSE_POSITION
            }
            if (isContinuousSwipeToRight(xVel)) {
                return horizontalWidth
            }
        }
        val settleToOpen: Boolean
        if (xVel > autoOpenSpeed) {
            settleToOpen = true
        } else if (xVel < -autoOpenSpeed) {
            settleToOpen = false
        } else if (draggingViewLeft > CLOSE_POSITION && Math.abs(draggingViewLeft) > leftViewWidth / 2) {
            settleToOpen = true
        } else if (draggingViewLeft > CLOSE_POSITION && Math.abs(draggingViewLeft) < leftViewWidth / 2) {
            settleToOpen = false
        } else {
            settleToOpen = false
        }
        return if (settleToOpen) leftViewWidth else CLOSE_POSITION
    }

    private fun getFinalXHorizontalDirection(xVel: Float): Int {
        if (isSwipeToOpenLeft(xVel)) {
            return leftViewWidth
        } else if (isSwipeToOpenRight(xVel)) {
            return -rightViewWidth
        } else if (isSwipeToClose(xVel)) {
            return CLOSE_POSITION
        }
        return NO_POSITION
    }

    private fun isContinuousSwipeToRight(xVel: Float): Boolean {
        return ((xVel > autoOpenSpeed && Math.abs(draggingViewLeft) > leftViewWidth)
                || (draggingViewLeft > CLOSE_POSITION && Math.abs(draggingViewLeft) > horizontalWidth / 2))
    }

    private fun isContinuousSwipeToLeft(xVel: Float): Boolean {
        return ((xVel < -autoOpenSpeed && Math.abs(draggingViewLeft) > rightViewWidth)
                || (draggingViewLeft < CLOSE_POSITION && Math.abs(draggingViewLeft) > horizontalWidth / 2))
    }

    private fun isSwipeToOpenRight(xVel: Float): Boolean {
        if (xVel > 0) {
            return false
        }
        return ((draggingViewLeft < CLOSE_POSITION && xVel < -autoOpenSpeed)
                || (draggingViewLeft < CLOSE_POSITION && Math.abs(draggingViewLeft) > rightViewWidth / 2))
    }

    private fun isSwipeToOpenLeft(xVel: Float): Boolean {
        if (xVel < 0) {
            return false
        }
        return ((draggingViewLeft > CLOSE_POSITION && xVel > autoOpenSpeed)
                || (draggingViewLeft > CLOSE_POSITION && Math.abs(draggingViewLeft) > leftViewWidth / 2))
    }

    private fun isSwipeToClose(xVel: Float): Boolean {
        return ((draggingViewLeft >= CLOSE_POSITION && xVel < -autoOpenSpeed)
                || (draggingViewLeft <= CLOSE_POSITION && xVel > autoOpenSpeed)
                || (draggingViewLeft >= CLOSE_POSITION && Math.abs(draggingViewLeft) < leftViewWidth / 2)
                || (draggingViewLeft <= CLOSE_POSITION && Math.abs(draggingViewLeft) < rightViewWidth / 2))
    }

    private fun setupPost() {
        if (isTogether) {
            post(object : Runnable {
                override fun run() {
                    if (currentDirection == LEFT) {
                        staticRightView!!.x = horizontalWidth.toFloat()
                    } else if (currentDirection == RIGHT) {
                        staticLeftView!!.x = -staticLeftView!!.width.toFloat()
                    } else if (currentDirection == HORIZONTAL) {
                        staticRightView!!.x = horizontalWidth.toFloat()
                        staticLeftView!!.x = -staticLeftView!!.width.toFloat()
                    }
                }
            })
        }
    }

    private fun isViewTouchTarget(view: View, point: Point): Boolean {
        return (point.y >= view.top
                ) && (point.y < view.bottom
                ) && (point.x >= view.left
                ) && (point.y < view.right)
    }

    private fun getNeededTouchView(event: MotionEvent, rootView: ViewGroup?): View? {
        if (rootView!!.onInterceptTouchEvent(event)) {
            return rootView
        }
        val count: Int = rootView.childCount
        for (i in 0 until count) {
            val view: View = rootView.getChildAt(i)
            if (!isViewGroup(view)) {
                continue
            }
            val neededScrollView: View? = getNeededTouchView(event, view as ViewGroup)
            if (neededScrollView != null) {
                return neededScrollView
            }
        }
        return null
    }

    private fun isViewGroup(view: View?): Boolean {
        return view is ViewGroup
    }

    private fun isSwipeViewTarget(event: MotionEvent): Boolean {
        val swipeViewLocation: IntArray = IntArray(2)
        draggedView!!.getLocationOnScreen(swipeViewLocation)
        val upperLimit: Int = swipeViewLocation.get(1) + draggedView!!.measuredHeight
        val lowerLimit: Int = swipeViewLocation.get(1)
        val y: Int = event.rawY.toInt()
        return (y > lowerLimit && y < upperLimit)
    }

    private fun isIdleAfterMoving(state: Int): Boolean {
        return ((currentDraggingState == ViewDragHelper.STATE_DRAGGING
                || currentDraggingState == ViewDragHelper.STATE_SETTLING)
                && state == ViewDragHelper.STATE_IDLE)
    }

    private fun isDragIdle(state: Int): Boolean {
        return state == ViewDragHelper.STATE_IDLE
    }

    private val isRightViewOpen: Boolean
        private get() {
            return staticRightView != null && draggingViewLeft == -rightViewWidth
        }
    private val isLeftViewOpen: Boolean
        private get() {
            return staticLeftView != null && draggingViewLeft == leftViewWidth
        }
    private val isRightOpenCompletely: Boolean
        private get() {
            return draggingViewLeft == -horizontalWidth
        }
    private val isLeftOpenCompletely: Boolean
        private get() {
            return draggingViewLeft == horizontalWidth
        }
    private val leftViewWidth: Int
        private get() {
            return staticLeftView!!.width
        }
    private val rightViewWidth: Int
        private get() {
            return staticRightView!!.width
        }
    private val isEmptyLeftView: Boolean
        private get() {
            return staticLeftView == null
        }
    private val isEmptyRightView: Boolean
        private get() {
            return staticRightView == null
        }

    private fun parametersAdjustment() {
        if (isContinuousSwipe && currentDirection != HORIZONTAL) {
            isFreeDragAfterOpen = true
        }
        if (currentDirection == HORIZONTAL) {
            rightDragViewPadding = 0
            leftDragViewPadding = 0
        }
    }

    private fun moveTo(x: Int) {
        dragHelper!!.smoothSlideViewTo((draggedView)!!, x, draggedView!!.top)
        ViewCompat.postInvalidateOnAnimation(this)
    }

    interface SwipeActionsListener {
        fun onOpen(direction: Int, isContinuous: Boolean)
        fun onClose()
    }

    companion object {
        const val LEFT: Int = 1
        const val RIGHT: Int = LEFT shl 1
        const val HORIZONTAL: Int = LEFT or RIGHT
        private const val CLOSE_POSITION: Int = 0
        private const val NO_POSITION: Int = -1
        private const val DEFAULT_AUTO_OPEN_SPEED: Int = 1000
    }


}