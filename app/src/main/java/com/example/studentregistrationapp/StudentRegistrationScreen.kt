package com.example.studentregistrationapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.example.studentregistrationapp.AddStudentDialog
import com.example.studentregistrationapp.PermissionHelper
import com.example.studentregistrationapp.Screens
import com.example.studentregistrationapp.SortType
import com.example.studentregistrationapp.StudentViewModel
import com.example.studentregistrationapp.StudentEvent
import com.example.studentregistrationapp.hasRequiredPermissions


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentRegistrationScreen(
    studentViewModel: StudentViewModel,
    navController: NavController,
    onEvent: (StudentEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val applicationContext = LocalContext.current
    val activityContext = applicationContext as ComponentActivity
    val imageState by studentViewModel.bitmaps.collectAsState()

    val context = LocalContext.current
    val studentScreenState by studentViewModel.state.collectAsState()

    var showDialog by remember {
        mutableStateOf(false)
    }



    val cameraLaunchResult =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { result ->
            Log.d("boys", result.toString())
//            if (result.resultCode == RESULT_OK) {
//                val uri = result.data?.data
//                Log.d("boys", uri.toString())
//                uri?.let { uri1->
//                studentEdit?.let {
//                    saveUriAsImageNote(uri=uri1, context=context, studentViewModel = studentViewModel,it )
//
//                }
//                } ?: Toast.makeText(context, "uri is empty", Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(context, "return without taking a picture", Toast.LENGTH_SHORT).show()
//            }

        }

    val scope = rememberCoroutineScope()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(StudentEvent.ShowDialog)
                showDialog = true
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add contact"
                )
            }
        }
    )
    { padding ->
//        if(state.isAddingStudent) {
//            AddStudentDialog(state = state, onEvent = onEvent, showDialog =)
//        }
        AddStudentDialog(onEvent = onEvent, showDialog = showDialog, onCloseDialog = {
            showDialog = it
        }, state = studentScreenState)


        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SortType.values().forEach { sortType ->
                        Row(
                            modifier = Modifier
                                .clickable {
                                    onEvent(StudentEvent.SortStudents(sortType))
                                },
                            verticalAlignment = CenterVertically
                        ) {
                            RadioButton(
                                selected = studentScreenState.sortType == sortType,
                                onClick = {
                                    onEvent(StudentEvent.SortStudents(sortType))
                                })
                            Text(text = sortType.name)

                        }
                        Icon(
                            imageVector = Icons.Default.NavigateNext,
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                navController.navigate(Screens.SearchStudents.route)
                            }

                            )
                    }
                }

            }

            items(studentScreenState.students) { student ->
                val isBlacklisted = student.isBlacklisted

                Column(modifier = Modifier
                    .border(1.dp, MaterialTheme.colorScheme.primary)
                    .padding(16.dp)) {
                    Box(
                        modifier = Modifier

                            .background(color = Color.Blue, shape = CircleShape)
                            .size(70.dp)
                            .clip(
                                CircleShape
                            )
                            .clickable {
                                if (!hasRequiredPermissions(applicationContext)) {
                                    ActivityCompat.requestPermissions(
                                        activityContext,
                                        PermissionHelper.CAMERAX_PERMISSIONS,
                                        0
                                    )
                                }
                                else{
                                    Log.i("box", "to start camera")

                                onEvent(StudentEvent.SetStudentToUpdate(student))
                                navController.navigate(Screens.TakePicture.route)
                                }

                            }) {
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


                        Row(
                            modifier = Modifier
                                .background(color = if (isBlacklisted) Color.Gray else Color.White)
                                .fillParentMaxWidth()
                        ) {

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(20.dp)
                            ) {

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
                            IconButton(onClick = {
                                onEvent(StudentEvent.BlacklistStudents(student))

                            }) {
                                Icon(

                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Blacklist Student"
                                )
                            }
                        }
                    }
                }
            }
        }
    }


//private fun createImageFile(context: Context,onFileCreated:(File)->Unit) {
//    val fileName = System.currentTimeMillis().toString() + ".jpg"
//    val fileDir = File(context.filesDir, "images")
//    if (!fileDir.exists()) fileDir.mkdir()
//    val file = File(fileDir, fileName)
//    onFileCreated(file)
//}
//
//fun saveBitmapToFile(bytes:ByteArray, filePath: String) {
//    val bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.size)
//    val outputStream = FileOutputStream(filePath)
//    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
//}
//
fun getBitmapFromFilePath(
    filePath: String,
): Bitmap? {
    return BitmapFactory.decodeFile(filePath)
}















