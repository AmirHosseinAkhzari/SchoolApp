const jwt = require("jsonwebtoken");

module.exports = (req, res, next) => {
    const token = req.cookies?.token

    console.log()
    
    if (!token) {
        return res.redirect("/adminPanel/login");
    }

    try {
        const payload  = jwt.verify(token, "asdeashndjoasndfekswfeiw0mrfmn4rj24u9fr")
        if(payload.role == "admin"){
            next()
        }else{
            return res.redirect("/adminPanel/login");
        }
        
    } catch (err) {
        return res.redirect("/adminPanel/login");
    }
}
