package extensions.android

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import io.reactivex.Observable

fun TextView.textChanges(): Observable<String> = Observable.create { observer ->
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(e: Editable) {}
        override fun beforeTextChanged(c: CharSequence, p1: Int, p2: Int, p3: Int) { }
        override fun onTextChanged(text: CharSequence, p1: Int, p2: Int, p3: Int) =
            observer.onNext(string)
    })
    observer.onNext(string)
}

var TextView.string: String
    get() = text.toString()
    set(msg) {text = msg}

var TextView.textOrGone
    get() = text.toString()
    set(msg) { text = msg; beGoneIf(msg.isBlank()) }
