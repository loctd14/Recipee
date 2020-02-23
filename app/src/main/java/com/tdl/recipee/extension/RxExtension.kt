package com.tdl.recipee.extension

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables

/**
 * Created by loc.ta on 2/22/2020.
 */
operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
    add(disposable)
}

fun AppCompatEditText?.textChanges(): Observable<String> {
    return Observable.create { e ->
        val simpleTextWatcher = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!e.isDisposed) {
                    e.onNext(s.toString().trim())
                }
            }
        }
        this?.addTextChangedListener(simpleTextWatcher)
        e.setDisposable(Disposables.fromAction { this?.removeTextChangedListener(simpleTextWatcher) })
    }
}

fun View?.clicks(): Observable<Boolean> {
    return Observable.create { emitter ->
        this?.setOnClickListener {
            if (!emitter.isDisposed) {
                emitter.onNext(true)
            } else {
                emitter.setDisposable(Disposables.fromAction { it.setOnClickListener(null) })
            }
        }
    }
}