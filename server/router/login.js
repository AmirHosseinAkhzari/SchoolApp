
const express = require('express')



const router = express.Router()

router.get("/", async (req , res) =>{

    console.log("done")
    
    res.json(await global.db.otp.add(req.body.number));



} )

module.exports = router;