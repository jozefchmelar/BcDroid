package rx

import android.view.View
import extensions.android.onAttach
import extensions.android.onDetach
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

interface BindFunction { operator fun <A> invoke(source: Observable<A>, action: (A) -> Unit) }

inline fun <Key, Value> BindFunction.by(key: Observable<Key>, crossinline selectSource: (Key) -> Observable<Value>, crossinline action: (Key, Value) -> Unit) =
    this.invoke(key.switchMap { keyValue -> selectSource(keyValue).map { keyValue to it } }) { (k, v) -> action(k, v) }

fun <V: View> V.whenAttached(action: V.(BindFunction) -> Unit): Unit = when {
    isAttachedToWindow -> action(bindUntilDetached())
    else               -> onAttach { action(bindUntilDetached()) }
}

private fun View.bindUntilDetached() = object : BindFunction {
    override fun <A> invoke(source: Observable<A>, action: (A) -> Unit) {
        source
            .takeUntil(Observable.create<Unit> { x -> onDetach { x.onNext(Unit) } })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(action)
    }
}