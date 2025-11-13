



const express = require('express')

const router = express.Router()

const path = require('path');

const fs = require('fs')
router.post("/profile", async (req , res) =>{


    let imagePath  = path.join(__dirname, '..', 'static', 'images', `${req.body.id}.png`);


    if (fs.existsSync(imagePath)){

       res.sendFile(imagePath)

    }else {

        imagePath  = path.join(__dirname, '..', 'static', 'images', `unknow.png`);
        res.sendFile(imagePath)

    }

} )





module.exports = router;
