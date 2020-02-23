package com.tdl.recipee.support

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * Created by loc.ta on 2/22/2020.
 */
class NetworkClient(private val okHttpClient: OkHttpClient) {

    fun fetchImage(imageUrl: String): Observable<Bitmap> {
        return Observable.fromCallable {
            val loadRequest = Request.Builder()
                .url(imageUrl)
                .build()

            val response = okHttpClient
                .newCall(loadRequest)
                .execute()

            BitmapFactory.decodeStream(requireNotNull(response.body()).byteStream())
        }
    }
}