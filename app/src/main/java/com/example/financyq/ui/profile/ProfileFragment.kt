package com.example.financyq.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.financyq.R
import com.example.financyq.data.local.UserPreferences
import com.example.financyq.databinding.FragmentProfileBinding
import com.example.financyq.ui.about.AboutFinancyQActivity
import com.example.financyq.ui.about.AboutUsActivity
import com.example.financyq.ui.about.PrivacyPolicyActivity
import com.example.financyq.ui.welcome.WelcomeActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var userPreferences: UserPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPreferences = UserPreferences.getInstance(requireContext())

        setupAction()
    }

    private fun setupAction() {
        binding.apply {
            btnAboutFinancyq.setOnClickListener {
                startActivity(Intent(requireContext(), AboutFinancyQActivity::class.java))
            }

            btnPrivacyPolicy.setOnClickListener {
                startActivity(Intent(requireContext(), PrivacyPolicyActivity::class.java))
            }

            btnAboutUs.setOnClickListener {
                startActivity(Intent(requireContext(), AboutUsActivity::class.java))
            }

            btnLogout.setOnClickListener {
                showLogoutConfirmationDialog()
            }
        }
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.confirm_logout)
            .setMessage(R.string.message_logout)
            .setPositiveButton(R.string.yes) { dialog, _ ->
                logoutUser()
                dialog.dismiss()
            }
            .setNegativeButton(R.string.No) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun logoutUser() {
        lifecycleScope.launch(Dispatchers.IO) {
            userPreferences.clearToken()
        }
        navigateToWelcomeScreen()
    }

    private fun navigateToWelcomeScreen() {
        val intent = Intent(requireContext(), WelcomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
