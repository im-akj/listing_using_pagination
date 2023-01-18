package com.listingwithpagination

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.listingwithpagination.adapter.ProductAdapter
import com.listingwithpagination.databinding.ActivityMainBinding
import com.listingwithpagination.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: ProductViewModel by viewModels()

    @Inject
    lateinit var productAdapter: ProductAdapter
    private var isFirstTime = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        loadData()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = productAdapter
            layoutManager = GridLayoutManager(this@MainActivity, 2)
        }

        productAdapter.addLoadStateListener { loadState ->
            // show empty list
            if (loadState.refresh is LoadState.Loading ||
                loadState.append is LoadState.Loading
            ) {
                if (isFirstTime) {
//                    binding.veilRecyclerView.veil()
                } else {
//                    binding.progressBar.isVisible = true
//                    binding.veilRecyclerView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
//                        setMargins(0, 0, 0, 100)
//                    }
                }
            } else {
                if (isFirstTime) {
//                    binding.veilRecyclerView.postDelayed({
//                        binding.veilRecyclerView.unVeil()
//                        isFirstTime = false
//                    }, 2000)
                }
//                binding.progressBar.isVisible = false
//                binding.veilRecyclerView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
//                    setMargins(0, 0, 0, 0)
//                }
                // If we have an error, show a toast
                val errorState = when {
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                errorState?.let {
                    Toast.makeText(this, it.error.message, Toast.LENGTH_LONG).show()
                }

            }
        }
    }

    private fun loadData() {
        lifecycleScope.launch {
            viewModel.listData.collect {
                productAdapter.submitData(it)
            }
        }
    }
}