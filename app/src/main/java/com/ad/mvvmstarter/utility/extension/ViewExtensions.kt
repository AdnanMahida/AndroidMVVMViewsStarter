package com.ad.mvvmstarter.utility.extension

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.SystemClock
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton

fun View.setVisible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

fun View.clickWithDebounce(
    debounceTime: Long = 700L,
    action: () -> Unit
) {
    this.setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0
        override fun onClick(v: View) {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
            else action()
            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}

fun View.setMargin(
    left: Int? = null,
    top: Int? = null,
    right: Int? = null,
    bottom: Int? = null
) {
    val params = (layoutParams as? ViewGroup.MarginLayoutParams)
    params?.setMargins(
        left ?: params.leftMargin,
        top ?: params.topMargin,
        right ?: params.rightMargin,
        bottom ?: params.bottomMargin
    )
    layoutParams = params
}

fun View.setMarginRes(
    @DimenRes
    left: Int? = null,
    @DimenRes
    top: Int? = null,
    @DimenRes
    right: Int? = null,
    @DimenRes
    bottom: Int? = null
) {
    setMargin(
        if (left == null) null else resources.getDimensionPixelSize(left),
        if (top == null) null else resources.getDimensionPixelSize(top),
        if (right == null) null else resources.getDimensionPixelSize(right),
        if (bottom == null) null else resources.getDimensionPixelSize(bottom),
    )
}

fun MaterialButton.setBgColor(
    @ColorRes
    color: Int
) {
    backgroundTintList = ColorStateList.valueOf(
        ContextCompat.getColor(
            this.context,
            color
        )
    )
}

fun CardView.setBgColor(
    @ColorRes
    color: Int
) {
    backgroundTintList = ColorStateList.valueOf(
        ContextCompat.getColor(
            this.context,
            color
        )
    )
}

fun Spinner.selected(action: (position: Int) -> Unit) {
    this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {}
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            action(position)
        }
    }
}

fun AppCompatTextView.setBGDrawable(@DrawableRes drawableInt: Int) {
    setBackgroundDrawable(ContextCompat.getDrawable(this.context, drawableInt))
}

fun AppCompatTextView.setBGColorTint(@ColorRes color: Int) {
    backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this.context, color))
}

fun AppCompatTextView.setLabelColor(@ColorRes color: Int) {
    setTextColor(ContextCompat.getColor(this.context, color))
}

fun AppCompatTextView.setDrawableColorTint(@ColorRes color: Int) {
    val colorFilter =
        PorterDuffColorFilter(ContextCompat.getColor(this.context, color), PorterDuff.Mode.SRC_IN)
    this.compoundDrawablesRelative.forEach { drawable ->
        if (drawable != null) {
            drawable.colorFilter = colorFilter
        }
    }
}

fun AppCompatTextView.applyColors(isSelected: Boolean, selectedColor: Int, unselectedColor: Int) {
    val color = if (isSelected) selectedColor else unselectedColor
    setBGColorTint(color)
    setDrawableColorTint(color)
    setLabelColor(color)
}


fun TextView.setSpannableTextView(
    fullText: String,
    textToSpan1: String,
    textToSpan2: String? = null,
    spanTextColor1: Int,
    spanTextColor2: Int = android.R.color.black,
    isUnderlineText: Boolean = false,
    onSpan1Click: (() -> Unit?)? = null,
    onSpan2Click: (() -> Unit?)? = null
) {
    val spannableBuilder = SpannableStringBuilder(fullText)
    val start1Index = fullText.indexOf(textToSpan1)
    if (start1Index != -1) {
        val end1Index = start1Index + textToSpan1.length

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                onSpan1Click?.invoke()
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = isUnderlineText
            }
        }

        spannableBuilder.setSpan(
            clickableSpan, start1Index, end1Index, SpannableStringBuilder.SPAN_INCLUSIVE_INCLUSIVE
        )

        spannableBuilder.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(context, spanTextColor1)),
            start1Index,
            end1Index,
            SpannableStringBuilder.SPAN_INCLUSIVE_INCLUSIVE
        )

        val start2Index = fullText.indexOf(textToSpan2 ?: "")
        if (start2Index != -1 && textToSpan2 != null) {
            val end2Index = start2Index + textToSpan2.length

            spannableBuilder.setSpan(
                object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        onSpan2Click?.invoke()
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        ds.isUnderlineText = isUnderlineText
                    }
                }, start2Index, end2Index, SpannableStringBuilder.SPAN_INCLUSIVE_INCLUSIVE
            )

            spannableBuilder.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(context, spanTextColor2)),
                start2Index,
                end2Index,
                SpannableStringBuilder.SPAN_INCLUSIVE_INCLUSIVE
            )
        }

        movementMethod = LinkMovementMethod.getInstance()
        highlightColor = ContextCompat.getColor(context, android.R.color.transparent)
        text = spannableBuilder
    }
}

fun TextView.getTrimmedText(): String {
    return this.text.toString().trim()
}

fun TextView.isEmptyText(): Boolean {
    return TextUtils.isEmpty(this.getTrimmedText())
}

fun EditText.showKeyboard() {
    val inputManager =
        this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}


interface TextChanges {
    fun onTextChange(text: String, view: View?)
}


fun EditText.afterTextChangeEvent(textChanges: TextChanges, view: View? = null) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            textChanges.onTextChange(p0.toString().trim(), view)
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }
    })
}


fun AppCompatEditText.compoundDrawable(
    @DrawableRes left: Int = 0,
    @DrawableRes right: Int = 0,
    @DrawableRes top: Int = 0,
    @DrawableRes bottom: Int = 0
) {
    this.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom)
}
