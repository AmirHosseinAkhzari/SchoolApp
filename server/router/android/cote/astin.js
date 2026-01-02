

const express = require('express')  


const router = express.Router()

router.post("/add", async (req, res) => {

    

    const checkToken  = global.utils.admin.checkToken(req.headers['authorization'])

    if(checkToken){
        const data = await global.db.card.add(req.body.uid, req.body.ownerId)

        res.json(data)
    }else{
        res.json({message : "توکن صحیح نیست" , code : 500 , students : []})
    }
})

router.post("/read", async (req, res) => {

    

    const checkToken  = global.utils.admin.checkToken(req.headers['authorization'])

    if(checkToken){

        const data = await global.db.user.getInfoWithUid(req.body.uid)

        res.json(data)
    }else{
        res.json({message : "توکن صحیح نیست" , code : 500 , students : []})
    }


})

module.exports = router;
