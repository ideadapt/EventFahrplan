package info.metadude.android.eventfahrplan.database.extensions

import android.support.test.runner.AndroidJUnit4
import info.metadude.android.eventfahrplan.database.contract.FahrplanContract.HighlightsTable.Columns.EVENT_ID
import info.metadude.android.eventfahrplan.database.contract.FahrplanContract.HighlightsTable.Columns.HIGHLIGHT
import info.metadude.android.eventfahrplan.database.models.Highlight
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HighlightExtensionsTest {

    @Test
    fun toContentValues() {
        val highlight = Highlight(
                eventId = 2342,
                isHighlight = true
        )
        val values = highlight.toContentValues()
        assertThat(values.getAsInteger(EVENT_ID)).isEqualTo(2342)
        assertThat(values.getAsBoolean(HIGHLIGHT)).isEqualTo(true)
    }

}
