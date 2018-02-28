package com.chronos.agenda.fcm

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService


/**
 * Created by John Vanderson M L on 27/02/2018.
 */
class AgendaInstanceIDService : FirebaseInstanceIdService() {

    override fun onTokenRefresh() {
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.d("Token fireBase", "Refreshed token: ${refreshedToken!!}" )

        enviarTokenServidor(refreshedToken)

    }

    private fun enviarTokenServidor(refreshedToken: String) {

    }
}