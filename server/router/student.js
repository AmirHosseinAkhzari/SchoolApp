
const express = require('express')  


const router = express.Router()


router.get("/read", async (req, res) => {

    const data = await global.db.user.getAllStu()

    console.log(data)

    res.json(data)
})


module.exports = router;
