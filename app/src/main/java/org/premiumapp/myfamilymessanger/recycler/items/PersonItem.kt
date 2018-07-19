package org.premiumapp.myfamilymessanger.recycler.items

import android.content.Context
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_person.*
import org.premiumapp.myfamilymessanger.R
import org.premiumapp.myfamilymessanger.glide.GlideApp
import org.premiumapp.myfamilymessanger.model.User
import org.premiumapp.myfamilymessanger.utils.StorageUtil

class PersonItem(val person: User,
                 val userId: String,
                 private val ctx: Context)
    : Item() {
    override fun getLayout(): Int = R.layout.item_person

    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.textView_name.text = person.name
        viewHolder.textView_bio.text = person.bio

        if (person.profilePicturePath != null) {
            GlideApp.with(ctx)
                    .load(StorageUtil.pathToReference(person.profilePicturePath))
                    .placeholder(R.drawable.ic_account_box_black_24dp)
                    .into(viewHolder.imageView_profile_picture)
        }
    }
}