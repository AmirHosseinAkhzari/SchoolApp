
    const express = require('express')


    const router = express.Router()

router.post("/number", async (req , res) =>{

    console.log("done")
    

    const data  = await global.db.otp.SendWithNumber(req.body.number , "Student")

    console.log(data)

    if(data.code != 200){
        res.status(500).json(data)
    }

    res.status(200).json(data)

} )


router.post("/uid", async (req , res) =>{

    console.log("done")

    const data  = await global.db.otp.SendWithUid(req.body.uid)

    console.log(data)

    if(data.code != 200){
        res.status(500).json(data)
    }

    res.status(200).json(data)

} )


router.post("/check", async (req , res) =>{


    console.log(req.body.number , req.body.code)

    const data  = await global.db.otp.check(req.body.number , req.body.code)

    console.log(data.code)



    if(data.code != 200){
        res.status(500).json(data)
    }

    res.status(200).json(data)


} )

module.exports = router;