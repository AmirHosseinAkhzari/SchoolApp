const express = require('express')


const router = express.Router()

router.get("/getname" , async (req , res ) => { 

    const data = await global.db.user.getName(req.headers['authorization'])

    res.status(200).json(data)

})

module.exports = router
