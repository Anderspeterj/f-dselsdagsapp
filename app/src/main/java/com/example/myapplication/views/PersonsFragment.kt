package com.example.myapplication

import android.graphics.Path.Direction
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.example.myapplication.databinding.FragmentPersonsBinding
import com.example.myapplication.models.Person
import com.example.myapplication.models.PersonsViewModel
import com.example.myapplication.models.MyAdapter
import com.google.firebase.auth.FirebaseAuth


class PersonsFragment : Fragment() {
    private var _binding: FragmentPersonsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.

    private val personsViewModel : PersonsViewModel by activityViewModels()

    private val binding get() = _binding!!

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var userId: String? = auth.currentUser?.email


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPersonsBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        personsViewModel.personsLiveData.observe(viewLifecycleOwner) {Persons ->
            binding.progressbar.visibility = View.GONE

            binding.recyclerView.visibility = if (Persons == null) View.GONE else View.VISIBLE

            binding.textView.text = auth.currentUser?.email
            if (Persons != null) {


                val adapter = MyAdapter(Persons) { position ->
                    val action =
                        PersonsFragmentDirections.actionPersonsFragmentToSinglepersonFragment(position)
                    findNavController().navigate(action)
                    //findNavController().navigate(R.id.action_PersonsFragment_to_singlePersonFragment)
                }
                binding.recyclerView.layoutManager = LinearLayoutManager(activity)

                binding.recyclerView.adapter = adapter


                val spinner: Spinner = binding.spinnerSorting
                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        Log.d("APPLE", "spinner change")

                        if(position == 1){personsViewModel.sortByName()}
                        if(position == 2){personsViewModel.sortByNameDescending()}
                        if(position == 3){personsViewModel.sortByAge()}
                        if(position == 4){personsViewModel.sortByAgeDescending()}
                        if(position == 5){personsViewModel.sortByBirth()}
                        if(position == 6){personsViewModel.sortByBirthDescending()}

                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        Log.d("APPLE", "onNothingSelected was called?")

                    }
                }

                binding.buttonFilter.setOnClickListener {
                    val name = binding.edittextFilterName.text.toString().trim()
                    personsViewModel.filter(name, userId)
                }

            }
            binding.buttonAdd.setOnClickListener {
                findNavController().navigate(R.id.action_personsFragment_to_addpersonFragment)
            }

        }
        personsViewModel.reload(userId)

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}