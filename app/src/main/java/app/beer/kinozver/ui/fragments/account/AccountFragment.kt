package app.beer.kinozver.ui.fragments.account

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.view.*
import app.beer.kinozver.R
import app.beer.kinozver.database.*
import app.beer.kinozver.ui.fragments.BaseFragment
import app.beer.kinozver.ui.fragments.account.change.ChangeEmailFragment
import app.beer.kinozver.ui.fragments.account.change.ChangeNameFragment
import app.beer.kinozver.utils.*
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_account.*

class AccountFragment : BaseFragment(R.layout.fragment_account) {

    override fun onStart() {
        super.onStart()
        setHasOptionsMenu(true)
        init()
    }

    private fun init() {
        val userName = if (USER.userName == "null")
            CURRENT_UID
        else
            USER.userName
        account_user_name.text = userName
        account_user_email.text = USER.email
        account_user_name_change.text = userName
        account_email_change.text = USER.email
        account_user_photo.downloadAndSetImage(
            if (USER.avatarUrl == "empty")
                "https://yt3.ggpht.com/a/AATXAJxRvoO4IiBRst4Aas-nUOlKcCufYKGxPEIGTPz7tg=s900-c-k-c0xffffffff-no-rj-mo"
            else
                USER.avatarUrl
        )

        account_change_user_name_btn.setOnClickListener { replaceFragment(ChangeNameFragment(), true) }
        account_change_email_btn.setOnClickListener { replaceFragment(ChangeEmailFragment(), true) }
        account_change_photo_user_btn.setOnClickListener {
            changePhotoUser()
        }
    }

    private fun changePhotoUser() {
        CropImage.activity()
            .setAspectRatio(1, 1)
            .setRequestedSize(300, 300)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(APP_ACTIVITY, this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.account_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.account_menu_sign_out) {
            AUTH.signOut()
            restartActivity()
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val uri = CropImage.getActivityResult(data).uri
            val path = REF_STORAGE_ROOT.child(FOLDER_USER_IMAGES)
                .child(CURRENT_UID)
            putImageToStorage(uri, path) {
                getUrlFromStorage(path) {
                    putUrlToDatabase(it) {
                        USER.avatarUrl = it
                        account_user_photo.downloadAndSetImage(it)
                        showToast(getString(R.string.photo_user_change_successful))
                    }
                }
            }
        }
    }

}