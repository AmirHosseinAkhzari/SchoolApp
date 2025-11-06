const db = require('./database/database')

const sms = require('./SMS/sms')

const express = require('express')

const loginRoutes = require('./router/login');

const morgan = require('morgan')
async function main() {

  // strat my database
  await db()

  // start my SMS Service
  await sms()

  const app  = express()

  app.use(morgan("dev"))
  
  app.use(express.json())

  app.use('/login', loginRoutes)


const PORT = process.env.PORT || 3000;
app.listen(PORT, '0.0.0.0', () => {
  console.log(`Server listening on http://0.0.0.0:${PORT}`);
});


}

main()
