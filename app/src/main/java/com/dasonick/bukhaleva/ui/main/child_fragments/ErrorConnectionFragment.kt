package com.dasonick.bukhaleva.ui.main.child_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.dasonick.bukhaleva.R

class ErrorConnectionFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_error_connection, container, false)
        val tryAgainButton: Button = root.findViewById(R.id.tryAgain)
        return root
    }
}