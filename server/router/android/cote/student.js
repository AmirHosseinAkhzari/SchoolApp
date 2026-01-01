const express = require('express')  


const router = express.Router()

router.get("/read", async (req, res) => {

    const checkToken  = global.utils.admin.checkToken(req.headers['authorization'])

    if(checkToken){
        const data = await global.db.user.getAllStuIdAndName()

        res.json({message : "توکن صحیح نیست" , code : 500 , students : data})
    }else{
        res.json({message : "توکن صحیح نیست" , code : 500 , students : []})
    }

})

module.exports = router;
