

const express = require('express')


const router = express.Router()


router.post("/sendsms", async (req , res) =>{


    const checkToken  = global.utils.admin.checkToken(req.headers['authorization'])

    if(checkToken){

        const data  = await global.db.attendance.sendsms()


        res.json(data)
    }else{
        res.json({message : "توکن صحیح نیست" , code : 500 , students : []})
    }


})

module.exports = router
