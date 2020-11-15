import android.content.Context
import android.content.SharedPreferences

class Session(var context: Context?) {
    var preferences: SharedPreferences
    var editor: SharedPreferences.Editor
    fun setLoggedin(loggedin: Boolean) {
        editor.putBoolean("loggedInmode", loggedin)
        editor.commit()
    }

    fun loggedIn(): Boolean {
        return preferences.getBoolean("loggedInmode", false)
    }

    init {
        preferences = context!!.getSharedPreferences("myapp", Context.MODE_PRIVATE)
        editor = preferences.edit()
    }
}