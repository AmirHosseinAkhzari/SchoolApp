
const express = require('express')


const router = express.Router()

router.post("/number", async (req , res) =>{

    console.log("done")
    

    const data  = await global.db.otp.SendWithNumber(req.body.number , "Student" , "Parent")

    console.log(data)

    if(data.code != 200){
        global.analytics.Event.sendEvent("login(Number)" , data , "Main")
        res.status(500).json(data)
    }
    global.analytics.Event.sendEvent("login(Number)" , data , "Main")
    res.status(200).json(data)

} )



router.post("/check", async (req , res) =>{


    console.log(req.body.number , req.body.code)

    const data  = await global.db.otp.check(req.body.number , req.body.code , null , "Parent")

    console.log(data.code)



    if(data.code != 200){
        global.analytics.Event.sendEvent("OTPCheck" , data , "Main")
        res.status(500).json(data)
    }
    
    global.analytics.Event.sendEvent("OTPCheck" , data , "Main")
    res.status(200).json(data)


} )

module.exports = router;