package com.p4r4d0x.hollowminds.presenter.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HollowButton(
    textResource: Int,
    action: () -> Unit
) {
    Button(
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onSecondary
        ),
        modifier = Modifier
            .width(200.dp)
            .height(45.dp),
        onClick = { action.invoke() }
    ) {
        Text(
            text = stringResource(id = textResource),
            fontWeight = FontWeight.Light,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSecondary
        )
    }
}

@Composable
fun HollowText(
    modifier: Modifier = Modifier,
    textResource: Int,
    auxStringValue: Int? = null,
    auxStringValue2: Int? = null

) {
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colors.surface)
            .padding(20.dp)
    ) {
        Text(
            text = auxStringValue?.let { fValue ->
                auxStringValue2?.let { sValue ->
                    stringResource(id = textResource, fValue, sValue)
                } ?: run {
                    stringResource(id = textResource)
                }

            } ?: run {
                stringResource(id = textResource)
            },
            fontSize = 16.sp,
            fontWeight = FontWeight.Light,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSecondary
        )
    }
}

@Composable
fun HorizontalHollowDivider(height: Int) {
    Divider(modifier = Modifier.height(height.dp), color = Color.Transparent)
}

@Composable
fun VerticalHollowDivider() {
    Divider(Modifier.width(10.dp), color = Color.Transparent)
}