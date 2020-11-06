package app.beer.kinozver.ui.fragments.account.change

import app.beer.kinozver.R
import app.beer.kinozver.database.*
import app.beer.kinozver.utils.*
import kotlinx.android.synthetic.main.fragment_change_email.*
import java.util.regex.Pattern

class ChangeEmailFragment : BaseChangeFragment(R.layout.fragment_change_email, USER.email) {

    override fun change() {
        val email = new_email_text_input_layout?.editText?.text.toString()
        if (email.isNotEmpty()) {
            if (isEmailValid(email)) {
                REF_DATABASE_ROOT.child(NODE_EMAIL)
                    .addListenerForSingleValueEvent(AppValueEventListener() {
                        if (it.hasChild(email.substring(0, email.length - 5))) {
                            showToast(getString(R.string.email_is_exists_label))
                        } else {
                            REF_DATABASE_ROOT.child(NODE_EMAIL)
                                .child(email.substring(0, email.length - 5))
                                .setValue(CURRENT_UID)
                                .addOnSuccessListener {
                                    REF_DATABASE_ROOT.child(NODE_USERS)
                                        .child(CURRENT_UID)
                                        .child(CHILD_EMAIL)
                                        .setValue(email)
                                        .addOnSuccessListener {
                                            showToast(getString(R.string.email_change_successful))
                                            APP_ACTIVITY.supportFragmentManager.popBackStack()
                                        }
                                        .addOnFailureListener {
                                            showToast(getString(R.string.error_some_went).format(it.message))
                                        }
                                }
                                .addOnFailureListener { showToast(it.message.toString()) }
                        }
                    })
            } else {
                new_email_text_input_layout.error = getString(R.string.error_input_valid_email)
            }
        } else {
            new_email_text_input_layout.error = getString(R.string.error_input_new_email)
        }
    }

}