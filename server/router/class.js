



const express = require('express')  


const router = express.Router()

router.get("/read", async (req, res) => {

    const data = await global.db.class.getall();

    console.log(data)

    res.json(data)
})


router.post("/add", async (req, res) => {

    const data = await global.db.class.add(req.body.name)

    res.json(data)
})


router.delete("/delete", async (req, res) => {

    const data = await global.db.class.delete(req.body.id)

    res.json(data)
});


router.patch("/changeName", async (req, res) => {

    const data = await global.db.class.changeName(req.body.id , req.body.name)

    res.json(data)
});



module.exports = router;
