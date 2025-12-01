
const express = require('express')  


const router = express.Router()

router.get("/read", async (req, res) => {

    const data = await global.db.user.getAllStu()

    console.log(data)

    res.json(data)
})


router.post("/add", async (req, res) => {

    const data = await global.db.user.add(req.body)

    console.log(data)

    res.json(data)
})


router.delete("/delete", async (req, res) => {

    const data = await global.db.user.delete(req.body.id)


    res.json(data)
})



module.exports = router;
