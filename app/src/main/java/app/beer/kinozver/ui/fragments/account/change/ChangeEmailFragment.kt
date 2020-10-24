package app.beer.kinozver.ui.fragments.account.change

import app.beer.kinozver.R
import app.beer.kinozver.database.*
import app.beer.kinozver.utils.APP_ACTIVITY
import app.beer.kinozver.utils.replaceFragment
import app.beer.kinozver.utils.showToast
import kotlinx.android.synthetic.main.fragment_change_email.*
import java.util.regex.Pattern

class ChangeEmailFragment : BaseChangeFragment(R.layout.fragment_change_email, USER.email) {

    override fun change() {
        val email = new_email_text_input_layout?.editText?.text.toString()
        if (email.isNotEmpty()) {
            if (isEmailValid(email)) {
                val hashMap = hashMapOf<String, Any>()
                hashMap[CHILD_EMAIL] = email
                REF_DATABASE_ROOT.child(NODE_USERS)
                    .child(CURRENT_UID)
                    .updateChildren(hashMap)
                    .addOnSuccessListener {
                        showToast(getString(R.string.email_change_successful))
                        APP_ACTIVITY.supportFragmentManager.popBackStack()
                    }
                    .addOnFailureListener {
                        showToast(getString(R.string.error_some_went).format(it.message))
                    }
            } else {
                new_email_text_input_layout.error = getString(R.string.error_input_valid_email)
            }
        } else {
            new_email_text_input_layout.error = getString(R.string.error_input_new_email)
        }
    }

    private fun isEmailValid(email: String): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

}