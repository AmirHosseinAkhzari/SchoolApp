// utils about time 

const fs = require("fs").promises 
const fsSync = require("fs")


const path = require('path')
const jwt  = require('jsonwebtoken');

const SECRET_KEY = "asdeashndjoasndfekswfeiw0mrfmn4rj24u9fr"


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

                if (timeMinutes < startMinutes) return "حاضر";
                if (timeMinutes > endMinutes) return "دیر اومده";
                return "حاضر";
        } , 

        CheckOtp : function isMoreThan2MinutesAgo(timeStr) {
            const now = Date.now();
            const diff = now - timeStr
            return diff >= 0.5 * 60 * 1000;
        }
        } , 
        config : {
            read : async () => {
                const data = await fs.readFile(path.join(__dirname , ".." , "config.json"), 'utf8')
                return JSON.parse(data)
            } ,
            edit : async (time) => {
                let oldCnf = await global.utils.config.read(); // Use the existing 'read' function
                oldCnf.time.start = time;
                console.log(oldCnf);
                
                // 1. Convert object to JSON string
                const jsonString = JSON.stringify(oldCnf, null, 4); // null, 4 is for pretty printing
                
                // 2. Use await fs.writeFile (from fs.promises)
                await fs.writeFile(path.join(__dirname , ".." , "config.json"), jsonString);
                // No return needed here, just a successful write
            }   
        } , 
        random : {
            getOtpCode : function (){
                return String(Math.floor(Math.random() * (99999 - 10000 + 1)) + 10000)
            }
        } , 
        hex : {
            hexToDec : function (hex){
                  hex = hex.replace(/[^A-Fa-f0-9]/g, "");

                
                let reversed = hex.match(/.{1,2}/g).reverse().join("");

               
                let dec = parseInt(reversed, 16);

                
                return dec.toString().padStart(10, "0");
            }
        } , 
        token : {
            newToken : function (userId , role) {

                
                const payload  = {
                    userId : userId , 
                    role : role
                }
                
                const token = jwt.sign(payload , SECRET_KEY )

                return token 
            } , 
            verify : function (token) {

                const payload = jwt.verify(token , SECRET_KEY)

                return payload
            }
        }
    }
}




module.exports = utils