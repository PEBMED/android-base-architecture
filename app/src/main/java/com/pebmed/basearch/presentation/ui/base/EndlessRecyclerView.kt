package com.pebmed.basearch.presentation.ui.base

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pebmed.basearch.R

/**
 * @Descrição: EndlessRecyclerView é uma recycler view customizada que avisa quando a parte visível
 * da lista está chegando ao fim.
 */
class EndlessRecyclerView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {
    companion object {
        private const val INSTANCE_STATE_KEY = "instanceState"
        private const val INSTANCE_STATE_NEXT_PAGE = "instanceStateNextPage"
        private const val INSTANCE_STATE_IS_PAGING = "instanceStateIsPaging"
        private const val INSTANCE_STATE_HAS_NEXT_PAGE = "instanceStateHasNextPage"
    }

    private val alpha by lazy { PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f) }
    private val propertyY by lazy { PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 100f, 0f) }
    private val animatorSet by lazy { AnimatorSet() }

    private var callback: Callback? = null
    private var hasNextPage = false
    private var nextPage = 0
    private var isPaging: Boolean = false
    private var animate = false
    private val scrollListener = recyclerScrollListener()

    init {
        val typedArray =
                context.obtainStyledAttributes(
                        attrs,
                        R.styleable.EndlessRecyclerView,
                        0,
                        defStyleAttr
                )
        animate =
                typedArray.getBoolean(R.styleable.EndlessRecyclerView_erv_animate, false)
        typedArray.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (animate) {
            setAlpha(0f)
            startAnimation()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        addOnScrollListener(scrollListener)
    }

    override fun onDetachedFromWindow() {
        removeOnScrollListener(scrollListener)
        super.onDetachedFromWindow()
    }

    override fun onSaveInstanceState() = Bundle().apply {
        putParcelable(INSTANCE_STATE_KEY, super.onSaveInstanceState())
        putInt(INSTANCE_STATE_NEXT_PAGE, nextPage)
        putBoolean(INSTANCE_STATE_HAS_NEXT_PAGE, hasNextPage)
        putBoolean(INSTANCE_STATE_IS_PAGING, isPaging)
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val bundle = (state as? Bundle)?.apply {
            nextPage = getInt(INSTANCE_STATE_NEXT_PAGE)
            hasNextPage = getBoolean(INSTANCE_STATE_HAS_NEXT_PAGE)
            isPaging = getBoolean(INSTANCE_STATE_IS_PAGING)
        }
        super.onRestoreInstanceState(bundle?.getParcelable(INSTANCE_STATE_KEY))
    }

    fun startPaging() {
        isPaging = true
    }

    fun stopPaging() {
        isPaging = false
    }

    fun nextPage() = nextPage

    fun hasNextPage() = hasNextPage

    fun isPaging() = isPaging

    fun callback(callback: Callback?) = apply {
        this.callback = callback
    }

    fun nextPage(nextPage: Int?) = apply {
        this.nextPage = nextPage ?: 1
    }

    fun hasNextPage(hasNextPage: Boolean?) = apply {
        this.hasNextPage = hasNextPage ?: false
    }

    interface Callback {
        fun loadMore(nextPage: Int)

        /**
        Cria corpo vazio para método OnScrolled de forma que o client não tenha obrigação de
        implementar.
         */
        fun onScrolled(
                recyclerView: RecyclerView?,
                scrollX: Int,
                scrollY: Int
        ){}
    }

    private fun startAnimation() {
        animatorSet.apply {
            playTogether(
                    ObjectAnimator.ofPropertyValuesHolder(
                            this@EndlessRecyclerView,
                            propertyY,
                            alpha
                    )
            )
            duration = 250
            startDelay = 100
        }.start()
    }

    private fun recyclerScrollListener() = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            when (layoutManager) {
                is LinearLayoutManager -> layoutManager as? LinearLayoutManager
                is GridLayoutManager -> layoutManager as? GridLayoutManager
                else -> null
            }?.let {
                val totalItemCount = it.itemCount
                val lastVisibleItem =
                        if (it is GridLayoutManager) it.findLastCompletelyVisibleItemPosition()
                        else it.findLastVisibleItemPosition()

                if (lastVisibleItem >= totalItemCount - 2
                        && hasNextPage()
                        && !isPaging()
                ) {
                    startPaging()
                    callback?.loadMore(nextPage)
                }
            }

            callback?.onScrolled(recyclerView, dx, dy)
        }
    }
}