package app.beer.kinozver.utils

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class AppValueEventListener(val onSuccess: (snapshot: DataSnapshot) -> Unit) : ValueEventListener {

    override fun onCancelled(error: DatabaseError) {

    }

    override fun onDataChange(snapshot: DataSnapshot) {
        onSuccess(snapshot)
    }

}