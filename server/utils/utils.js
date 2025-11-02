// utils about time 

const fs = require('fs');
const path = require('path')
function utils() {

    global.utils  = {
        time : {
            iran : function(){
                const now = new Date();
                const options = { hour: '2-digit', minute: '2-digit', hour12: false, timeZone: 'Asia/Tehran' };
                return now.toLocaleTimeString('en-GB', options); 
          } , 
            iran2: function() {
                const now = new Date();
                const options = { 
                    hour: '2-digit', 
                    minute: '2-digit', 
                    second: '2-digit',  
                    hour12: false, 
                    timeZone: 'Asia/Tehran' 
                };
                return now.toLocaleTimeString('en-GB', options); 
            } , 
            checkStatus : function(time, start, end)   {
                const [h1, m1] = time.split(":").map(Number);
                const [h2, m2] = start.split(":").map(Number);
                const [h3, m3] = end.split(":").map(Number);

                const timeMinutes = h1 * 60 + m1;
                const startMinutes = h2 * 60 + m2;
                const endMinutes = h3 * 60 + m3;

                if (timeMinutes < startMinutes) return "زود اومده";
                if (timeMinutes > endMinutes) return "دیر اومده";
                return "سر تایم";
        } , 

        CheckOtp : function isMoreThan2MinutesAgo(timeStr) {

    
            const now = new Date();

            const [hours, minutes, seconds] = timeStr.split(':').map(Number);

            const inputTime = new Date();
            inputTime.setHours(hours, minutes, seconds, 0);

            const diffMs = now - inputTime;

            return diffMs > 2 * 60 * 1000;
        }
        } , 
        config : {
            read : async () => {
                return await JSON.parse(fs.readFileSync(path.join(__dirname , ".." , "config.json")))
            }
        } , 
        random : {
            getOtpCode : function (){
                return String(Math.floor(Math.random() * (99999 - 10000 + 1)) + 10000)
            }
        }
    }
}


module.exports = utils