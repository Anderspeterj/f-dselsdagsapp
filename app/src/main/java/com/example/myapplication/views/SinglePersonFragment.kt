package com.example.myapplication

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.myapplication.models.PersonsViewModel
import com.example.myapplication.models.Person
import com.example.myapplication.databinding.FragmentSinglePersonBinding
import java.time.LocalDate
import java.time.Period

import android.util.Log
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth

class SinglePersonFragment : Fragment() {
    private var _binding: FragmentSinglePersonBinding? = null
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val binding get() = _binding!!

    private val personsViewModel: PersonsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSinglePersonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = requireArguments()
        val singlePersonFragmentArgs: SinglePersonFragmentArgs = SinglePersonFragmentArgs.fromBundle(bundle)
        val position = singlePersonFragmentArgs.position
        val person = personsViewModel[position]

        if(person == null){
            binding.textviewMessage.text = "No such friend!"
            return
        }
        binding.editTextName.setText(person.name)
        binding.editTextDay.setText(person.birthDayOfMonth.toString())
        binding.editTextMonth.setText(person.birthMonth.toString())
        binding.editTextYear.setText(person.birthYear.toString())
        binding.textViewAge.setText("Age: " + person.age.toString())

        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.buttonDelete.setOnClickListener {
            personsViewModel.delete(person.id)
            findNavController().popBackStack()
        }


        binding.buttonUpdate.setOnClickListener {
            val user = auth.currentUser
            val name = binding.editTextName.text.toString().trim()
            val day = binding.editTextDay.text.toString().trim().toInt()
            val month = binding.editTextMonth.text.toString().trim().toInt()
            val year = binding.editTextYear.text.toString().trim().toInt()
            val age = 1
            val updatedPerson = Person(person.id, user?.email.toString(), name, year, month, day, age)
            personsViewModel.update(updatedPerson)
            findNavController().popBackStack()
        }



    }

}