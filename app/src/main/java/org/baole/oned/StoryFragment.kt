package org.baole.oned

import android.content.Intent
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import org.baole.oned.model.Story
import org.baole.oned.util.AppState
import org.baole.oned.util.FirestoreUtil

open class StoryFragment : Fragment() {
    var mFirebaseUser: FirebaseUser? = null
    lateinit var mFirestore: FirebaseFirestore
    var mQuery: Query? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
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
        val lastStory = AppState.get(context!!).getLastStoryTimestamp()
        return DateUtils.isToday(lastStory)
    }

    fun newStory(storyId: String = "") {
        startActivity(Intent(activity, StoryEditorActivity::class.java).putExtra(FirestoreUtil.FIELD_ID, storyId))
    }


    companion object {
        const val LIMIT = 50L
        val TAG = StoryFragment::class.java.simpleName
    }
}