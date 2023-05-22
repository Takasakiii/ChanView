package dev.takasaki.chanview.core.services

import android.net.Uri
import dev.takasaki.chanview.core.adapters.ChanAdapter
import dev.takasaki.chanview.core.dtos.Board
import dev.takasaki.chanview.core.dtos.BoardsResponse
import dev.takasaki.chanview.core.dtos.CatalogPageResponse
import dev.takasaki.chanview.core.dtos.Post
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object ChanService {
    private val chanHttpClient = Retrofit.Builder()
        .baseUrl("https://a.4cdn.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ChanAdapter::class.java)

    suspend fun getBoards(): List<Board> {
        return suspendCoroutine { continuation ->
            chanHttpClient.getBoards().enqueue(object : Callback<BoardsResponse> {
                override fun onFailure(call: Call<BoardsResponse>, t: Throwable) {
                    continuation.resumeWithException(t)
                }

                override fun onResponse(
                    call: Call<BoardsResponse>,
                    response: Response<BoardsResponse>
                ) {
                    continuation.resume(response.body()?.boards ?: listOf())
                }
            })
        }
    }

    suspend fun getCatalog(board: Board): List<Post> {
        return suspendCoroutine { continuation ->
            chanHttpClient.getCatalog(board.board)
                .enqueue(object : Callback<List<CatalogPageResponse>> {
                    override fun onFailure(call: Call<List<CatalogPageResponse>>, t: Throwable) {
                        println(t.message)
                        continuation.resumeWithException(t)
                    }

                    override fun onResponse(
                        call: Call<List<CatalogPageResponse>>,
                        response: Response<List<CatalogPageResponse>>
                    ) {
                        val posts = mutableListOf<Post>()
                        val pages = response.body() ?: listOf()

                        for (page in pages) {
                            for (thread in page.threads) {
                                if (thread.ext == ".webm") continue

                                val post = Post(
                                    thread.now,
                                    thread.com?.replace(Regex("<.*?>"), "")
                                        ?.replace(Regex("&.*;"), ""),
                                    if (thread.tim != null)
                                        Uri.parse("https://i.4cdn.org/${board.board}/${thread.tim}${thread.ext}")
                                    else
                                        null, "https://boards.4chan.org/${board.board}/thread/${thread.no}"
                                )
                                posts.add(post)
                            }
                        }

                        continuation.resume(posts)
                    }
                })
        }
    }
}