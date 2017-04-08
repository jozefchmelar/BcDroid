package recycler

import android.content.Context
import android.os.Parcelable
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import common.DividerItemDecoration
import common.VERTICAL_LIST
import extensions.android.inflate

open class RecyclerListView(context: Context, attrs: AttributeSet) : RecyclerView(context, attrs) {
    private var state: Parcelable? = null
    private val itemsAdapter = RecyclerAdapter()

    init {
        setHasFixedSize(true)
        if (layoutManager == null) // ak nebol nastaveny v XML
            layoutManager = LinearLayoutManager(context)
        adapter = itemsAdapter

    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state)
        this.state = state
    }

    fun show(items: List<ListItem>) = itemsAdapter.show(items)

    fun restoreScroll() {
        if (state != null) {
            super.onRestoreInstanceState(state)
            state = null
        }
    }

    fun toBottom() {
        this.smoothScrollToPosition(this.adapter.itemCount - 1)

    }
}

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

data class ListItem(val layoutId: Int, val show: (View) -> Unit)

fun Int.showAs(show: View.() -> Unit) = ListItem(this, show)

class RecyclerAdapter : RecyclerView.Adapter<ViewHolder>() {

    private val items = arrayListOf<ListItem>()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = items[position].show(holder.itemView)
    override fun getItemCount() = items.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(viewType))
    override fun getItemViewType(position: Int) = items[position].layoutId

    fun show(items: List<ListItem>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }
}

fun RecyclerListView.hideFabOnScroll(fab: FloatingActionButton) {
    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            if (dy > 0 || dy < 0 && fab.isShown) {
                fab.hide()
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                fab.show()
            }

            super.onScrollStateChanged(recyclerView, newState)
        }
    })
}

fun RecyclerListView.setDefaultDecorator(context: Context) {
    this.addItemDecoration(DividerItemDecoration(context, VERTICAL_LIST))

}
