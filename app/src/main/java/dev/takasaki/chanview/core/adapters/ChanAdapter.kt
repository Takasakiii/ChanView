package dev.takasaki.chanview.core.adapters

import dev.takasaki.chanview.core.dtos.BoardsResponse
import dev.takasaki.chanview.core.dtos.CatalogPageResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ChanAdapter {
    @GET("boards.json")
    fun getBoards(): Call<BoardsResponse>

    @GET("{board}/catalog.json")
    fun getCatalog(@Path("board") board: String): Call<List<CatalogPageResponse>>
}