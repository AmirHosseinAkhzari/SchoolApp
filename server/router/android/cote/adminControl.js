

const express = require('express')  


const router = express.Router()

router.post("/add" , async (req , res) => {

    const LocalRes = await global.db.admin.add(req.body.number , req.body.firstname , req.body.lastname)

    res.send(LocalRes)
})

router.get("/read" , async (req , res) => {

    const LocalRes = await global.db.admin.read()

    res.send( {code : 200 , data : LocalRes})
})

router.delete("/delete" , async (req , res) => {

    const LocalRes = await global.db.admin.delete(req.query.id)

    res.send(LocalRes)
})


module.exports = router;
