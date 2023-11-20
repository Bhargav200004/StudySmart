package com.example.studysmart.ui.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.studysmart.R
import com.example.studysmart.ui.components.CountCard
import com.example.studysmart.ui.components.SubjectCard
import com.example.studysmart.ui.domain.model.Subject

@Composable
fun DashboardScreen() {

    val subject = listOf(
        Subject(name = "Maths", goalHours = "20", colors = Subject.subjectCardColor[0]),
        Subject(name = "Physics", goalHours = "5", colors = Subject.subjectCardColor[1]),
        Subject(name = "Chemistry", goalHours = "20", colors = Subject.subjectCardColor[2]),
        Subject(name = "Bio", goalHours = "60", colors = Subject.subjectCardColor[3]),
        Subject(name = "DSA", goalHours = "100", colors = Subject.subjectCardColor[4])
    )

    Scaffold(
        topBar = { DashboardScreenTopBar() }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                CountCardsSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    subjectCount = 5,
                    studiedHourCount = "20",
                    goalStudyHourCount = "40"
                )
            }
            item {
                SubjectCardSection(
                    modifier = Modifier.fillMaxWidth(),
                    subjectList = subject
                )
            }
            item {
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 48.dp, vertical = 20.dp)
                ) {
                        Text(text = "Start Study Session")
                }
            }

            item {
                UpcomingTaskList(modifier = Modifier.fillMaxWidth())
            }
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardScreenTopBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Study Smart",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    )
}

@Composable
private fun CountCardsSection(
    modifier: Modifier = Modifier,
    subjectCount: Int = 0,
    studiedHourCount: String = "0",
    goalStudyHourCount: String = "0"
) {
    Row(modifier = modifier) {
        CountCard(
            modifier = Modifier
                .weight(1f),
            headline = "Subject Count",
            count = "$subjectCount"
        )
        Spacer(modifier = Modifier.width(10.dp))
        CountCard(
            modifier = Modifier
                .weight(1f),
            headline = "Studied Hours",
            count = studiedHourCount
        )
        Spacer(modifier = Modifier.width(10.dp))
        CountCard(
            modifier = Modifier
                .weight(1f),
            headline = "Goal study Hour",
            count = goalStudyHourCount
        )
    }
}


@Composable
private fun SubjectCardSection(
    modifier: Modifier,
    subjectList: List<Subject>,
    emptyText : String = "Your Subject is empty. \n Press + to add the Subject"
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "SUBJECT",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(start = 12.dp)
            )
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Subject"
                )
            }
        }
        if (subjectList.isEmpty()) {
            Image(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally),
                painter = painterResource(id = R.drawable.img_books),
                contentDescription = emptyText
            )
            Text(
                text = emptyText,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(start = 12.dp , end = 12.dp)
        ){
            items(subjectList){subject->
                SubjectCard(
                    subjectName = subject.name,
                    gradientColor = subject.colors,
                    onClick = {}
                )
            }
        }

    }
}


//UpcomingTaskView
@Composable
fun UpcomingTaskList(modifier: Modifier) {
    Column(modifier = modifier) {
        Text(
            text = "UpcomingTask",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(12.dp)
        )
    }
}

