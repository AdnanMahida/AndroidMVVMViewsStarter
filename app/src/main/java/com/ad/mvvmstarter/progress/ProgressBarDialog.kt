package com.ad.mvvmstarter.progress

import android.app.Dialog
import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.core.content.res.ResourcesCompat
import com.ad.mvvmstarter.R
import com.ad.mvvmstarter.databinding.DialogProgressBinding

class ProgressBarDialog {
    private var dialog: Dialog? = null
    private lateinit var binding: DialogProgressBinding

    fun show(context: Context): Dialog {
        return show(
            context,
            null
        )
    }

    fun show(
        context: Context,
        title: CharSequence?
    ): Dialog {
        if (isShowing()) {
            dismiss()
        }
        binding = DialogProgressBinding.inflate(
            LayoutInflater.from(context),
            null,
            true
        )
        if (title != null) {
            binding.cpTitle.text = title
        }

        setColorFilter(
            binding.progressBar.indeterminateDrawable,
            ResourcesCompat.getColor(
                context.resources,
                R.color.purple_500,
                null
            )
        ) //Progress Bar Color
        dialog = Dialog(
            context,
            R.style.CustomProgressBarTheme
        )
        dialog?.setContentView(binding.root)
        //Set the dialog to not focusable (makes navigation ignore us adding the window)
        dialog?.window?.apply {
            setFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            )
            addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            setDimAmount(0.5f)
        }
        dialog?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        dialog?.show()
        return dialog!!
    }

    fun isShowing() = dialog?.isShowing ?: false
    fun dismiss() {
        dialog?.dismiss()
    }

    @SuppressWarnings("deprecation")
    private fun setColorFilter(
        drawable: Drawable,
        color: Int
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            drawable.colorFilter = BlendModeColorFilter(
                color,
                BlendMode.SRC_ATOP
            )
        } else {
            drawable.setColorFilter(
                color,
                PorterDuff.Mode.SRC_ATOP
            )
        }
    }

    fun setProgress(progress: Int) {
        if (this::binding.isInitialized) {
            binding.cpTitle.text = "Uploading $progress%"
        }
    }
}