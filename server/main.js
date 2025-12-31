const db = require('./database/database')

const analytics = require('./analytics/analytics')


const sms = require('./SMS/sms')

const express = require('express')

const path  = require('path')


const AttendanceRoutes = require('./router/attendance')

const AdminRoutes = require('./router/admin')

const imagesRoutes = require('./router/image')

const ClassRoutes = require('./router/class')

const StudentRoutes = require('./router/student')

const CardRoutes = require('./router/card')

const adminControlRoutes = require('./router/adminControl')




const NotificationAstinRoutes = require('./router/android/astin/notification')

const attendanceAstinRoutes = require('./router/android/astin/attendance')

const loginAstinRoutes = require('./router/android/astin/login')

const accAstinRoutes = require('./router/android/astin/accunt')




const AttendanceKeravatRoutes = require('./router/android/keravat/attendance')

const loginKeravatRoutes = require('./router/android/keravat/login')

const accKeravatRoutes = require('./router/android/keravat/accunt')





const cookieParser = require("cookie-parser");

const checkToken = require("./middleware/checkToken");

const checkTokenAttendance = require("./middleware/checkTokenAttendAnce");


const morgan = require('morgan')
const utils = require('./utils/utils')
async function main() {

  // strat my database
  await db()

  // start my SMS Service
  await sms()

  await analytics()

  await utils()

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


  app.use('/android/astin/login',loginAstinRoutes)
  app.use('/android/astin/notification', NotificationAstinRoutes )
  app.use('/android/astin/attendance', attendanceAstinRoutes )
  app.use('/android/astin/acc',accKeravatRoutes)


  app.use('/android/keravat/attendance', AttendanceKeravatRoutes )
  app.use('/android/keravat/login',loginKeravatRoutes)
  app.use('/android/keravat/acc',accKeravatRoutes)


  app.use('/attendance'  , AttendanceRoutes)
  app.use('/admin' , AdminRoutes )
  app.use('/class',checkToken , ClassRoutes )
  app.use('/image',checkToken , imagesRoutes )
  app.use('/student',checkToken, StudentRoutes )
  app.use('/card',checkToken , CardRoutes )
  app.use('/adminControl' , adminControlRoutes )

  
  app.use( "/dev/login", express.static(path.join(__dirname, "public/attendanceDevice/login")))
  app.use( "/dev", checkTokenAttendance,   express.static(path.join(__dirname, "public/attendanceDevice/main")))


  app.use( "/adminPanel/login", express.static(path.join(__dirname, "public/admin/login")))
  app.use("/adminPanel", checkToken , express.static(path.join(__dirname, "public/admin/main")));
  app.use("/" , express.static(path.join(__dirname, "public/main")));


const PORT = process.env.PORT || 9000;

app.listen(PORT, "0.0.0.0", () => {
  console.log(`✅ Server running on http://0.0.0.0:${PORT}`);
});





}

main()
