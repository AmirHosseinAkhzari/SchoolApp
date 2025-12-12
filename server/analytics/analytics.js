
const axios = require("axios");
const Rest = require("melipayamak-api/src/sms/rest");



function analytics(){
    global.analytics = {
        Event : {
            sendEvent: async (name , data , userID) =>{
                const res = await axios.post("https://www.google-analytics.com/mp/collect?measurement_id=G-FR4QH1MPBG&api_secret=0r3Nyh8_RgCVJLZlXI_ulQ", {
                    client_id: userID,
                    events: [{
                    name: name,
                    params: data
                    }]
                })

            }
        }
    }

}


module.exports = analytics



