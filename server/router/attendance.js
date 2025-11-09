


const express = require('express')


const router = express.Router()

router.post("/check", async (req , res) =>{

    console.log("done")
    

    const data  = await global.db.attendance.check(req.body.uid)

    console.log(data)

    if(data.code != 200){

        res.status(data.code).json(data)
    }

    res.status(200).json(data)

})



module.exports = router;
