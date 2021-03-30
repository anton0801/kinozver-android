package app.beer.kinozver.ui.fragments.register

import android.accounts.Account
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.util.Log
import android.view.View
import app.beer.kinozver.DEFAULT_USER_AVATAR_URL
import app.beer.kinozver.IS_AUTH_KEY
import app.beer.kinozver.R
import app.beer.kinozver.USER_ID_KEY
import app.beer.kinozver.database.*
import app.beer.kinozver.models.auth.AuthResponse
import app.beer.kinozver.ui.fragments.BaseFragment
import app.beer.kinozver.utils.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : BaseFragment(R.layout.fragment_register) {

    // google
    private lateinit var gso: GoogleSignInOptions
    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var sharedManager: SharedManager

    override fun onStart() {
        super.onStart()
        sharedManager = SharedManager()

        init()
    }

    private fun init() {
        APP_ACTIVITY.bottomNavigationView.visibility = View.GONE
        btn_email_login.setOnClickListener {
            loginOrRegisterEmail()
        }

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(APP_ACTIVITY, gso)

        google_login_btn.setOnClickListener {
            loginGoogle()
        }
    }

    private fun loginGoogle() {
        progressBar.visibility = View.VISIBLE
        val intent = googleSignInClient.signInIntent
        startActivityForResult(intent, GOOGLE_SIGN_IN_REQUEST_CODE)
    }

    private fun loginOrRegisterEmail() {
        val email = text_input_layout_email.editText?.text?.toString()
        val password = text_input_layout_password.editText?.text?.toString()
        if (email != null && password != null) {
            if (email.isNotEmpty() && isEmailValid(email)) {
                if (password.length >= 5) {
                    progressBar.visibility = View.VISIBLE
                    login(email, password)
                } else {
                    text_input_layout_password.error = getString(R.string.pasword_less_5_error)
                }
            } else {
                text_input_layout_email.error = getString(R.string.error_input_valid_email)
            }
        } else {
            showToast(getString(R.string.all_filds_done_error))
        }
    }

    private fun login(
        email: String = "",
        password: String = "",
        userSocialId: String = "",
        name: String = "",
        photoUrl: String = ""
    ) {
        val n = if (name != "") name else email
        val avatar = if (photoUrl != "") photoUrl else DEFAULT_USER_AVATAR_URL
        APP.getMovieApi().auth(email, password, userSocialId, n, avatar)
            .enqueue(AppRetrofitCallback<AuthResponse> { _, response ->
                val authResponse = response.body()
                if (authResponse != null) {
                    if (authResponse.message == "Вы удачно зарегистрировались" || authResponse.message == "Вы удачно вошли") {
                        sharedManager.putBoolean(IS_AUTH_KEY, true)
                        sharedManager.putInt(USER_ID_KEY, authResponse.id)
                        restartActivity()
                    } else {
                        showToast(authResponse.message)
                    }
                }
                progressBar.visibility = View.GONE
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN_REQUEST_CODE && resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    signInWithGoogle(account)
                }
            } catch (e: ApiException) {
                progressBar.visibility = View.GONE
                showToast(e.message.toString())
                Log.d("GoogleSignIn", e.message.toString())
            }
        }
    }

    private fun signInWithGoogle(account: GoogleSignInAccount) {
        login(
            account.email.toString(),
            "",
            account.id.toString(),
            account.displayName.toString(),
            account.photoUrl.toString()
        )
    }

    companion object {
        const val GOOGLE_SIGN_IN_REQUEST_CODE = 123
    }

}