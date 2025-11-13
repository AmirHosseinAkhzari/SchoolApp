const db = require('./database/database')

const sms = require('./SMS/sms')

const express = require('express')

const path  = require('path')

const loginRoutes = require('./router/login')

const AttendanceRoutes = require('./router/attendance')

const AdminRoutes = require('./router/admin')

const imagesRoutes = require('./router/image')


const ClassRoutes = require('./router/class')

const StudentRoutes = require('./router/student')



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


  app.use('/login',loginRoutes)
  app.use('/attendance', AttendanceRoutes)
  app.use('/admin',AdminRoutes )
  app.use('/class',ClassRoutes )
  app.use('/image',imagesRoutes )
  app.use('/student',StudentRoutes )


  app.use( "/adminPanel/login", express.static(path.join(__dirname, "public/admin/login")))
  app.use("/adminPanel", checkToken , express.static(path.join(__dirname, "public/admin/main")));




const PORT = process.env.PORT || 3000;
app.listen(3000, "0.0.0.0", () => {
  console.log("✅ Server running on http://192.168.1.10:3000");
});



}

main()
