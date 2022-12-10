package com.example.bisindo_recognitions.presentasion.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.bisindo_recognitions.R
import com.example.bisindo_recognitions.databinding.FragmentHomeBinding
import com.example.bisindo_recognitions.model.utils.ResultRespon
import com.example.bisindo_recognitions.presentasion.ViewModelFactory
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private val viewModel : HomeFragmentViewModel by viewModels{
        ViewModelFactory.getInstance(requireContext())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        lifecycleScope.launch {
            getData()
        }
        return binding.root
    }

    private suspend fun getData(){
        viewModel.getBisindoData().observe(viewLifecycleOwner){ respon ->
            when(respon){
                is ResultRespon.Loading ->{

                }
                is ResultRespon.Sucess ->{
                    Log.d("bisindo",respon.data.toString())
                }
                is ResultRespon.Error ->{

                }
            }
        }
    }


}