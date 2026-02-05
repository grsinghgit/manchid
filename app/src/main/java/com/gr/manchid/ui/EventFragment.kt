package com.gr.manchid.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.gr.manchid.R
import com.gr.manchid.data.repository.event.MetaEvent
import com.gr.manchid.models.eventm.MetaViewModel
import com.gr.manchid.models.eventm.MetaEventAdapter
import com.gr.manchid.models.eventm.Resource
import androidx.appcompat.app.AlertDialog
import android.widget.EditText



class EventFragment : Fragment(R.layout.fragment_event) {

    private val viewModel: MetaViewModel by viewModels()

    private lateinit var adapter: MetaEventAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var addBtn: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ⭐ FindViewById
        recyclerView = view.findViewById(R.id.recyclerView)
        addBtn = view.findViewById(R.id.addBtn)
        // ⭐ UID
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        // ⭐ Adapter Init
        adapter = MetaEventAdapter(

            onDeleteClick = {
                val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                viewModel.deleteMeta(uid, it.id)
            },

            onEditClick = {
                Toast.makeText(requireContext(), "Edit Click", Toast.LENGTH_SHORT).show()
                showEditDialog(uid, it)
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter



        // ⭐ Load Data
        viewModel.loadMeta(uid)

        // ⭐ Observe Data
        observeData()

        // ⭐ Add Button Click
        addBtn.setOnClickListener {

            showAddDialog(uid)
            Toast.makeText(requireContext(), "Add Click", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeData() {

        viewModel.metaList.observe(viewLifecycleOwner) {

            when (it) {

                is Resource.Loading -> {}

                is Resource.Success -> {

                    val list = it.data ?: emptyList()

                    if (list.size >= 15) {
                        addBtn.visibility = View.GONE
                    } else {
                        addBtn.visibility = View.VISIBLE
                    }

                    adapter.submitList(list)
                }


                is Resource.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    //dialoge function

    private fun showAddDialog(uid:String){

        val dialogView = layoutInflater.inflate(R.layout.dialog_meta_event, null)

        val dateEdit = dialogView.findViewById<EditText>(R.id.dateEdit)
        val programEdit = dialogView.findViewById<EditText>(R.id.programEdit)
        val submitBtn = dialogView.findViewById<Button>(R.id.submitBtn)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        submitBtn.setOnClickListener {

            val date = dateEdit.text.toString()
            val program = programEdit.text.toString()

            if(date.isEmpty() || program.isEmpty()){
                Toast.makeText(requireContext(),"Fill All Fields",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val event = MetaEvent(
                date = date,
                program = program
            )

            viewModel.addMeta(uid,event)

            dialog.dismiss()
        }

        dialog.show()
    }



    //ediit dialog
    private fun showEditDialog(uid:String, meta:MetaEvent){

        val dialogView = layoutInflater.inflate(R.layout.dialog_meta_event, null)

        val dateEdit = dialogView.findViewById<EditText>(R.id.dateEdit)
        val programEdit = dialogView.findViewById<EditText>(R.id.programEdit)
        val submitBtn = dialogView.findViewById<Button>(R.id.submitBtn)

        // Old Data Fill
        dateEdit.setText(meta.date)
        programEdit.setText(meta.program)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        submitBtn.setOnClickListener {

            val updated = MetaEvent(
                date = dateEdit.text.toString(),
                program = programEdit.text.toString(),
                id = meta.id
            )

            viewModel.updateMeta(uid, updated)

            dialog.dismiss()
        }

        dialog.show()
    }




    //last
}
