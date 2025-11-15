



const express = require('express')

const router = express.Router()

const path = require('path');

const fs = require('fs')
router.get("/profile", async (req, res) => {
    const id = req.query.id;

    let imagePath = path.join(__dirname, '..', 'static', 'images', `${id}.png`);

    if (fs.existsSync(imagePath)) {
        res.sendFile(imagePath);
    } else {
        imagePath = path.join(__dirname, '..', 'static', 'images', `unknow.png`);
        res.sendFile(imagePath);
    }
});






module.exports = router;
