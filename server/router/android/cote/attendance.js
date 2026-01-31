

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

router.get("/read", async (req , res) =>{


    const date = req.query.date

    console.log(date)


    if(date == null) {
        res.json({message : "one missing query" , code : 500})
    }
    const data  = await global.db.attendance.getall(date)


    console.log(data)

    res.status(200).json(data)

})

router.patch("/changeStatus", async (req , res) =>{

    console.log(req.body)
    
    const data  = await global.db.attendance.changeStatus(req.body.id , req.body.status , req.body.date)


    console.log(data)
    if(data.code != 200){
        res.status(data.code).json(data)
    }

    res.status(200).json(data)

})

module.exports = router
