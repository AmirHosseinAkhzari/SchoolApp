


async function sms() {
    


    // get config.json for read password and user of  MelipayamakApi Service
    const utils = require("../utils/utils")
    utils()
    const config = await global.utils.config.read()


    // crate a new API line 
    const MelipayamakApi = require('melipayamak')

    const api = new MelipayamakApi(config.sms.user,config.sms.password)


    global.sms = { 
        send : { 
            otp : async (to , code) => {
                // api.sms().sendByBaseNumber(code, to, 393028)
            } , 
            absent : async(to , name) => { 
                api.sms().sendByBaseNumber(name, to, 393383)
            } , 
            lateness : async(to , name) => { 
                api.sms().sendByBaseNumber(name, to, 393384)
            } , 
            manager : async(to , name) => { 
                api.sms().sendByBaseNumber(name, to, 393387)
            }
            
        }
    }

    
   


    
}



module.exports = sms