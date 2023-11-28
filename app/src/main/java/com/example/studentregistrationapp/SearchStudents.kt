package com.example.studentregistrationapp

import android.icu.text.StringSearch
import android.view.SearchEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchStudent(
    navController: NavController,
    viewModel: StudentViewModel
) {


    var searchText by remember { mutableStateOf("") }
    val searchStatee by viewModel.searchStudentListState.collectAsState()



    val keyboardController = LocalSoftwareKeyboardController.current
    val textInputService = LocalTextInputService.current
    val context = LocalContext.current

    Surface(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize()
    ) {


        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.background)
                    .clickable { }

            ) {
                TextField(

                    value = searchText,
                    onValueChange = {
                        searchText = it
                        viewModel.updateSearchQuery(it)
                    },
                    placeholder = { Text(text = "Search") },
                    singleLine = true,

                    )


            }  
            
            Spacer(modifier = Modifier.height(15.dp))


            if(searchStatee.studentList.isNotEmpty()){


            LazyColumn {
                item {

                }

                items(searchStatee.studentList) {student->
                Row(modifier = Modifier.fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.primary)) {
                    Box(
                        modifier = Modifier

                            .background(color = Color.Blue, shape = CircleShape)
                            .size(70.dp)
                            .clip(
                                CircleShape
                            )) {
                        getBitmapFromFilePath(student.imageurl)?.let {
                            Image(
                                bitmap = it.asImageBitmap(),
                                contentDescription = null,
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(
                                        color = Color.Transparent,
                                        shape = CircleShape,
                                    )
                            )

                        }
                    }
                    Spacer(modifier = Modifier.width(18.dp))

                    Column {

                    Text(

                        text = "${student.lastName} ${student.firstName}",
                        fontSize = 18.sp
                    )

                    Text(

                        text = "${student.course}",
                        fontSize = 15.sp,
                    )
                    Text(

                        text = "${student.faculty}",
                        fontSize = 15.sp,
                    )
                    Text(

                        text = "${student.location}",
                        fontSize = 15.sp,
                    )
                    Text(
                        text = "${student.studentID}",
                        fontSize = 15.sp,
                    )
                    }
                }
            }
}} else {
    Text(modifier = Modifier.padding(20.dp),
        text = "search student by course or faculty"
    )

        }}}}


