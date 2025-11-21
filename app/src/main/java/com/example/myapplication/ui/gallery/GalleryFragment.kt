package com.example.myapplication.ui.gallery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentGalleryBinding
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.src.authusers.application.AuthUserServices
import com.example.myapplication.stores.AuthUserStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var authUserServices: AuthUserServices

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        lifecycleScope.launch {
            AuthUserStore.authUserFlow.collectLatest { authUser ->
                binding.textAuthUserEmail.text = authUser?.email.toString()

            }
        }

        val button: Button = binding.buttonGallery
        button.setOnClickListener {
            Log.d("GalleryFragment", "Button Clicked!")
            lifecycleScope.launch {
                val authUser = authUserServices.login("j.doe@gmail.com", "password")
                AuthUserStore.setAuthUser(requireContext(), authUser)
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}