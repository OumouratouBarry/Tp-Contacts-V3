package com.example.contacts

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListeContactsFragment : Fragment() {

    private val viewModel: ContactViewModel by activityViewModels()
    private lateinit var adapter: ContactAdapter
    private lateinit var txtAucunResultat: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_liste_contacts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val edtRecherche = view.findViewById<EditText>(R.id.edtRecherche)
        txtAucunResultat = view.findViewById(R.id.txtAucunResultat)
        val recyclerContacts = view.findViewById<RecyclerView>(R.id.recyclerContacts)

        adapter = ContactAdapter(emptyList()) { contact ->
            ouvrirDetailContact(contact.id)
        }

        recyclerContacts.layoutManager = LinearLayoutManager(requireContext())
        recyclerContacts.adapter = adapter

        viewModel.displayedContacts.observe(viewLifecycleOwner) { contacts ->
            adapter.updateData(contacts)
            if (contacts.isEmpty()) {
                txtAucunResultat.visibility = View.VISIBLE
            } else {
                txtAucunResultat.visibility = View.GONE
            }
        }

        edtRecherche.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.filterContacts(s?.toString() ?: "")
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun ouvrirDetailContact(idContact: Int) {
        val detailFragment = DetailContactFragment.newInstance(idContact)
        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fragment_container, detailFragment)
            addToBackStack(null)
        }
    }
}
