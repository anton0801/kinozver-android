package app.beer.kinozver.ui.fragments.register

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.util.Log
import android.view.View
import app.beer.kinozver.R
import app.beer.kinozver.database.*
import app.beer.kinozver.ui.fragments.BaseFragment
import app.beer.kinozver.utils.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
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

    override fun onStart() {
        super.onStart()
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
        val intent = googleSignInClient.signInIntent
        startActivityForResult(intent, GOOGLE_SIGN_IN_REQUEST_CODE)
    }

    private fun loginOrRegisterEmail() {
        val email = text_input_layout_email.editText?.text?.toString()
        val password = text_input_layout_password.editText?.text?.toString()
        if (email != null && password != null) {
            REF_DATABASE_ROOT.child(NODE_EMAIL)
                .addValueEventListener(AppValueEventListener() {
                    if (it.hasChild(email.substring(0, email.length - 5))) {
                        loginEmail(email, password)
                    } else {
                        registerEmail(email, password)
                    }
                })
        } else {
            showToast("Поля должны быть заполнены")
        }
    }

    private fun registerEmail(email: String, password: String) {
        if (email.isNotEmpty() && isEmailValid(email)) {
            if (password.length >= 6) {
                AUTH.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        writeUserData(password)
                        restartActivity()
                    }
                    .addOnFailureListener {
                        showToast("Что-то пошло не так! ${it.message}")
                    }
            } else {
                text_input_layout_password.error = "Пароль должен быть больше 6 символов"
            }
        } else if (!isEmailValid(email)) {
            text_input_layout_email.error = getString(R.string.error_input_valid_email)
        } else {
            text_input_layout_email.error = "Введите ваш email"
        }
    }

    private fun loginEmail(email: String, password: String) {
        if (email == null || password == null) {
            showToast("Поля должны быть заполнены")
            return
        }
        if (email.isNotEmpty() && isEmailValid(email)) {
            if (password.length >= 6) {
                AUTH.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        restartActivity()
                        showToast(getString(R.string.successful_login_label))
                    }
                    .addOnFailureListener { showToast(it.message.toString()) }
            } else {
                text_input_layout_password.error = "Пароль должен быть больше 6 символов"
            }
        } else if (!isEmailValid(email)) {
            text_input_layout_email.error = getString(R.string.error_input_valid_email)
        } else {
            text_input_layout_email.error = "Введите ваш email"
        }
    }

    private fun login(authCredential: AuthCredential) {
        AUTH.signInWithCredential(authCredential)
            .addOnSuccessListener {
                writeUserData()
                restartActivity()
                showToast(getString(R.string.successful_login_label))
            }
            .addOnFailureListener {
                showToast("Что-то погло не так! ${it.message}")
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN_REQUEST_CODE && resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    signInWithGoogle(account.idToken)
                }
            } catch (e: ApiException) {
                showToast(e.message.toString())
                Log.d("GoogleSignIn", e.message.toString())
            }
        }
    }

    private fun signInWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        login(credential)
    }

    companion object {
        const val GOOGLE_SIGN_IN_REQUEST_CODE = 123
    }

}