package com.eliasasskali.tfg.android.ui.features.chats

import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

sealed class ChatsResponse
data class OnSuccessChats(val querySnapshot: QuerySnapshot?): ChatsResponse()
data class OnErrorChats(val exception: FirebaseFirestoreException?): ChatsResponse()