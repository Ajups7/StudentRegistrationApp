package com.example.studentregistrationapp

import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream

class StudentViewModel(
    private val dao: StudentDao
): ViewModel() {

    private val _bitmaps = MutableStateFlow<Bitmap?>(null)
    val bitmaps = _bitmaps.asStateFlow()

    fun onTakePhoto(bitmap: Bitmap) {
        _bitmaps.value = bitmap
    }
    private val imageUrl = MutableStateFlow("")

    private  val _sortType = MutableStateFlow(SortType.LAST_NAME)

    var studentEdit  = MutableStateFlow<Student?>(null)

    private val searchQuery = MutableStateFlow("")
    fun updateSearchQuery(string: String){
        searchQuery.update { string }
    }
    data class SearchStudentState(
        val studentList: List<Student> = emptyList()
    )
    private val studentSearchState = MutableStateFlow(SearchStudentState())

    var searchStudentListState = searchQuery.flatMapLatest {
        if (searchQuery.value.isNotEmpty()) {

            val currentStudent = _students.value.filter { student ->
                student.course.contains(it, true) || student.faculty.contains(it, true)
            }

            studentSearchState.update {
                it.copy(currentStudent)
            }
        }
        else {
            studentSearchState.update {
                it.copy(emptyList())
            }
        }
            studentSearchState
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), SearchStudentState())
    private set


    private val _students = _sortType
        .flatMapLatest { sortType ->
            when(sortType) {
                SortType.LAST_NAME -> dao.getStudentOrderedByLastName()
                SortType.COURSE -> dao.getStudentOrderedBycourse()
                SortType.STUDENT_ID -> dao.getStudentOrderedBystudentID()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(StudentState())
    val state = combine(_state, _sortType, _students) { state, sortType, students ->
        Log.d("girl", _students.value.toString())
        state.copy(
            students = students,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StudentState())

    fun onEvent(event: StudentEvent) {
        when(event) {
            is StudentEvent.ShareStudentData -> {
                exportToExcel(state.value.students, generateFilePath("StudentData.xlsx", Environment.DIRECTORY_DOWNLOADS))
            }
            is StudentEvent.SetStudentToUpdate -> {
                studentEdit.value = event.student
            }
            is StudentEvent.CaptureStudents -> TODO()
            is StudentEvent.SaveStudentImage -> {
                studentEdit.value?.let {
                val imageUpdate = it.copy(imageurl = imageUrl.value)
//                Log.d("boys", "imageUpdate = ${imageUpdate}")
                viewModelScope.launch {
                    dao.upsertStudent(imageUpdate)
                    Log.d("boys", "saveImage")
                }

                }


            }
            is StudentEvent.SetImageUri -> {
                imageUrl.value = event.imageUri
                Log.d("boys", "imageurl = ${imageUrl.value}")
                Log.d("boys", "eventUrl = ${event.imageUri}")

            }
            is StudentEvent.BlacklistStudents -> {
                val student = event.student
                val blacklistUpdate = student.copy(isBlacklisted = !student.isBlacklisted)
                viewModelScope.launch {
                    dao.upsertStudent(blacklistUpdate)
                }
            }
            StudentEvent.SaveStudentProfile -> {
                val firstName = state.value.firstName
                val lastName = state.value.lastName
                val location = state.value.location
                val faculty = state.value.faculty
                val course = state.value.course

                if (firstName.isBlank() || lastName.isBlank() || location.isBlank() || faculty.isBlank() || course.isBlank()) {
//                    Log.d("boy", "one of the parameters is null")
                    return
                }

                val student = Student(
                    firstName = firstName,
                    lastName = lastName,
                    location = location,
                    faculty = faculty,
                    course = course
                )
                viewModelScope.launch {
                    dao.upsertStudent(student)
                }
//                Log.d("boy", student.toString())
//
                _state.update {it.copy(
                    firstName = "",
                    lastName = "",
                    location = "",
                    faculty = "",
                    course = ""
                )}
            }

            StudentEvent.HideDialog -> {
                _state.update { it.copy(
                    isAddingStudent = false
                ) }
            }
            StudentEvent.ShowDialog -> {
                _state.update { it.copy(
                    isAddingStudent = true
                ) }
            }
            is StudentEvent.SetCourse -> {
                _state.update { it.copy(
                    course = event.course
                ) }
            }
            is StudentEvent.SetFaculty -> {
                _state.update { it.copy(
                    faculty = event.faculty
                ) }
            }
            is StudentEvent.SetFirstName -> {
                _state.update { it.copy(
                    firstName = event.firstName
                ) }
            }
            is StudentEvent.SetLastName -> {
                _state.update { it.copy(
                    lastName = event.lastName
                ) }
            }
            is StudentEvent.SetLocation -> {
                _state.update { it.copy(
                    location = event.location
                ) }
            }
            is StudentEvent.SortStudents -> {
                _sortType.value = event.sortType
            }

        }
    }
}

class StudentViewModelFactory(private val dao: StudentDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return StudentViewModel(dao) as T
    }
}

private fun generateFilePath(
    fileName: String,
    directoryType: String
): String {
//Get the public directory for the specified type
val publicDirectory = Environment.getExternalStoragePublicDirectory (directoryType)
/// Create a subdirectory within the public directory
val subdirectory = File (publicDirectory,  "YourAppDirectoryName")
//Ensure that the subdirectory exists; create it if not
if (!subdirectory.exists()) {
    subdirectory.mkdirs()
}
/// Create a File object with the specified subdirectory and file name
val file = File(subdirectory, fileName)
/// Return the absolute path of the file
return file.absolutePath
}


private fun exportToExcel(students: List<Student>, filePath: String) {
    val workbook: Workbook = XSSFWorkbook()
    val sheet: Sheet = workbook.createSheet(  "Sheetl")
//    / Create header PoW
    val headerRow: Row = sheet.createRow(  0)
    headerRow.createCell(  0).setCellValue("FirstName")
    headerRow.createCell(1).setCellValue("LastName")
    headerRow.createCell(  2).setCellValue("Course")
    headerRow. createCell(  3) . setCellValue("Faculty")
    headerRow.createCell( 4).setCellValue("Location")
    headerRow.createCell( 5).setCellValue ("StudentID")

//    Add other headers as needed
//    Populate data
            for ((index, obj) in students.withIndex()) {
                val dataRow: Row = sheet.createRow(index +1)
                dataRow.createCell( 0).setCellValue (obj.firstName)
                dataRow.createCell( 1).setCellValue (obj.lastName)
                dataRow.createCell(2).setCellValue(obj.course)
                dataRow.createCell( 3).setCellValue(obj.faculty)
                dataRow.createCell(4).setCellValue(obj.location)
                dataRow.createCell( 5).setCellValue(obj.studentID.toString())
//                Add other data as needed
//                / Write to file
                FileOutputStream(filePath).use { fileOut -> workbook.write(fileOut) }
//                / CLose workbook to release resources
                workbook.close()
            }}