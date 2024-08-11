import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun SecondApp() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight(0.5f),

            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(
                modifier = Modifier
                    .width(100.dp)
                    .height(80.dp),
                color = MaterialTheme.colors.primary
            ) {  }

            Surface(
                modifier = Modifier
                    .width(100.dp)
                    .height(80.dp),
                color = MaterialTheme.colors.primary
            ) {  }

            Surface(
                modifier = Modifier
                    .width(100.dp)
                    .height(80.dp),
                color = MaterialTheme.colors.primary
            ) {  }
        }
    }
}