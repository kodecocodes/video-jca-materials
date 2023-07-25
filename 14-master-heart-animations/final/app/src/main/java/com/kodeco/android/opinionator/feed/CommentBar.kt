/*
 * Copyright (c) 2023 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * This project and source code may use libraries or frameworks that are
 * released under various Open-Source licenses. Use of those libraries and
 * frameworks are governed by their own individual licenses.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.kodeco.android.opinionator.feed

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kodeco.android.opinionator.R
import com.kodeco.android.opinionator.models.Post
import com.kodeco.android.opinionator.models.User
import java.util.UUID

@Composable
fun CommentBar(
    post: Post,
    onPostLiked: () -> Unit
) {
  val likeImage = if (post.hasBeenLiked) R.drawable.favorite else R.drawable.favorite_border
  val viewModel: FeedViewModel = viewModel()
  var changeColor by remember { mutableStateOf(false) }
  val startColor = Color.Black
  val endColor = Color.Green

  val backgroundColor by animateColorAsState(
      targetValue = if (changeColor) endColor else startColor,
      animationSpec = tween(
          durationMillis = 2000,
          delayMillis = 10,
          easing = LinearEasing
      )
  )

  Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(top = 8.dp)) {
    Image(
        painter = painterResource(id = likeImage),
        contentDescription = "Favorite",
        colorFilter = ColorFilter.tint(Color.Black),
        modifier = Modifier
            .size(16.dp)
            .clickable {
              onPostLiked()
              viewModel.postLiked(post)
            }
    )
    LikeCount(post = post)
    Image(
        painter = painterResource(id = R.drawable.comment),
        contentDescription = "Comment",
        modifier = Modifier
            .padding(start = 16.dp)
            .size(16.dp)
            .clickable {
              changeColor = !changeColor
            },
        colorFilter = ColorFilter.tint(backgroundColor),
    )
    Text("${post.comments}", modifier = Modifier.padding(start = 4.dp))
  }
}

@Composable
private fun LikeCount(post: Post) {
  val previousLikeCount = remember { mutableStateOf(post.likes) }
  val likeCountAnimation = useLikeCountAnimation(likes = post.likes)
  if (likeCountAnimation.finished) {
    previousLikeCount.value = post.likes
  }
  Box(modifier = Modifier.padding(start = 4.dp)) {
    Text(
        text = "${post.likes}",
        modifier = Modifier
    )
    Text(
        text = "${previousLikeCount.value}",
        modifier = Modifier
            .graphicsLayer(
                translationY = likeCountAnimation.translation,
                alpha = likeCountAnimation.alpha
            )
    )
  }
}

private enum class LikeAnimationState {
  Started,
  Ended
}

data class LikeCountAnimation(
    val alpha: Float,
    val translation: Float,
    val finished: Boolean
)

@Composable
private fun useLikeCountAnimation(likes: Int): LikeCountAnimation {
  val state = remember(likes) { MutableTransitionState(LikeAnimationState.Started) }
  state.targetState = LikeAnimationState.Ended

  val transition = updateTransition(state, label = "Like Count Transition")
  val translation by transition.animateDp(
      label = "Translation",
      transitionSpec = { spring(stiffness = Spring.StiffnessLow) }
  ) { animationState ->
    when (animationState) {
      LikeAnimationState.Started -> 0.dp
      LikeAnimationState.Ended -> (-15).dp
    }
  }
  val translationPx = with(LocalDensity.current) { translation.toPx() }
  val alpha by transition.animateFloat(
      label = "Alpha",
      transitionSpec = { spring(stiffness = Spring.StiffnessLow) }
  ) { animationState ->
    when (animationState) {
      LikeAnimationState.Started -> 1f
      LikeAnimationState.Ended -> 0.0f
    }
  }
  val isFinished = transition.currentState == transition.targetState
  return LikeCountAnimation(alpha, translationPx, isFinished)
}

val post = Post(
    UUID.randomUUID(),
    "Test",
    User(R.drawable.default_account, "Test"),
    100,
    100,
    false
)

@Preview
@Composable
private fun LikeCountPreview() {
  Box(
      contentAlignment = Alignment.Center,
      modifier = Modifier
          .width(200.dp)
          .height(200.dp)
  ) {
    LikeCount(post)
  }}