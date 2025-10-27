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
        } , 
        config : {
            read : async () => {
                return await JSON.parse(fs.readFileSync(path.join(__dirname , ".." , "config.json")))
            }
        }
    }
}


module.exports = utils