package com.example.studentregistrationapp

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


@Composable
fun Navigation(studentDatabase: StudentDatabase)
{
    val navController = rememberNavController()
    val context = LocalContext.current
    val onBoardingSharedPref = OnBoardingSharedPref(context = context )

    val studentViewModel = viewModel <StudentViewModel> ( factory =
        StudentViewModelFactory(studentDatabase.dao)
    )

    val adminViewModel = viewModel<AdminViewModel>( factory =
        AdminViewModelFactory(sharedPref = onBoardingSharedPref)
    )




    NavHost(
        navController = navController,
        startDestination = Screens.AdminLoginScreen.route
    ) {
        composable(
            route = Screens.AdminLoginScreen.route

        ) {
            AdminLoginScreen(
                navController = navController
                , viewModel = adminViewModel
            )
        }
        composable(
            route= Screens.StudentRegistrationScreen.route
        ) {
            StudentRegistrationScreen(
                navController = navController,
                onEvent = {
                    if(it is StudentEvent.SetImageUri) {
                        studentViewModel.onEvent(
                            StudentEvent.SetImageUri(it.imageUri)
                        )
                    }
                    if(it is StudentEvent.SaveStudentImage) {
                        studentViewModel.onEvent(
                            StudentEvent.SaveStudentImage
                        )
                    }
                    if(it is StudentEvent.SetStudentToUpdate) {
                        studentViewModel.onEvent(
                            StudentEvent.SetStudentToUpdate(it.student)
                        )
                    }
                    if(it is StudentEvent.BlacklistStudents) {
                        studentViewModel.onEvent(
                            StudentEvent.BlacklistStudents(it.student))
                    }

                    if(it is StudentEvent.SortStudents) {
                        studentViewModel.onEvent(
                            StudentEvent.SortStudents(it.sortType))
                    }
                    if(it is StudentEvent.SaveStudentProfile) {
                        studentViewModel.onEvent(
                            StudentEvent.SaveStudentProfile)
                    }

                    if(it is StudentEvent.SetFirstName) {
                        studentViewModel.onEvent(
                            StudentEvent.SetFirstName(it.firstName))
                    }

                    if(it is StudentEvent.SetLastName) {
                        studentViewModel.onEvent(
                            StudentEvent.SetLastName(it.lastName))
                    }

                    if(it is StudentEvent.SetCourse) {
                        studentViewModel.onEvent(
                            StudentEvent.SetCourse(it.course))
                    }

                    if(it is StudentEvent.SetFaculty) {
                        studentViewModel.onEvent(
                            StudentEvent.SetFaculty(it.faculty))
                    }

                    if(it is StudentEvent.SetLocation) {
                        studentViewModel.onEvent(
                            StudentEvent.SetLocation(it.location))
                    }

                },
         studentViewModel = studentViewModel
            )
        }
        composable(
            route = Screens.TakePicture.route
        ) {
            TakePicture(navController=navController, viewModel = studentViewModel)
        }
        composable(
            route = Screens.SearchStudents.route
        ) {
            SearchStudent(navController = navController,
                viewModel = studentViewModel)
        }
    }}

class OnBoardingSharedPref(context: Context) {
    private val sharedPref = context.getSharedPreferences("ONBOARDING_STORE", Context.MODE_PRIVATE)
    private val editor = sharedPref.edit()

    fun updateSharedPref(password: String) {
        editor.putString("PASSWORD", password).apply()
    }

    fun getSharedPref(): String? {
        return sharedPref.getString("PASSWORD", "")
    }


}

