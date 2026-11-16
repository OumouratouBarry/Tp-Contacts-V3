package com.example.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ContactViewModel : ViewModel() {

    private val _allContacts = mutableListOf(
        Contact(1, "Barry", "Oumouratou", "22123456", "oumouratou@gmail.com", "photo_barry", 9.5370, -13.6785),
        Contact(2, "Diallo", "Fatoumata", "22345678", "fatoumata@gmail.com", "photo_fatoumata", 14.6937, -17.4441),
        Contact(3, "Camara", "Mamadou", "23456789", "mamadou@gmail.com", "photo_mamadou", 5.3600, -4.0083),
        Contact(4, "Sow", "Aminata", "24567890", "aminata@gmail.com", "photo_aminata", null, null),
        Contact(5, "Bah", "Ibrahima", "25678901", "ibrahima@gmail.com", "photo_ibrahima", 48.8566, 2.3522)
    )

    private val _displayedContacts = MutableLiveData<List<Contact>>(_allContacts.toList())
    val displayedContacts: LiveData<List<Contact>> get() = _displayedContacts

    private var currentFilter: String = ""

    fun getContactById(id: Int): Contact? {
        return _allContacts.find { it.id == id }
    }

    fun updateContact(updatedContact: Contact) {
        val index = _allContacts.indexOfFirst { it.id == updatedContact.id }
        if (index != -1) {
            _allContacts[index] = updatedContact
            applyFilter(currentFilter)
        }
    }

    fun filterContacts(query: String) {
        currentFilter = query
        applyFilter(query)
    }

    private fun applyFilter(query: String) {
        val trimmedQuery = query.trim().lowercase()
        if (trimmedQuery.isEmpty()) {
            _displayedContacts.value = _allContacts.toList()
        } else {
            _displayedContacts.value = _allContacts.filter { contact ->
                contact.nom.lowercase().contains(trimmedQuery) ||
                        contact.prenom.lowercase().contains(trimmedQuery) ||
                        contact.telephone.lowercase().contains(trimmedQuery)
            }
        }
    }
}
