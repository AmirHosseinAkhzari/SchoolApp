package com.example.cote.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH

import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


data class ReqOtpNum(
    val number: String
)



data class ResOtp(
    val message : String ,
    val code : Int
)



data class ResCheckOtp(
    val message : String ,
    val code : Int ,
    val token : String
)

data class ReqCheckOtp(
    val number : String ,
    val code : String ,
    val role : String
)




data class total (
    val present : Int ,
    val lateness : Int ,
    val absent  : Int
)



data class Student(
    val _id : String ,
    val firstname : String ,
    val lastname : String ,
)

data class ResReadStudent(
    val message: String ,
    val code: Int ,
    val students : List<Student>
)

data class ReqAddAstin(
    val ownerId : String ,
    val uid : String
)

data class ResAddAstin(
    val message : String ,
    val code : Int ,
)

data class ReqReadAstin (
    val uid  : String
)

data class StudentFullData(
    val firstname: String ,
    val lastname: String ,
    val nationalid : String ,
    val number : String ,
    val birthday : String ,
    val ParentNumber : String ,
    val LocalNumber : String ,
    val className : String ,
    val classId : String
)

data class ResReadAstin(
    val code: Int ,
    val message: String ,
    val stu: StudentFullData
)

data class ResSendSms(
    val message : String ,
    val code : Int ,
)

data class stuAttendanceData(
    val id : String ,
    val fullname : String ,
    val className  : String ,
    val status : String ,
    val checkIn : String ,
    val userId : String ,
    val description : String
)


data class ResReadAttendance(
    val code : Int ,
    val data : List<stuAttendanceData>
)

data class ReqAttendanceChangeStatus(
    val id : String ,
    val status  : String ,
    val date : String
)

data class ResAttendanceChangeStatus(
    val message : String ,
    val code : Int ,
)

data class ResReadClass(
    val data: List<ClassModel> ,
    val code : Int ,
)

data class ClassModel(
    val _id : String ,
    val name : String
)

data class ReqDeleteClass(
    val id : String ,
)

data class ResDeleteClass(
    val message : String ,
    val code : Int ,
)

data class ReqChangeClassName(
    val id : String ,
    val name : String
)

data class ResChangeClassName(
    val message : String ,
    val code : Int ,
)

data class ResAddClass(
    val message : String ,
    val code : Int ,
)

data class ReqAddStudent(
    val firstname : String  ,
    val lastname  : String ,
    val birthday  : String ,
    val nationalid  : String ,
    val number  : String ,
    val ParentNumber  : String ,
    val LocalNumber  : String ,
    val classId  : String ,
    val role  : String ,

)

data class ResAddStudent(
    val message : String ,
    val code : Int ,
)

data class ResDeleteStudent(
    val message : String ,
    val code : Int ,
)

data class ResReadOneStudent(
    val data : StudentFullData ,
    val code : Int ,
)



data class ReqUpdateStudent(
    val id : String ,
    val updateData : ReqAddStudent,
)

data class ResUpdateStudent(
    val message : String ,
    val code : Int ,
)

data class ReqAddAdmin(
    val number : String ,
    val firstname : String,
    val lastname : String
)

data class ResAddAdmin(
    val message : String ,
    val code : Int ,
)

data class Admin(
    val _id : String ,
    val number : String ,
    val firstname : String,
    val lastname : String
)

data class ResReadAdmins(
    val code : Int ,
    val data : List<Admin>
)

data class ResDeleteAdmin(
    val message : String ,
    val code : Int ,
)



interface CoteApi {

    @POST("login/number")
    suspend fun loginWithNumber(@Body request: ReqOtpNum  ) : Response<ResOtp>

    @POST("login/check")
    suspend fun logincheck(@Body request: ReqCheckOtp ) : Response<ResCheckOtp>


    @GET("student/read")
    suspend fun ReadStudent(@Header("Authorization") token : String) : Response<ResReadStudent>

    @POST("astin/add")
    suspend fun AddAstin(@Header("Authorization") token : String , @Body request: ReqAddAstin) : Response<ResAddAstin>

    @GET("astin/read/{uid}")
    suspend fun ReadAstin(@Header("Authorization") token : String , @Path("uid") uid : String) : Response<ResReadAstin>

    @POST("attendance/sendsms")
    suspend fun SnedSms(@Header("Authorization") token : String ) : Response<ResSendSms>

    @GET("attendance/read")
    suspend fun ReadAttendance(@Header("Authorization") token : String  , @Query("date") date : String) : Response<ResReadAttendance>

    @PATCH("attendance/changeStatus")
    suspend fun ChangeStatusAttendance(@Header("Authorization") token : String  , @Body request: ReqAttendanceChangeStatus) : Response<ResAttendanceChangeStatus>

    @GET("class/read")
    suspend fun ReadClass(@Header("Authorization") token : String  ) : Response<ResReadClass>

    @DELETE("class/delete")
    suspend fun DeleteClass(@Header("Authorization") token : String  , @Query("id") id  : String ) : Response<ResDeleteClass>

    @PATCH("class/changeName")
    suspend fun ChangeClassName(@Header("Authorization") token : String  , @Body  request: ReqChangeClassName ) : Response<ResChangeClassName>

    @POST("class/add")
    suspend fun AddClass(@Header("Authorization") token : String  , @Query("name") name  : String ) : Response<ResAddClass>

    @POST("student/add")
    suspend fun AddStudent(@Header("Authorization") token : String , @Body request : ReqAddStudent) : Response<ResAddStudent>

    @DELETE("student/delete")
    suspend fun DeleteStudent(@Header("Authorization") token : String , @Query("id") id  : String) : Response<ResDeleteStudent>

    @GET("student/readone")
    suspend fun ReadOneStudent(@Header("Authorization") token : String , @Query("id") id  : String) : Response<ResReadOneStudent>

    @PATCH("student/update")
    suspend fun UpdateStudent(@Header("Authorization") token : String , @Body request: ReqUpdateStudent  ) : Response<ResUpdateStudent>

    @POST("adminControl/add")
    suspend fun AddAdmin(@Header("Authorization") token : String , @Body request: ReqAddAdmin  ) : Response<ResAddAdmin>

    @GET("adminControl/read")
    suspend fun ReadAdmin(@Header("Authorization") token : String  ) : Response<ResReadAdmins>

    @DELETE("adminControl/delete")
    suspend fun DeleteAdmin(@Header("Authorization") token : String  , @Query("id") id  : String ) : Response<ResDeleteAdmin>

}