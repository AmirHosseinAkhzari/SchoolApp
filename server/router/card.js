
const express = require('express')  


const router = express.Router()

router.get("/read", async (req, res) => {

    const data = await global.db.card.read()

    console.log(data)

    res.json(data)
})


router.post("/add", async (req, res) => {

    const data = await global.db.card.add(req.body.uid, req.body.ownerId)

    console.log(data)

    res.json(data)
})


router.delete("/delete", async (req, res) => {

    const data = await global.db.card.delete(req.body.id)


    res.json(data)
})



module.exports = router;
