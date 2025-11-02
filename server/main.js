const db = require('./database/database')

const sms = require('./SMS/sms')

const express = require('express')

const loginRoutes = require('./router/login');


async function main() {

  // strat my database
  await db()

  // start my SMS Service
  await sms()

  const app  = express()

  app.use(express.json())


  app.use('/login', loginRoutes)


  app.listen(3000, () => console.log("Server is Up"));

  
  
  

}

main()
