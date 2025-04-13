
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.newuplift.database.DatabaseHelper
import com.android.newuplift.fragments.QuotesViewModel
import com.android.newuplift.utility.QuoteType

class QuotesViewModelFactory(
    private val databaseHelper: DatabaseHelper,
    private val quoteType: QuoteType
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuotesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QuotesViewModel(databaseHelper, quoteType) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}