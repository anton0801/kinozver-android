package app.beer.kinozver.ui.fragments.register

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.beer.kinozver.R
import app.beer.kinozver.database.AUTH
import app.beer.kinozver.database.writeUserData
import app.beer.kinozver.utils.APP_ACTIVITY
import app.beer.kinozver.utils.restartActivity
import app.beer.kinozver.utils.showToast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.AuthCredential
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : Fragment() {

    // google
    private lateinit var gso: GoogleSignInOptions
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onStart() {
        super.onStart()

        init()
    }

    private fun init() {
        APP_ACTIVITY.bottomNavigationView.visibility = View.GONE
        btn_email_login.setOnClickListener {
            registerEmail()
        }

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
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

    private fun registerEmail() {
        val email = text_input_layout_email.editText?.text?.toString()
        val password = text_input_layout_password.editText?.text?.toString()
        if (email == null || password == null) {
            showToast("Поля должны быть заполнены")
            return
        }
        if (email.isNotEmpty()) {
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
            } else {
            text_input_layout_email.error = "Введите ваш email"
        }
    }

    private fun login(authCredential: AuthCredential) {
        AUTH.signInWithCredential(authCredential)
            .addOnSuccessListener {
                writeUserData()
                restartActivity()
            }
            .addOnFailureListener {
                showToast("Что-то погло не так! ${it.message}")
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN_REQUEST_CODE && resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        }
    }

    companion object {
        const val GOOGLE_SIGN_IN_REQUEST_CODE = 123
    }

}