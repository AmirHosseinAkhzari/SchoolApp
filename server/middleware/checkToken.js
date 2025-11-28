const jwt = require("jsonwebtoken");
const utils = require('../utils/utils')


module.exports = (req, res, next) => {

    const token = req.cookies?.token

    utils()
    console.log()
    
    if (!token) {
        return res.redirect("/adminPanel/login");
    }

    try {
        const payload  = global.utils.token.verify(token)
        if(payload.role == "admin"){
            next()
        }else{
            return res.redirect("/adminPanel/login");
        }
        
    } catch (err) {
        return res.redirect("/adminPanel/login");
    }
}
