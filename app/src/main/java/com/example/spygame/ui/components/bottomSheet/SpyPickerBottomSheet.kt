package com.example.spygame.ui.components.bottomSheet

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.spygame.R
import com.example.spygame.ui.components.picker.Picker
import com.example.spygame.ui.components.picker.rememberPickerState
import com.example.spygame.ui.theme.lightGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpyPickerBottomSheet(
    onDismissRequest: () -> Unit,
    onCanceled: () -> Unit = {},
    onConfirmed: (Int) -> Unit,
    sheetState: SheetState,
    startRange: Int,
    endRange: Int,
    startIndex: Int,
    topText: String,
    topTextStyle: TextStyle = LocalTextStyle.current,
    pickerNumberStyle: TextStyle = LocalTextStyle.current
) {

    val values = remember { (startRange..endRange).map { it.toString() } }
    val valuesPickerState = rememberPickerState(min = startRange)


    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        containerColor = MaterialTheme.colorScheme.onSecondary,
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        sheetState = sheetState,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = topText,
                modifier = Modifier.padding(top = 16.dp),
                style = topTextStyle,
                color = MaterialTheme.colorScheme.primary
            )

            Row(modifier = Modifier.fillMaxWidth()) {
                Picker(
                    state = valuesPickerState,
                    items = values,
                    visibleItemsCount = 5,
                    startIndex = startIndex - startRange,
                    modifier = Modifier.weight(0.3f),
                    textModifier = Modifier.padding(8.dp),
                    textStyle = pickerNumberStyle,
                    dividerColor = MaterialTheme.colorScheme.secondary,
                )
            }

            val interactionSource = remember { MutableInteractionSource() }
            val isPressed by interactionSource.collectIsPressedAsState()
            val backgroundColor by animateColorAsState(
                targetValue = if (isPressed) lightGreen else MaterialTheme.colorScheme.secondary,
                animationSpec = tween(durationMillis = 250)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        onConfirmed(valuesPickerState.selectedItem.toInt())
                        onDismissRequest()
                    },
                    shape = RoundedCornerShape(8.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 4.dp
                    ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = backgroundColor,
                        contentColor = MaterialTheme.colorScheme.onSecondary,
                    ),
                    interactionSource = interactionSource,
                ) {
                    Text(text = stringResource(R.string.confirm))
                }
                Spacer(Modifier.width(8.dp))
                Button(
                    onClick = {
                        onCanceled()
                        onDismissRequest()
                    },
                    shape = RoundedCornerShape(8.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                    border = BorderStroke(
                        color = MaterialTheme.colorScheme.secondary,
                        width = 1.dp
                    ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onSecondary,
                        contentColor = MaterialTheme.colorScheme.secondary,
                    ),
                ) {
                    Text(text = stringResource(R.string.cancel))
                }
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}