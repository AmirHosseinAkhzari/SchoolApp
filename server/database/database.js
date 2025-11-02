

const mongoose = require('mongoose')


const utils = require('../utils/utils')

const jalaali = require('jalaali-js');

// ------- Student Schema -------
const studentSchema = new mongoose.Schema({
    firstname : String , 
    lastname : String , 
    birthday : Date , 
    nationalid : String , 
    number : String , 
    ParentNumber : String , 
    LocalNumber : String , 
    classId : mongoose.Schema.Types.ObjectId 
})


// ------- class Schema -------2
const classSchema = new mongoose.Schema({
    name : String , 
})

const otpSchema = new mongoose.Schema({
    code : String  , 
    studentId : mongoose.Schema.Types.ObjectId , 
    checkIn : String
})





//  ----- attendance Schema -------
const attendanceSchema = new mongoose.Schema({
    studentId : mongoose.Schema.Types.ObjectId , 
    classId : mongoose.Schema.Types.ObjectId , 
    studentFullName : String , 
    className : String , 
    checkIn : String , 
    status : String , 
    date : String

})

const Class = mongoose.model('Class' , classSchema)

const Student = mongoose.model('Student', studentSchema)

const Attendance = mongoose.model("Attendance" ,  attendanceSchema)

const Otp = mongoose.model("Otp" ,  otpSchema)


async function ConnectTodb() {
  try {
    await mongoose.connect("mongodb://localhost:27017/school")
    console.log("✅ Connected to MongoDB")
    utils()

     global.db = {
        student : { 
            add : async (data) => { 
                const s = new Student(data)
                await s.save()    
            } , 
            update : async (id , data) => { 
                await Student.findByIdAndUpdate(id , data , {new : true})
            }  , 
            delete : async ( id) => { 
                await Student.findByIdAndDelete(id )
            }  ,
            getall : async () => {
                const data = await Student.find()
                return data
            } ,
            getone : async (id) =>{
                return await Student.findById(id)
            },
            deleteAll : async () => {
                await Student.deleteMany({})
            }
        }, 
        class : {
            add : async (name) => {
                const c = Class({name : name})
                await c.save()
            } , 
            update : async (id , name) => {
                await Class.findByIdAndUpdate(id , {name : name})
            } , 
            getall : async () => {
                return await Class.find()
            } , 
            getone : async (id) => {
                return await Class.findById(id)
            }, 
            delete : async (id) => {
                await Class.findByIdAndDelete(id)
            } , 
            deleteAll : async () => {
                await Class.deleteMany({})
            }
        } , 
        attendance : { 
            check : async (stuId , cId) => {

                const stu = (await Student.findById(stuId))

                const fullname = stu.firstname + " " + stu.lastname

                const cName = (await Class.findById(cId)).name

                console.log(cName)

                const time = global.utils.time.iran()

                const config = await global.utils.config.read()

                console.log(config)

                console.log(time)

                console.log(config.time.start)

                console.log(config.time.end)

                const jDate = jalaali.toJalaali(new Date())

                const date  = `${jDate.jy}/${jDate.jm}/${jDate.jd}`
                const existing = await Attendance.findOne({
                    studentId: stuId,
                    classId: cId,
                    date: date
                });

                if (existing) {
                    return;
                }

                console.log(jDate)
                const status = global.utils.time.checkStatus(time, config.time.start , config.time.end)

                const check = Attendance({
                    className: cName , 
                    studentId:stuId ,
                    classId : cId,
                    checkIn : time  ,
                    status : status ,
                    studentFullName :  fullname , 
                    date :date
                    })
                check.save()
            } , 
            getall : async () => {
                return await Attendance.find()
            }
        } , 
        otp : {
            add : async (num) => {
    
            
                const code = global.utils.random.getOtpCode()
                

                const student = await Student.findOne({  
                    number : num 
                })

                console.log(student)
                
                if (!student){ 
                    return {message : "دسترسی غیر مجاز" , code : 500}
                }

                const otpBlock = await Otp.findOne({
                    studentId  : student._id
                })


                console.log(otpBlock )
                if (otpBlock){
                    console.log("hi")
                    const checkTime = global.utils.time.CheckOtp(otpBlock.checkIn)

                    if (checkTime){
                        await global.sms.send.otp(num  ,code)

                        await otpBlock.deleteOne()

                        const o= Otp({
                        checkIn  : global.utils.time.iran2() , 
                        code : code ,
                        studentId : student._id
                        })

                        o.save()

                        return {message : "کد ارسال شد !!!" , code : 200}
                    }else{
                        return {message : "لطفا دقایق دیگر تلاش کنید" , code : 500}
                    }

                }else{
                    
                    await global.sms.send.otp(num ,code)

                    const o= Otp({
                        checkIn  : global.utils.time.iran2() , 
                        code : code ,
                        studentId : student._id
                    })

                    o.save()


                    return {message : "کد ارسال شد !!!" , code : 200}


                }


            }
        }
    }

  } catch (e) {
    console.log("❌ Error:", e)
  }
}

module.exports = ConnectTodb
