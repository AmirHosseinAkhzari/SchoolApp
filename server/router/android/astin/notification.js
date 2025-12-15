

const express = require('express')

const router = express.Router()

router.post("/addToken" , async (req , res) => {


    const data = await global.db.notification.addToken(req.body.notificationToken , req.headers['authorization'])

    console.log(data)

    if(data.code != 200){
        res.status(500).json(data)
    }

    res.status(200).json(data)

})

module.exports = router;
