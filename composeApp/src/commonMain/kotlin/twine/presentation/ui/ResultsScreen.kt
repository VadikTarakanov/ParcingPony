package twine.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import twine.presentation.components.results.ResultsComponent
import twine.presentation.model.TypeTraining
import ui.TextSecondary

@Composable
fun ResultsScreen(component: ResultsComponent) {
    //TODO REFACTOR TYPE!
    val typeTraining = remember {
        mutableStateOf(TypeTraining.SIDE_SPLIT)
    }

    val listResult by component.getResults(typeTraining.value).collectAsState(emptyList())

    Column {
        Text(
            text = "Progress\ntraining ",
            style = MaterialTheme.typography.h1,
            modifier = Modifier.padding(16.dp)
        )
        ChipsTrainingSection(
            typeTraining = typeTraining.value,
            onItemClick = {
                typeTraining.value = it
            }
        )
        if (listResult.isNotEmpty()) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(
                    items = listResult,
                    key = { item -> item.id ?: 0 }
                ) { item ->
                    ResultItem(
                        result = item.progress,
                        dateTraining = item.dateTraining,
                        onDelete = {
                            component.deleteResult( //TODO REFACTOR TYPE!
                                typeTraining = TypeTraining.SIDE_SPLIT,
                                item = item
                            )
                        }
                    )
                }
            }
        } else {
            // here would be your results
        }
    }
}

@Composable
fun ResultItem(
    result: Int,
    dateTraining: String,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Column(modifier = Modifier.weight(0.8f)) {
            Text(
                text = "Progress ${result}%",
                style = MaterialTheme.typography.body1
            )
            Text(
                text = "date of training $dateTraining",
                style = MaterialTheme.typography.body2,
                color = TextSecondary
            )
        }
        IconButton(
            onClick = onDelete,
            modifier = Modifier.weight(0.2f)
        ) {
            Icon(
                Icons.Filled.Delete,
                contentDescription = "Delete",
                tint = Color.Red
            )
        }
    }
}