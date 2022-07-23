package com.example.android_imperative.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android_imperative.R
import com.example.android_imperative.adapter.EpisodesAdapter
import com.example.android_imperative.adapter.TVShortAdapter
import com.example.android_imperative.adapter.TVShowAdapter
import com.example.android_imperative.databinding.ActivityDetailsBinding
import com.example.android_imperative.databinding.ActivityMainBinding
import com.example.android_imperative.model.Episode
import com.example.android_imperative.utils.Logger
import com.example.android_imperative.viewmodel.DetailsViewModel
import com.example.android_imperative.viewmodel.MainViewModel

class DetailsActivity : BaseActivity() {
    private val TAG = DetailsActivity::class.java.simpleName
    lateinit var binding: ActivityDetailsBinding
    private val viewModel: DetailsViewModel by viewModels()
    lateinit var adapter: TVShortAdapter
    lateinit var episodeAdapter: EpisodesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        initObservers()
        val lm = GridLayoutManager(this, 1)
        binding.rvEpisodes.layoutManager = lm
        refreshAdapter(ArrayList())

        binding.rvShorts.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val iv_detail = binding.ivDetail
        binding.ivClose.setOnClickListener {
            ActivityCompat.finishAfterTransition(this)
        }
        val extras = intent.extras
        val show_id = extras!!.getLong("show_id")
        val show_img = extras!!.getString("show_img")
        val show_name = extras!!.getString("show_name")
        val show_network = extras!!.getString("show_network")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val imageTransitionName = extras.getString("iv_movie")
            iv_detail.transitionName = imageTransitionName
        }

        binding.tvName.text = show_name
        binding.tvType.text = show_network
        Glide.with(this).load(show_img).into(iv_detail)

        viewModel.apiTVShowDetails(show_id.toInt())
    }

    private fun initObservers() {
        viewModel.tvShowDetails.observe(this){
            Logger.d(TAG, it.toString())
            refreshAdapter(it.tvShow.pictures)
            binding.tvDetails.text = it.tvShow.description
            refreshEpisodeAdapter(it.tvShow.episodes as ArrayList<Episode>)
        }
        viewModel.errorMessage.observe(this){
            Logger.d(TAG, it.toString())
        }
        viewModel.isLoading.observe(this){
            Logger.d(TAG, it.toString())
            if (it){
                binding.pbLoading.visibility = View.VISIBLE
            } else {
                binding.pbLoading.visibility = View.GONE
            }
        }
    }

    private fun refreshAdapter(pictures: List<String>) {
        adapter = TVShortAdapter(this, pictures)
        binding.rvShorts.adapter = adapter
    }

    private fun refreshEpisodeAdapter(pictures: ArrayList<Episode>) {
        episodeAdapter = EpisodesAdapter(this, pictures)
        binding.rvEpisodes.adapter = episodeAdapter
    }

}