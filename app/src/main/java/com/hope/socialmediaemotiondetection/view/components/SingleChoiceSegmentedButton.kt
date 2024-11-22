package com.hope.socialmediaemotiondetection.view.components

import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SingleChoiceSegmentedButton(onSelectionChanged: (Int) -> Unit) {
    var checkedItem by remember { mutableIntStateOf(0) }
    val options = listOf("Paylaşımlar", "Yorumlar","Durum")
    SingleChoiceSegmentedButtonRow {
        options.forEachIndexed { index, option ->
            SegmentedButton(
                onClick = {
                    checkedItem = index
                    onSelectionChanged(index)  // Seçim değiştiğinde dışarıya ilet
                },
                selected = checkedItem == index,
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size
                )
            ) {
                Text(option)
            }
        }
    }
}


