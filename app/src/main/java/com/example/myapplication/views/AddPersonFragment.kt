package com.example.myapplication


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentAddPersonBinding
import com.example.myapplication.models.Person
import com.example.myapplication.models.PersonsViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar


class AddFriendFragment : Fragment() {
    private var _binding: FragmentAddPersonBinding? = null
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val binding get() = _binding!!

    private val personsViewModel: PersonsViewModel by activityViewModels()
    private var month: Int = 1
    private var day: Int = 1


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPersonBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun getSpinnerValues(spinner: Spinner)
    {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.d("APPLE", "spinner change")
                if(spinner == binding.spinnerMonth) { month = position + 1 }
                if(spinner == binding.spinnerDay) { day = position + 1 }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.d("APPLE", "onNothingSelected was called?")

            }
        }
    }
    fun getSnackBar(view: View) {
        Snackbar.make(view, "Date not valid", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val spinnerMonth: Spinner = binding.spinnerMonth
        getSpinnerValues(spinnerMonth)
        val spinnerDay: Spinner = binding.spinnerDay
        getSpinnerValues(spinnerDay)
        val user = auth.currentUser


        binding.buttonAdd.setOnClickListener {
            val name = binding.editTextName.text.trim().toString()
            val year = binding.editTextYear.text.trim().toString().toInt()

            if (name.isEmpty()) {
                binding.editTextName.error = "No name"
                return@setOnClickListener
            }else if(year > Calendar.getInstance().get(Calendar.YEAR) || year < 0) {
                binding.editTextYear.error="Invalid year"
                return@setOnClickListener
            }
            else if(year % 4 == 0 && month == 2 && day > 29){
                getSnackBar(view)
                return@setOnClickListener
            }else if(year % 4 != 0 &&month == 2 && day > 28) {
                getSnackBar(view)
                return@setOnClickListener
            }else if((month == 4 || month == 6 || month == 9 || month ==11)&& day>30) {
                getSnackBar(view)
                return@setOnClickListener
            }
            else {
                val newPerson = Person(user?.email.toString(), name, year, month, day)
                personsViewModel.add(newPerson)
                findNavController().popBackStack()
            }
        }

        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }


}