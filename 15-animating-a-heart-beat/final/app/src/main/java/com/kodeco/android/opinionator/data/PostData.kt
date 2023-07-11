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

package com.kodeco.android.opinionator.data

import com.kodeco.android.opinionator.R
import com.kodeco.android.opinionator.models.Post
import com.kodeco.android.opinionator.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*

object PostData {
  private val emily = User(R.drawable.emily, "Emily")
  private val jennifer = User(R.drawable.jennifer, "Jennifer")
  private val allison = User(R.drawable.allison, "Allison")
  private val jake = User(R.drawable.jake, "Jake")
  private val levi = User(R.drawable.levi, "Levi")
  private val adira = User(R.drawable.adira, "Adira")
  private val suhyeon = User(R.drawable.suhyeon, "Suhyeon")

  var posts = listOf(
      Post(UUID.randomUUID(), """Hot take: Guinea Pigs are the coolest animal. Move over dogs, the dawn of the guinea pig has begun!""", adira, 0, 5, false, listOf(R.drawable.guinea1, R.drawable.guinea2, R.drawable.guinea3)),
      Post(UUID.randomUUID(), """Cheese is gross! It's just milk that you leave around for a long time right? Nasty.""", jake, 1, 150, false),
      Post(UUID.randomUUID(), """Cheese is the best food ever! What compares to a melty grilled cheese? Absolutely nothing that's what.""", suhyeon, 150, 1, false),
      Post(UUID.randomUUID(), """Not a big fan of doors. Like, get out of my way I'm trying to move here!""", jake, 10, 0, false),
      Post(UUID.randomUUID(), """Pencils are way better than pens. Why would I ever want to write with something)that can't erase?""", emily, 100, 5, false),
      Post(UUID.randomUUID(), """The color violet is better than all other colors. I mean, what's the deal with blue? Get out of here.""", levi, 3, 1, false),
      Post(UUID.randomUUID(), """I love the winter! So brisk and cozy. Also, snow is beautiful!""", jennifer, 1, 200, false, listOf(R.drawable.winter1, R.drawable.winter2, R.drawable.winter3)),
      Post(UUID.randomUUID(), """Seventh wave techno dubstep mix is the only thing that counts as real music.""", allison, 1000, 0, false),
      Post(UUID.randomUUID(), """I cannot wait for the winter to end. Worst season by far. #goodseasonsonly #jenniferissowrong #Idontthinkspringisreal""", jake, 1000, 1, false),
      Post(UUID.randomUUID(), """Mouses are waaay better than trackpads. There, I said it.""", levi, 0, 0, false)
  )
    set(value) {
      field = value
      _postsFlow.value = value
    }

  private val _postsFlow = MutableStateFlow(posts)
  val postsFlow = _postsFlow.asStateFlow()
}