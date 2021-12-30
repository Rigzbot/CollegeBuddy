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
import com.example.collegebuddy.domain.User
import com.example.collegebuddy.util.SavedPreference
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.w3c.dom.Text

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var mAuth: FirebaseAuth
    private lateinit var dbReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        mAuth = FirebaseAuth.getInstance()
        dbReference =
            FirebaseDatabase.getInstance("https://college-buddy-fc7e4-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("users")
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
        val name = binding.nameEt.text.toString()
        val enrolmentNumber = binding.enrollmentEt.text.toString()

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) ||
            TextUtils.isEmpty(enrolmentNumber) || TextUtils.isEmpty(name)
        ) {
            Toast.makeText(context, "Please enter all values.", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(
                context,
                "Password length cannot be shorter than 6 characters",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Registration Successful", Toast.LENGTH_SHORT).show()
                    goToLoginFragment()
                } else {
                    Toast.makeText(context, task.exception?.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                }
            }

        val user = User(enrolmentNumber, email, name)
        dbReference.child(enrolmentNumber).setValue(user)

        SavedPreference.setEnrolment(requireContext(), enrolmentNumber)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}