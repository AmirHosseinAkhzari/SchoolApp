

const express = require('express')  


const router = express.Router()

router.post("/add", async (req, res) => {

    const data = await global.db.card.add(req.body.uid, req.body.ownerId)

    console.log(data)

    res.json(data)
})

module.exports = router;
