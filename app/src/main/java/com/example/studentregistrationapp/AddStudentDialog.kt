package com.example.studentregistrationapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddStudentDialog(
    state: StudentState,
    onEvent: (StudentEvent) -> Unit,
    modifier: Modifier = Modifier,
    showDialog: Boolean,
    onCloseDialog: (Boolean) -> Unit
) {

    if(showDialog) {

        AlertDialog(
            modifier = modifier,
            onDismissRequest = {
                onEvent(StudentEvent.HideDialog)
                onCloseDialog(false)
            },
            title = { Text(text = "Add Student") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextField(
                        value = state.firstName,
                        onValueChange = {
                            onEvent(StudentEvent.SetFirstName(it))
                        },
                        placeholder = {
                            Text(text = "First name")
                        }
                    )
                    TextField(
                        value = state.lastName,
                        onValueChange = {
                            onEvent(StudentEvent.SetLastName(it))
                        },
                        placeholder = {
                            Text(text = "Last name")
                        }
                    )
                    TextField(
                        value = state.course,
                        onValueChange = {
                            onEvent(StudentEvent.SetCourse(it))
                        },
                        placeholder = {
                            Text(text = "Course")
                        }
                    )
                    TextField(
                        value = state.faculty,
                        onValueChange = {
                            onEvent(StudentEvent.SetFaculty(it))
                        },
                        placeholder = {
                            Text(text = "Faculty")
                        }
                    )
                    TextField(
                        value = state.location,
                        onValueChange = {
                            onEvent(StudentEvent.SetLocation(it))
                        },
                        placeholder = {
                            Text(text = "Location")
                        }
                    )

                }
            },
            confirmButton = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Button(onClick = {
                        onEvent(StudentEvent.SaveStudentProfile)
                        onCloseDialog(false)
                    }) {
                        Text(text = "Save Student")
                    }
                }
            }
        )
    }
    }
