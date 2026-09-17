package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BasilGreenContainer
import com.example.ui.theme.BasilGreenVeg
import com.example.ui.theme.CrimsonNonVeg
import com.example.ui.theme.CrimsonNonVegContainer

@Composable
fun VegBadge(
    isVeg: Boolean,
    modifier: Modifier = Modifier,
    showLabel: Boolean = true
) {
    val borderColor = if (isVeg) BasilGreenVeg else CrimsonNonVeg
    val dotColor = if (isVeg) BasilGreenVeg else CrimsonNonVeg
    val backgroundColor = if (isVeg) BasilGreenContainer else CrimsonNonVegContainer
    val labelText = if (isVeg) "VEG" else "NON-VEG"

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(backgroundColor)
            .padding(horizontal = 6.dp, vertical = 3.dp)
    ) {
        // Standard food icon symbol: square with inner circle
        Box(
            modifier = Modifier
                .size(14.dp)
                .border(1.5.dp, borderColor, RoundedCornerShape(2.dp)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(dotColor)
            )
        }

        if (showLabel) {
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = labelText,
                color = borderColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }
    }
}
