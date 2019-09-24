package com.br.vobis.model

import com.google.firebase.firestore.DocumentReference

interface IEntity {
    var key: DocumentReference?
}
