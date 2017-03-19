package navigation

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import com.chmelar.jozef.bcfiredroid.BuildConfig
import com.chmelar.jozef.bcfiredroid.R
import java.util.*

enum class Navigation { none, up }
fun <A> A.log(tag: String = "Baga") = apply { if (BuildConfig.DEBUG) Log.e(tag, this.toString()) }

open class ToolbarActivity(private val setContent: (ToolbarActivity.() -> Unit), private val navigation: Navigation = Navigation.up) : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        "toolbarActivity".log("content")
        if(intent.getStringExtra("activity_session_id") == null)
            intent = intent.putExtra("activity_session_id", UUID.randomUUID().toString())

        setContent(this)

        val toolbar = findViewById(R.id.toolbar) as Toolbar?
        if(toolbar != null && toolbar.title == null)
            toolbar.title = packageManager.getActivityInfo(componentName, 0).loadLabel(packageManager)

        when (navigation) {
            Navigation.none -> { }
            Navigation.up -> {
                toolbar?.setNavigationIcon(R.drawable.abc_ic_ab_back_material)
                toolbar?.setNavigationOnClickListener { supportFinishAfterTransition() }
            }
        }
    }

    val defaultBackBehaviour = { super.onBackPressed() }
    var backBehaviour = defaultBackBehaviour
    override fun onBackPressed() = backBehaviour()
}

val Activity.activityId get() = UUID.fromString(intent.getStringExtra("activity_session_id"))