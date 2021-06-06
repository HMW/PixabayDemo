package com.jajinba.pixabaydemo

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import androidx.annotation.LayoutRes
import butterknife.ButterKnife
import com.jajinba.pixabaydemo.view.fragment.BaseFragment
import butterknife.BindView
import com.jajinba.pixabaydemo.R
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jajinba.pixabaydemo.adapter.ImageListAdapter
import com.jajinba.pixabaydemo.presenter.ListPresenter
import com.jajinba.pixabaydemo.model.PixabayImageObject
import android.text.TextUtils
import com.jajinba.pixabaydemo.model.ImageManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.jajinba.pixabaydemo.view.fragment.ImageGridFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.jajinba.pixabaydemo.view.fragment.ImageListFragment
import com.jajinba.pixabaydemo.view.ViewHolder.OnRecyclerViewItemClickListener
import com.jajinba.pixabaydemo.view.ViewHolder.OnRecyclerViewItemLongClickListener
import android.util.SparseArray
import androidx.annotation.IdRes
import android.view.View.OnLongClickListener
import com.jajinba.pixabaydemo.view.ViewHolder.OnViewHolderItemClickListener
import com.jajinba.pixabaydemo.view.ViewHolder.OnViewHolderItemLongClickListener
import androidx.appcompat.app.AppCompatActivity
import android.widget.ProgressBar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import android.view.MenuInflater
import android.app.SearchManager
import com.jajinba.pixabaydemo.presenter.MainActivityPresenter
import com.jajinba.pixabaydemo.adapter.ViewPagerAdapter
import android.content.DialogInterface
import androidx.annotation.StringRes
import com.jajinba.pixabaydemo.MainApplication
import com.jajinba.pixabaydemo.view.MainActivity
import androidx.annotation.StringDef
import kotlin.jvm.JvmOverloads
import com.jajinba.pixabaydemo.utils.SearchUtils
import com.jajinba.pixabaydemo.network.ApiClient
import com.jajinba.pixabaydemo.model.PixabayResponseObject
import io.reactivex.disposables.Disposable
import com.google.gson.annotations.SerializedName
import com.bumptech.glide.Glide
import androidx.fragment.app.FragmentPagerAdapter
import com.jajinba.pixabaydemo.network.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlin.jvm.Volatile
import retrofit2.http.POST
import android.app.Application
import org.junit.Assert
import org.junit.Test
import java.lang.Exception
import kotlin.Throws

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleUnitTest {
    @Test
    @Throws(Exception::class)
    fun addition_isCorrect() {
        Assert.assertEquals(4, (2 + 2).toLong())
    }
}