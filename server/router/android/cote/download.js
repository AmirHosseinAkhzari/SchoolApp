const express = require('express')  

const path = require('path')

const router = express.Router()


router.get('/coat', (req, res) => {
    const filePath = path.join("files" , "coat.apk" )

    console.log(filePath)

    res.download(filePath, 'cote.apk', (err) => {
        if (err) {
            console.log(err)
            res.status(500).send("Error downloading file")
        }
    })
})


module.exports = router;
