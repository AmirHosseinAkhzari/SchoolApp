


const express = require('express')


const router = express.Router()

router.post("/check", async (req , res) =>{

    console.log("done")


    const data  = await global.db.attendance.check(req.body.uid)

    console.log(data)


    console.log("asodjasdi")
    if(data.code != 200){

        res.status(data.code).json(data)

        console.log("Attendance" , data , "Main")
        global.analytics.Event.sendEvent("Attendance" , data , "Main")

    }

    console.log("Attendance" , data , "Main")
    global.analytics.Event.sendEvent("Attendance" , data , "Main")
    res.status(200).json(data)
    
})

router.patch("/changeStatus", async (req , res) =>{

    console.log("done")
    
    const data  = await global.db.attendance.changeStatus(req.body.id , req.body.status , req.body.date)


    console.log(data)
    if(data.code != 200){
        res.status(data.code).json(data)
    }

    res.status(200).json(data)

})




router.get("/read", async (req , res) =>{


    const date = req.query.date
    const data  = await global.db.attendance.getall(date)

    console.log(date)

    console.log(data)

    res.status(200).json(data)

})

router.get("/sendsms", async (req , res) =>{

    console.log("aDASD")

    const data  = await global.db.attendance.sendsms()

    console.log(data)

    res.status(200).json(data)

})

router.patch("/changeStarttime", async (req , res) =>{

    console.log("aDASD")

    const data  = await global.db.attendance.changeStarttime(req.body.time)


    // res.status(200).json()

})

router.get("/date" , async (req , res) => {

    const date = await global.db.attendance.getAllDate()

    res.json({date})
})





module.exports = router;
