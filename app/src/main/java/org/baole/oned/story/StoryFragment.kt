package org.baole.oned.story

import android.content.Intent
import android.os.Bundle
import android.text.format.DateUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import org.baole.oned.MainActivity
import org.baole.oned.StoryEditorActivity
import org.baole.oned.model.Story
import org.baole.oned.util.AppState
import org.baole.oned.util.FirestoreUtil

open class StoryFragment : Fragment() {
    lateinit var mViewModel: StoryViewModel
    var mFirebaseUser: FirebaseUser? = null
    lateinit var mFirestore: FirebaseFirestore
    lateinit var mQuery: Query

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
        mViewModel = ViewModelProvider(this).get(StoryViewModel::class.java)
        initFirestore()
    }
    fun initFirestore() {
        mFirestore = FirebaseFirestore.getInstance()
        mFirebaseUser = FirebaseAuth.getInstance().currentUser
        mQuery = FirestoreUtil.story(mFirestore, mFirebaseUser)
                .orderBy(Story.FIELD_DAY, Query.Direction.DESCENDING)
                .limit(LIMIT)
    }

    fun main(): MainActivity? {
        val parent = activity
        if (parent is MainActivity) return parent
        else return null
    }

    fun hasTodayStory (): Boolean {
        return AppState.get(context!!).hasTodayStory()
    }

    fun editStory(storyId: String = "") {
        startActivity(Intent(activity, StoryEditorActivity::class.java).putExtra(FirestoreUtil.FIELD_ID, storyId))
    }

    companion object {
        const val LIMIT = 5L
        val TAG = StoryFragment::class.java.simpleName
    }
}