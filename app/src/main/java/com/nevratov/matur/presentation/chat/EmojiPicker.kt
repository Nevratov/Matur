package com.nevratov.matur.presentation.chat

import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.emoji2.emojipicker.EmojiPickerView
import androidx.emoji2.emojipicker.EmojiViewItem
import com.nevratov.matur.R


@Composable
fun EmojiPicker(
    onEmojiClicked: (String) -> Unit
) {

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            val view =
                LayoutInflater.from(context)
                    .inflate(R.layout.emoji_picker_layout, /* root= */ null, /* attachToRoot= */ false)
            val emojiPickerView = view.findViewById<EmojiPickerView>(R.id.emoji_picker)
            emojiPickerView.setOnEmojiPickedListener { onEmojiClicked(it.emoji) }
            view
        },
    )
}

