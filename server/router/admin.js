



const express = require('express')

const jwt  = require('jsonwebtoken');

const router = express.Router()

router.post("/login", async (req , res) =>{

    console.log("done")
    

    const data  = await global.db.otp.SendWithNumber(req.body.number , "admin" , "admin")

    console.log(data)

    if(data.code != 200){
        res.status(500).json(data)
    }

    res.status(200).json(data)

} )


router.post("/check", async (req , res) =>{


    console.log(req.body)
    const data  = await global.db.otp.check(req.body.number , req.body.code , req.body.role)
    const token = data.token
    console.log(data)

    if(data.code != 200){
        res.status(500).json(data)
    }

    console.log(token)
    if(req.body.role == "attendance"){
        res.cookie("token", token, {
            httpOnly: true,    
            secure: false,       
            sameSite: "strict",
            maxAge: 10 * 365 * 24 * 60 * 60 * 1000 
        })
        res.status(200).json(data)
    }else{
        res.cookie("token", token, {
            httpOnly: true,    
            secure: false,       
            sameSite: "strict",
            maxAge:  24 * 60 * 60 * 1000 
        })
        res.status(200).json(data)
    }


} )

module.exports = router;
