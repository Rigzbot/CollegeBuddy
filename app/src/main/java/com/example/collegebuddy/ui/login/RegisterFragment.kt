package com.example.collegebuddy.ui.login

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.collegebuddy.databinding.FragmentRegisterBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        mAuth = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        binding.signUpButton.setOnClickListener {
            registerUser()
        }

        binding.backToLoginButton.setOnClickListener {
            goToLoginFragment()
        }

        binding.registerBackButton.setOnClickListener {
            goToLoginFragment()
        }
    }

    private fun goToLoginFragment() {
        val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
        binding.root.findNavController().navigate(action)
    }

    private fun registerUser() {
        val email = binding.emailEt.text.toString()
        val password = binding.passwordEt.text.toString()
        val confirmPassword = binding.confirmPasswordEt.text.toString()

        if(password != confirmPassword) {
            Toast.makeText(context, "Passwords entered do not match, Please try again.", Toast.LENGTH_LONG).show()
            binding.passwordEt.text.clear()
            binding.confirmPasswordEt.text.clear()
            return
        }

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(context, "Please enter all values.", Toast.LENGTH_SHORT).show()
            return
        }

        if(password.length < 6){
            Toast.makeText(context, "Password length cannot be shorter than 6 characters", Toast.LENGTH_LONG).show()
            return
        }

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Registration Successful", Toast.LENGTH_SHORT).show()
                    goToLoginFragment()
                } else {
                    Toast.makeText(context, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}