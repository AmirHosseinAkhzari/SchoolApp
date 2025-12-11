const jwt = require("jsonwebtoken");
const utils = require('../utils/utils')


module.exports = (req, res, next) => {

    const token = req.cookies?.token

    utils()
    console.log()
    
    if (!token) {
        return res.redirect("/dev/Login");
    }

    try {
        const payload  = global.utils.token.verify(token)
        if(payload.role == "attendance"){
            next()
        }else{
            return res.redirect("/dev/Login");
        }
        
    } catch (err) {
        return res.redirect("/dev/Login");
    }
}
