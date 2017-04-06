package extensions.spinner

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView

fun <T> Spinner.setupAdapter(list: List<T>) {
    adapter = SimpleSpinnerAdapter(context, list)
    setSelection(list.size)
}

//http://stackoverflow.com/questions/867518/how-to-make-an-android-spinner-with-initial-text-select-one
//http://inthecheesefactory.com/blog/fragment-state-saving-best-practices/en
//http://trickyandroid.com/saving-android-view-state-correctly/
//http://charlesharley.com/2012/programming/views-saving-instance-state-in-android/
class SimpleSpinnerAdapter<T>(context: Context, list: List<T>) : ArrayAdapter<T>(context, android.R.layout.simple_spinner_dropdown_item, list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val v = super.getView(position, convertView, parent)

//        if (position == count) {
//            (v.findViewById(android.R.id.text1) as TextView).hint = getItem(count).toString()
//        }

        return v;
    }

    override fun getCount(): Int = super.getCount() - 1
}