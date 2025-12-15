

const express = require('express')


const router = express.Router()


router.get("/read" , async (req , res ) => { 

    const data = await global.db.attendance.getUserInfo(req.headers['authorization'])

    global.analytics.Event.sendEvent("ReadAttendAnceInAndroid" , {role : "Student"} , "Main")


    console.log(req.headers['authorization'])
    res.status(200).json(data)

})







module.exports = router
