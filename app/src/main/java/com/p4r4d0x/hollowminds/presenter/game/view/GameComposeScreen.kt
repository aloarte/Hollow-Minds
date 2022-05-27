package com.p4r4d0x.hollowminds.presenter.game.view

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.p4r4d0x.hollowminds.R
import com.p4r4d0x.hollowminds.domain.Utils.ANIMATION_DURATION
import com.p4r4d0x.hollowminds.domain.bo.CharacterCardData
import com.p4r4d0x.hollowminds.presenter.common.HollowButton
import com.p4r4d0x.hollowminds.presenter.game.viewmodel.GameViewModel
import com.p4r4d0x.hollowminds.theme.GreyLightTransparent

@Composable
fun GameLayout(viewModel: GameViewModel, spanValue: Int, onReset: () -> Unit) {
    val time = viewModel.time.observeAsState()
    val pairsMatched = viewModel.pairsMatched.observeAsState()
    val totalPairs = viewModel.totalPairs.observeAsState()
    val movements = viewModel.movements.observeAsState()
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.game_background),
            contentDescription = "Game background"
        )

        Row(
            Modifier
                .fillMaxWidth()
                .height(200.dp)
                .align(Alignment.TopCenter)
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                Modifier.fillMaxWidth(0.3F),
                horizontalAlignment = CenterHorizontally
            ) {
                Text(
                    modifier = Modifier,
                    style = MaterialTheme.typography.h5,
                    color = Color.White,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Justify,
                    maxLines = 2,
                    text = stringResource(id = R.string.game_remaining_time)
                )
                Text(
                    modifier = Modifier,
                    style = MaterialTheme.typography.h4,
                    color = Color.White,
                    text = time.value ?: "00:00"
                )
            }

            if (viewModel.charactersLoaded.value == true) {
                Column(
                    Modifier.fillMaxWidth(0.6F),
                    horizontalAlignment = CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier,
                        fontSize = 14.sp,
                        style = MaterialTheme.typography.h5,
                        color = Color.White,
                        maxLines = 2,
                        textAlign = TextAlign.Justify,
                        text = stringResource(id = R.string.game_pairs_unlocked)
                    )
                    Text(
                        modifier = Modifier,
                        style = MaterialTheme.typography.h4,
                        color = Color.White,
                        text = "${pairsMatched.value}/${totalPairs.value}"
                    )
                }
            }

            if (viewModel.charactersLoaded.value == true) {
                Column(
                    Modifier.fillMaxWidth(1F),
                    horizontalAlignment = CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier,
                        fontSize = 14.sp,
                        style = MaterialTheme.typography.h5,
                        color = Color.White,
                        maxLines = 2,
                        textAlign = TextAlign.Justify,
                        text = stringResource(id = R.string.game_movements)
                    )
                    Text(
                        modifier = Modifier,
                        style = MaterialTheme.typography.h4,
                        color = Color.White,
                        text = "${movements.value}"
                    )
                }
            }

        }

        GameGrid(
            viewModel, spanValue,
            Modifier
                .wrapContentWidth()
                .align(Alignment.Center)
        )
        Row(
            Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 60.dp)
        ) {
            HollowButton(
                textResource = R.string.btn_game_reset
            ) {
                onReset.invoke()
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GameGrid(viewModel: GameViewModel, spanValue: Int, modifier: Modifier = Modifier) {

    val data = viewModel.characterCardsData
    LazyVerticalGrid(
        modifier = modifier.width(
            if (spanValue == 4) {
                350.dp
            } else {
                380.dp
            }
        ),
        cells = GridCells.Fixed(spanValue),
        contentPadding = PaddingValues(8.dp)
    ) {
        itemsIndexed(items = data) { index, item ->
            GameCard(viewModel, index, item)
        }
    }
}

@Composable
fun GameCard(viewModel: GameViewModel, index: Int, item: CharacterCardData) {

    val rotation by animateFloatAsState(
        targetValue = if (item.selected) 180f else 0f,
        animationSpec = tween(ANIMATION_DURATION)
    )

    val animateFront by animateFloatAsState(
        targetValue = if (!item.selected) 1f else 0f,
        animationSpec = tween(ANIMATION_DURATION)
    )

    val animateBack by animateFloatAsState(
        targetValue = if (item.selected) 1f else 0f,
        animationSpec = tween(ANIMATION_DURATION)
    )

    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .width(80.dp)
                .height(80.dp)
                .padding(2.dp)
                .graphicsLayer {
                    rotationY = rotation
                    cameraDistance = 8 * density
                }
                .clickable {
                    if (!item.matched && !item.selected && viewModel.processingPair.value == false) {
                        with(viewModel) {
                            setItemInList(index, selected = true, matched = false)
                            itemRevealed(index)
                            checkGameStatus()
                        }
                    }
                },
            border = BorderStroke(0.05.dp, MaterialTheme.colors.onSecondary),
            backgroundColor = GreyLightTransparent
        ) {
            if (item.selected || item.matched) {
                CharacterCardSide(item, item.selected, rotation, animateBack, animateFront)
            } else {
                BackCardSide(item.selected, rotation, animateBack, animateFront)
            }
        }
    }
}

@Composable
fun BackCardSide(rotated: Boolean, rotation: Float, animateBack: Float, animateFront: Float) {
    Image(
        modifier = Modifier
            .padding(5.dp)
            .graphicsLayer {
                alpha = if (rotated) animateBack else animateFront
                rotationY = rotation
            },
        painter = painterResource(id = R.drawable.back_card),
        contentDescription = "Back card"
    )
}

@Composable
fun CharacterCardSide(
    item: CharacterCardData,
    rotated: Boolean,
    rotation: Float,
    animateBack: Float,
    animateFront: Float
) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        Image(
            modifier = Modifier
                .width(60.dp)
                .height(60.dp)
                .align(CenterHorizontally)
                .graphicsLayer {
                    alpha = if (rotated) animateBack else animateFront
                    rotationY = rotation
                },
            painter = painterResource(id = item.characterImage),
            contentDescription = "Character ${item.characterName} image "
        )
        Text(
            text = item.characterName,
            fontSize = 11.sp,
            style = MaterialTheme.typography.h4,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.onSecondary,
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    alpha = if (rotated) animateBack else animateFront
                    rotationY = rotation
                }
        )
    }
}

