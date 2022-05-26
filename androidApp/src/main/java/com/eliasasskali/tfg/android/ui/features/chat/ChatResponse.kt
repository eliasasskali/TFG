package com.eliasasskali.tfg.android.ui.features.chat

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException

sealed class ChatResponse
data class OnSuccessChat(val querySnapshot: DocumentSnapshot?): ChatResponse()
data class OnErrorChat(val exception: FirebaseFirestoreException?): ChatResponse()