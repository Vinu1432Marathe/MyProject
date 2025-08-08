package com.vinayak.semicolon.securefolderhidefiles.Room

import androidx.lifecycle.LiveData

class NoteRepository(private val noteDao: NoteDao) {
    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes()

    suspend fun insert(note: Note) = noteDao.insert(note)
    suspend fun update(note: Note) = noteDao.update(note)
    suspend fun delete(note: Note) = noteDao.delete(note)
}

//class ContactRepository(private val dao: ContactDao) {
//    suspend fun saveToDatabase(contacts: List<Contact>) {
//        val saved = contacts.map { SavedContact(it.id, it.name, it.number) }
//        dao.insertAll(saved)
//    }
//
//    suspend fun deleteFromDatabase(contacts: List<Contact>) {
//        val saved = contacts.map { SavedContact(it.id, it.name, it.number) }
//        dao.deleteContacts(saved)
//    }
//
//    suspend fun getAllSaved(): List<SavedContact> = dao.getAll()
//}

class ContactRepository(private val dao: ContactDao) {

//        suspend fun saveToDatabase(contacts: List<Contact>) {
//        val saved = contacts.map { SavedContact(it.id, it.name, it.number) }
//        dao.insertAll(saved)
//    }

    val allContacts: LiveData<List<ContactEntity>> = dao.getAllContacts()

    suspend fun insertAll(contacts: List<ContactEntity>) = dao.insertContacts(contacts)

    suspend fun deleteById(contactId: String) = dao.deleteById(contactId)

    suspend fun insertSingle(contact: ContactEntity) = dao.insertContact(contact)

}
