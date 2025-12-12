


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
                const res = await api.sms().sendByBaseNumber(code, to, 393028)
                global.analytics.Event.sendEvent("Otp" , res , "Main")
            } , 
            absent : async(to , name) => { 
                 const res =  await api.sms().sendByBaseNumber(name, to, 393383)
                 global.analytics.Event.sendEvent("absentSMS" , res , "Main")

            } , 
            lateness : async(to , name) => { 
                
                const res =  await api.sms().sendByBaseNumber(name, to, 393384)
                 global.analytics.Event.sendEvent("latenessSMS" , res , "Main")

            } , 
            manager : async(to , name) => { 
                const res =  await api.sms().sendByBaseNumber(name, to, 393387)
                 global.analytics.Event.sendEvent("managerSMS" , res , "Main")
            }
            
        }
    }

    
   


    
}



module.exports = sms