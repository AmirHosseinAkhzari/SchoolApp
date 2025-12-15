

const express = require('express')


const router = express.Router()


router.get("/read" , async (req , res ) => { 

    const data = await global.db.attendance.getUserInfo(req.headers['authorization'])

    global.analytics.Event.sendEvent("ReadAttendAnceInAndroid" , {role : "Parent"} , "Main")

    console.log(req.headers['authorization'])
    res.status(200).json(data)

})

router.post("/addDescription" , async (req , res ) => { 

    const data = await global.db.attendance.addDescription(req.headers['authorization'] , req.body.text)

    console.log(req.headers['authorization'])
    res.status(200).json(data)

})







module.exports = router
