/*
 * Copyright (c) 2021 Razeware LLC
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

package com.raywenderlich.android.opinionator.feed

import android.widget.ProgressBar
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.raywenderlich.android.opinionator.R
import com.raywenderlich.android.opinionator.darkTransparent
import com.raywenderlich.android.opinionator.data.PostLoadingState
import com.raywenderlich.android.opinionator.models.Post
import com.raywenderlich.android.opinionator.models.User

@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
fun FeedScreen() {
  Feed()
}

@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
private fun Feed() {
  val viewModel = viewModel<FeedViewModel>()
  val postLoadingState by viewModel.posts.collectAsState(initial = PostLoadingState.Loading)
  when (postLoadingState) {
    PostLoadingState.Loading -> LoadingFeed()
    is PostLoadingState.Populated -> PopulatedFeed(
      posts = (postLoadingState as PostLoadingState.Populated).posts
    )
  }
}

@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
private fun PopulatedFeed(posts: List<Post>) {
  var isShowingPostInput by remember { mutableStateOf(false) }
  val rotationAnimation = animateFloatAsState(
    targetValue = if (isShowingPostInput) 405f else 0f
  )
  val viewModel: FeedViewModel = viewModel()

  Scaffold(
    floatingActionButton = {
      FloatingActionButton(
        onClick = { isShowingPostInput = !isShowingPostInput },
      ) {
        Icon(
          painter = painterResource(id = R.drawable.add),
          contentDescription = "Add Post",
          modifier = Modifier.graphicsLayer(rotationZ = rotationAnimation.value)
        )
      }
    }
  ) {
    Box {
      LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = Modifier
          .fillMaxWidth()
      ) {
        item {
          TopAppBar(
            title = { Text("Opinionator") },
            backgroundColor = Color.Transparent,
            elevation = 0.dp,
          )
        }
        for (post in posts) {
          item { Post(post) }
        }
      }
      AddPost(
        show = isShowingPostInput,
        onDoneClicked = {
          isShowingPostInput = false
          viewModel.addPost(it)
        }
      )
    }
  }
}

@Composable
private fun LoadingFeed() {
  Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier
      .fillMaxWidth()
      .fillMaxHeight()
  ) {
    CircularProgressIndicator()
  }
}

@ExperimentalPagerApi
@Composable
private fun Post(post: Post) {
  Column(modifier = Modifier.padding(vertical = 16.dp)) {
    ProfileItem(modifier = Modifier.padding(bottom = 16.dp), post.user)
    PostBody(post)
  }
}

@ExperimentalPagerApi
@Composable
private fun PostBody(post: Post) {
  Card(
    shape = RoundedCornerShape(4.dp),
    elevation = 8.dp,
    modifier = Modifier.fillMaxWidth()
  ) {
    Box(contentAlignment = Alignment.Center) {
      Column(
        modifier = Modifier
          .padding(16.dp)
          .fillMaxWidth()
      ) {
        Text(post.text)
        ImagePager(post.attachedImages)
        CommentBar(post)
      }
    }
  }
}

@Composable
private fun ProfileItem(modifier: Modifier = Modifier, user: User) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Image(
      painter = painterResource(id = user.profileImage),
      contentScale = ContentScale.Crop,
      contentDescription = "Profile Image",
      modifier = modifier
        .size(48.dp)
        .shadow(8.dp, CircleShape)
    )
    Text(text = user.userName, modifier = Modifier.padding(start = 16.dp))
  }
}
