package com.ad.mvvmstarter.utility.extension

import android.content.Context
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat

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
