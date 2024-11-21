package com.hope.socialmediaemotiondetection.view.components

import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SingleChoiceSegmentedButton(
    checkedItem: Int,
    onCheckedChange: (Int) -> Unit,
    modifier: Modifier = Modifier
){
    var checkedItem by remember { mutableIntStateOf(0) }
    val options = listOf("Paylaşımlar","Yorumlar","Beğeniler")
    SingleChoiceSegmentedButtonRow {
        options.forEachIndexed { index , option ->
            SegmentedButton(
                onClick = {
                    onCheckedChange(index) // Segment değişimini bildir
                },
                selected = checkedItem == index,
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size
                ),
                modifier = Modifier
            ) {
                Text(option)
            }
        }
    }
}


@Composable
@Preview
fun ButtonPreview(){
    SingleChoiceSegmentedButton()
}