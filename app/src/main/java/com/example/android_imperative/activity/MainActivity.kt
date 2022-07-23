package com.example.android_imperative.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_imperative.R
import com.example.android_imperative.adapter.TVShowAdapter
import com.example.android_imperative.databinding.ActivityMainBinding
import com.example.android_imperative.model.TVShow
import com.example.android_imperative.utils.Logger
import com.example.android_imperative.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

class MainActivity : BaseActivity() {
    private val TAG = MainActivity::class.java.simpleName
    val viewModel: MainViewModel by viewModels()
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: TVShowAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        initObservers()
        val lm = GridLayoutManager(this, 2)
        binding.rvHome.layoutManager = lm
        refreshAdapter(ArrayList())
        binding.rvHome.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (lm.findLastCompletelyVisibleItemPosition() == adapter.itemCount - 1) {
                    val nextPage = viewModel.tvShowPopular.value!!.page+1
                    val totalPage = viewModel.tvShowPopular.value!!.page
                    if (nextPage <= totalPage) {
                        viewModel.apiTVShowPopular(nextPage)
                    }
                }
            }
        })

        binding.bFab.setOnClickListener {
            binding.rvHome.smoothScrollToPosition(0)
        }
    }

    private fun refreshAdapter(items: ArrayList<TVShow>) {
        adapter = TVShowAdapter(this, items)
        binding.rvHome.adapter = adapter
    }

    private fun initObservers() {
        //Retrofit Related
        viewModel.tvShowFromApi.observe(this) {
            Logger.d(TAG, it.size.toString())
            adapter.setNewTVShows(it)
        }
        viewModel.errorMessage.observe(this) {
            Logger.d(TAG, it.toString())
        }
        viewModel.isLoading.observe(this) {
            Logger.d(TAG, it.toString())
            if (it) {
                binding.pbLoading.visibility = View.VISIBLE
            } else {
                binding.pbLoading.visibility = View.GONE
            }
        }


        //Room Related
    }

    fun callDetailsActivity(tvShow: TVShow, sharedImageView: ImageView) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("show_id", tvShow.id)
        intent.putExtra("show_img", tvShow.image_thumbnail_path)
        intent.putExtra("show_name", tvShow.name)
        intent.putExtra("show_network", tvShow.network)
        intent.putExtra("iv_movie", ViewCompat.getTransitionName(sharedImageView))

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this, sharedImageView, ViewCompat.getTransitionName(sharedImageView)!!)
        startActivity(intent, options.toBundle())
    }
}