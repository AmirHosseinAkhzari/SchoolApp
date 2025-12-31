const express = require('express')



const router = express.Router()


router.patch("/add" , async (req , res) => {

    const LocalRes = await global.db.admin.add(req.body.number , req.body.firstname , req.body.lastname)

    res.send(LocalRes)
})

router.get("/read" , async (req , res) => {

    const LocalRes = await global.db.admin.read()

    res.send(LocalRes)
})

router.delete("/delete" , async (req , res) => {

    const LocalRes = await global.db.admin.delete(req.body.id)

    res.send(LocalRes)
})




module.exports = router;

