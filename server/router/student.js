
const express = require('express')  


const router = express.Router()

router.get("/read", async (req, res) => {

    const data = await global.db.user.getAllStu()

    console.log(data)

    res.json(data)
})

router.get("/readone", async (req, res) => {

    const data = await global.db.user.getone(req.query.id)

    console.log(data)

    res.json({data : data , code : 200 })
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

router.patch("/update", async (req, res) => {

    const { id, ...updateData } = req.body;
    const data = await global.db.user.update(id, updateData)

    res.json({code: 200, message: "دانش‌آموز با موفقیت به‌روزرسانی شد"})
})



module.exports = router;
