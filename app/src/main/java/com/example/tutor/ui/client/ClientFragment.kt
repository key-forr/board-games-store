package com.example.tutor.ui.client

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tutor.databinding.FragmentClientBinding

class ClientFragment : Fragment() {
    private lateinit var binding: FragmentClientBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentClientBinding.inflate(inflater, container, false)
        return binding.root
    }
}