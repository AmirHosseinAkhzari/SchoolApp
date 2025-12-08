const db = require('./database/database')

const sms = require('./SMS/sms')

const express = require('express')

const path  = require('path')

const loginRoutes = require('./router/android/login')

const AttendanceRoutes = require('./router/attendance')

const AdminRoutes = require('./router/admin')

const imagesRoutes = require('./router/image')


const ClassRoutes = require('./router/class')

const StudentRoutes = require('./router/student')

const CardRoutes = require('./router/card')

const NotificationRoutes = require('./router/android/notification')

const AttendanceAndroidRoutes = require('./router/android/attendance')






const cookieParser = require("cookie-parser");

const checkToken = require("./middleware/checkToken");

const morgan = require('morgan')
async function main() {

  // strat my database
  await db()

  // start my SMS Service
  await sms()

  const app  = express()

  app.use(morgan("dev"))
  
  app.use(express.json())

  
  app.use(cookieParser())


  // global.db.user.add({
  //   firstname : "امیرحسین" , 
  //   lastname : "اخضری" , 
  //   birthday : Date.now() , 
  //   nationalid : "2500766111"  , 
  //   number : "09304682860" , 
  //   classId : "690000926aa70ec7aa4d4731" , 
  //   role : "Student" , 
    
  // })


  app.use('/android/login',loginRoutes)
  app.use('/android/notification', NotificationRoutes )
  app.use('/android/attendance', AttendanceAndroidRoutes )


  app.use('/attendance', AttendanceRoutes)
  app.use('/admin',AdminRoutes )
  app.use('/class',ClassRoutes )
  app.use('/image',imagesRoutes )
  app.use('/student',StudentRoutes )
  app.use('/card',CardRoutes )
  console.log(global.utils.token.newToken("6917990640fb4d7352b0be24" , "v"))    

  app.use( "/dev", express.static(path.join(__dirname, "public/attendanceDevice")))

  app.use( "/adminPanel/login", express.static(path.join(__dirname, "public/admin/login")))
  app.use("/adminPanel", checkToken , express.static(path.join(__dirname, "public/admin/main")));
  app.use("/" , express.static(path.join(__dirname, "public/main")));




const PORT = process.env.PORT || 9000;

app.listen(PORT, "0.0.0.0", () => {
  console.log(`✅ Server running on http://0.0.0.0:${PORT}`);
});



}

main()
