package com.tdl.recipee.support

import android.graphics.Bitmap
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Function

/**
 * Created by lam on 2/6/18.
 */
class ImageService(private val networkClient: NetworkClient) {

    fun loadImage(imageUrl: String): Observable<Bitmap> {
        return Observable.just(imageUrl).flatMap(fetchImage())
    }

    private fun fetchImage(): Function<String, ObservableSource<Bitmap>> {
        return Function { imageUrl ->
            Observable.defer {
                networkClient
                    .fetchImage(imageUrl)
                    .onErrorResumeNext(Function { Observable.error(it) })
            }
        }
    }
}
