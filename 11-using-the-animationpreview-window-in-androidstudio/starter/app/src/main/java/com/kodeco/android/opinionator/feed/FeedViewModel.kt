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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kodeco.android.opinionator.R
import com.kodeco.android.opinionator.data.PostsRepository
import com.kodeco.android.opinionator.data.PostsRepositoryImpl
import com.kodeco.android.opinionator.models.Post
import com.kodeco.android.opinionator.models.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*

class FeedViewModel : ViewModel() {
  private val postsRepository: PostsRepository = PostsRepositoryImpl()
  val uiState = MutableStateFlow(PostsUIState())

  init {
    getPostLoadingState()
  }

  fun postLiked(post: Post) {
    viewModelScope.launch {
      val postIsLiked = !post.hasBeenLiked
      val newLikeCount = if (postIsLiked) post.likes + 1 else post.likes - 1
      val newPost = post.copy(hasBeenLiked = postIsLiked, likes = newLikeCount)
      postsRepository.updatePost(newPost)
      uiState.update {
        it.copy(
          isLoading = false,
          feedScreenShowing = FeedScreenShowing.Feed,
          posts = postsRepository.getPosts()
        )
      }
    }
  }

  fun addPost(postText: String) {
    viewModelScope.launch {
      val post = Post(UUID.randomUUID(), postText, User(R.drawable.person, "You"), 0, 0, false)
      postsRepository.createPost(post)
      uiState.update {
        it.copy(
          isLoading = false,
          feedScreenShowing = FeedScreenShowing.Feed,
          posts = postsRepository.getPosts()
        )
      }
    }
  }

  private fun getPostLoadingState() {
    viewModelScope.launch {
      uiState.update {
        it.copy(
          isLoading = true,
          feedScreenShowing = FeedScreenShowing.Loading,
        )
      }
    }
  }

  fun getPosts() {
    viewModelScope.launch {
      delay(1000)
      uiState.update {
        it.copy(
          posts = postsRepository.getPosts(),
          feedScreenShowing = FeedScreenShowing.Feed,
          isLoading = false
        )
      }
    }
  }
}