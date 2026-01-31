const express = require('express')  


const router = express.Router()

router.get("/read", async (req, res) => {

    const checkToken  = global.utils.admin.checkToken(req.headers['authorization'])

    if(checkToken){
        const data = await global.db.user.getAllStuIdAndName()

        res.json({message : "عملیات با موفقیت انجام شد" , code : 200 , students : data})
    }else{
        res.json({message : "توکن صحیح نیست" , code : 500 , students : []})
    }

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

    const data = await global.db.user.delete(req.query.id)


    res.json(data)
})

router.patch("/update", async (req, res) => {
    

    const data = await global.db.user.update(req.body.id, req.body.updateData)

    res.json({code: 200, message: "دانش‌آموز با موفقیت به‌روزرسانی شد"})
})

module.exports = router;
