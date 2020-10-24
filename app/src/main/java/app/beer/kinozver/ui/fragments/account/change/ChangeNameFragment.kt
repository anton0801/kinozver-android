package app.beer.kinozver.ui.fragments.account.change

import app.beer.kinozver.R
import app.beer.kinozver.database.*
import app.beer.kinozver.ui.fragments.account.AccountFragment
import app.beer.kinozver.utils.APP_ACTIVITY
import app.beer.kinozver.utils.replaceFragment
import app.beer.kinozver.utils.showToast
import kotlinx.android.synthetic.main.fragment_change_name.*

class ChangeNameFragment : BaseChangeFragment(
    R.layout.fragment_change_name,
    if (USER.userName.isEmpty()) APP_ACTIVITY.getString(R.string.change_name_label) else USER.userName
) {

    override fun change() {
        val newName = new_name_text_input_layout.editText?.text.toString()
        if (newName.isNotEmpty()) {
            val hasMap = HashMap<String, Any>()
            hasMap[CHILD_FULL_NAME] = newName
            REF_DATABASE_ROOT.child(NODE_USERS)
                .child(CURRENT_UID)
                .updateChildren(hasMap)
                .addOnSuccessListener {
                    showToast(getString(R.string.name_change_successful))
                    replaceFragment(AccountFragment())
                }
                .addOnFailureListener {
                    showToast("Что-то пошло не так! ${it.message}")
                }
        } else {
            new_name_text_input_layout.error = getString(R.string.error_input_new_name)
        }
    }

}