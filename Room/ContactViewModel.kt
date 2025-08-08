package com.vinayak.semicolon.securefolderhidefiles.Room

import android.app.Application
import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ContactViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ContactRepository
    val savedContacts: LiveData<List<ContactEntity>>

    init {
        val dao = ContactDatabase.getInstance(application).contactDao()
        repository = ContactRepository(dao)
        savedContacts = repository.allContacts
    }

    fun insertContacts(contacts: List<ContactEntity>) = viewModelScope.launch {
        repository.insertAll(contacts)
    }

    fun deleteContactById(contactId: String) = viewModelScope.launch {
        repository.deleteById(contactId)
    }

    fun deleteFromDevice(context: Context, contactId: String) {
        val contactUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, contactId)
        context.contentResolver.delete(contactUri, null, null)
    }

    fun insertContact(contact: ContactEntity) = viewModelScope.launch {
        repository.insertSingle(contact)
    }

}