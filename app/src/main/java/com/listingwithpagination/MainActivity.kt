package com.listingwithpagination

import android.os.Bundle
import android.view.View
import android.view.View.OnScrollChangeListener
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.listingwithpagination.adapter.ProductAdapter
import com.listingwithpagination.databinding.ActivityMainBinding
import com.listingwithpagination.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var gridLayoutManager: GridLayoutManager
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
        gridLayoutManager = GridLayoutManager(this@MainActivity, 2);
        binding.recyclerView.apply {
            adapter = productAdapter
            layoutManager = gridLayoutManager
        }

        binding.recyclerView.setOnScrollChangeListener(object : OnScrollChangeListener {
            override fun onScrollChange(
                v: View?,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                if (productAdapter.isLast(gridLayoutManager.findLastVisibleItemPosition())) {
                    binding.footer.visibility = View.VISIBLE
//                    binding.recyclerView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
//                        setMargins(0, 0, 0, 100)
//                    }
                } else {
                    binding.footer.visibility = View.GONE
//                    binding.recyclerView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
//                        setMargins(0, 0, 0, 0)
//                    }
                }
            }

        })

        productAdapter.addLoadStateListener { loadState ->
            // show empty list
            if (loadState.refresh is LoadState.Loading ||
                loadState.append is LoadState.Loading
            ) {
                binding.progressBar.visibility = View.VISIBLE
                binding.progressTxt.text = "Loading"
                binding.progressTxt.visibility = View.VISIBLE


            } else {
                binding.progressBar.visibility = View.INVISIBLE
                binding.progressTxt.visibility = View.GONE
                // If we have an error, show a toast
                val errorState = when {
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                errorState?.let {
                    binding.progressTxt.text = it.error.message
                    binding.progressTxt.visibility = View.VISIBLE

//                    Toast.makeText(this, it.error.message, Toast.LENGTH_LONG).show()
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