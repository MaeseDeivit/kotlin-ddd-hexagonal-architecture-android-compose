package com.example.myapplication.ui.shared

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.databinding.NavHeaderMainBinding
import com.example.myapplication.stores.AuthUserStore
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NavHeaderMainFragment : Fragment() {

    private var _binding: NavHeaderMainBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = NavHeaderMainBinding.inflate(inflater, container, false)
        val root: View = binding.root

        lifecycleScope.launch {
            AuthUserStore.authUserFlow.collectLatest { authUser ->
                binding.authUserName.text = authUser?.name?.value
                binding.authUserEmail.text = authUser?.email?.value
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}