

const mongoose = require('mongoose')


const utils = require('../utils/utils')

const jalaali = require('jalaali-js');

const jwt  = require('jsonwebtoken');
const { use } = require('../router/android/login');

// ------- User Schema -------
const userSchema = new mongoose.Schema({
    firstname : String , 
    lastname : String , 
    birthday : String , 
    nationalid : String , 
    number : String , 
    ParentNumber : String , 
    LocalNumber : String , 
    classId : mongoose.Schema.Types.ObjectId  , 
    role : String , 
    status : String , 
    notificationToken : String
})


// ------- class Schema -------
const classSchema = new mongoose.Schema({
    name : String , 
})


// ----------- OPT Schema ------------
const otpSchema = new mongoose.Schema({
    code : String  , 
    number : String ,  
    checkIn : Date , 
    role : String
})


// ----------- card Schema ----------------

const cardSchema = new mongoose.Schema({
    ownerId : mongoose.Schema.Types.ObjectId , 
    role : String , 
    uid : String
})



//  ----- attendance Schema -------
const attendanceSchema = new mongoose.Schema({
    userId : mongoose.Schema.Types.ObjectId , 
    classId : mongoose.Schema.Types.ObjectId , 
    userFullName : String , 
    className : String , 
    checkIn : String , 
    status : String , 
    date : String , 

})



const Class = mongoose.model('Class' , classSchema)

const User = mongoose.model('User', userSchema)

const Attendance = mongoose.model("Attendance" ,  attendanceSchema)

const Otp = mongoose.model("Otp" ,  otpSchema)

const Card = mongoose.model("Card" , cardSchema)

async function ConnectTodb() {
  try {
    await mongoose.connect("mongodb://localhost:27017/school")
    console.log("✅ Connected to MongoDB")
    utils()
// const userSchema = new mongoose.Schema({
//     firstname : String , 
//     lastname : String , 
//     birthday : Date , 
//     nationalid : String , 
//     number : String , 
//     ParentNumber : String , 
//     LocalNumber : String , 
//     classId : mongoose.Schema.Types.ObjectId  , 
//     role : String
// })
    // {
    //   id: "stu-1",
    //   firstName: "امیرحسین",
    //   lastName: "اخضری",
    //   classId: "class-8",
    //   birthday: "1403/05/12",
    //   nationalId: "0056732941",
    //   number: "123456",
    //   ParentNumber: "۰۹۱۲۳۴۵۶۷۸۹",
    //   LocalNumber: "۰۷۱۵۲۳۴۵۶۷۸",
    //   photo: "https://i.pravatar.cc/150?u=1"
    // },
     global.db = {
        user : { 
            add : async (data) => { 
                try { 
                console.log(data)
                const s = new User(data)
                await s.save()    
                    return {code : 200 , message : "دانش آموز با موفقیت اضافه شد "}

                }catch{
                    return {code : 500 , message : "مشکلی در اضافه کردن دانش آموز"}
                }
                
            } , 
            update : async (id , data) => { 
                await User.findByIdAndUpdate(id , data , {new : true})
            }  , 
            delete : async ( id) => { 
                try { 
                    await User.findByIdAndDelete(id )   
                    return {code : 200 , message : "دانش آموز با موفقیت حذف شد "}

                }catch{
                    return {code : 500 , message : "مشکلی در حذف کردن دانش آموز"}
                }
            }  ,
            getall : async () => {
                const data = await User.find()
                return data
            } ,
            getone : async (id) =>{
                return await User.findById(id)
            },
            deleteAll : async () => {
                await User.deleteMany({})
            }, 
            getAllStu : async () => { 
                return await User.find({role : "Student"}).select("_id firstname lastname birthday nationalid number ParentNumber LocalNumber classId")
            }
        }, 
        class : {
            add : async (name) => {
                const c = Class({name : name})
                await c.save()
                return {code : 200 , message :"کلاس با موفقیت ساخته شد"}
            } , 
            changeName : async (id , name) => {
                await Class.findByIdAndUpdate(id , {name : name})
                return {code : 200 , message :"کلاس با موفقیت ساخته شد"}
            } , 
            getall : async () => {
                return await Class.find().select("name _id")

            } , 
            getone : async (id) => {
                return await Class.findById(id)
            }, 
            delete : async (id) => {
                await Class.findByIdAndDelete(id)
                return {code : 200 , message :"کلاس با موفقیت حذف شد"}
            } , 
            deleteAll : async () => {
                await Class.deleteMany({})
            }
        } , 
        attendance : {
            check : async (uid ) => {


                const CardData = await Card.findOne({
                    uid : uid
                })

                if(!CardData){
                    return {
                        message : "این کارت وجود ندارد" ,
                        code : 400 ,
                    }
                }

                const user = await User.findById(CardData.ownerId)

                const fullname = user.firstname + " " + user.lastname


                if(!user){
                    return {
                        message : "این دانش آموز وجود ندارد",
                        code : 400 ,
                    }
                }

                const _class = await Class.findById(user.classId)

                console.log(_class)

                if(!_class){
                    return {
                        message : "این کلاس وجود ندارد" ,
                        code : 400 ,
                    }
                }

                
                const time = global.utils.time.iran()

                console.log(time)

                const config = await global.utils.config.read()

                console.log(config)

                const jDate = jalaali.toJalaali(new Date())

                const date  = `${jDate.jy}/${jDate.jm}/${jDate.jd}`

                console.log(date)

                const existing = await Attendance.findOne({
                    userId: user._id,
                    date: date
                });

                console.log(existing)

                if(existing){
                    if(existing.status != "غایب"){
                    return {
                        message :"حضور شما ثبت شده" ,
                        code : 500 ,
                    }
                }
                }

                const status = global.utils.time.checkStatus(time, config.time.start , config.time.end)

                console.log(status)


                const data = Attendance({
                    className: _class.name , 
                    userId: user._id ,
                    classId : _class._id,
                    checkIn : time  ,
                    status : status ,
                    userFullName :fullname, 
                    date :date
                    })


                data.save()

                return {
                    message : fullname ,
                    code : 200 ,
                }


            } , 
            getall: async () => {
            try {
                const jDate = jalaali.toJalaali(new Date());
                const today = `${jDate.jy}/${jDate.jm}/${jDate.jd}`;

                const students = await User.find({ role: "Student" });
                const todayRecords = await Attendance.find({ date: today });

                const result = [];

                for (const stu of students) {
                const record = todayRecords.find(
                    a => a.userId.toString() === stu._id.toString()
                );

                if (record) {
                    
                    result.push({
                    id: record._id,          
                    studentId: stu._id,     
                    status: record.status,
                    checkIn: record.checkIn,
                    date: record.date
                    });
                } else {
                    // ✔️ غایب است
                    result.push({
                    id: stu._id ,                  
                    studentId: stu._id,     
                    status: "غایب",
                    checkIn: "—",
                    date: "—"
                    });
                }
                }

                return { code: 200, data: result };

            } catch (err) {
                console.error("❌ ERROR getall:", err);
                return { code: 500, message: "خطا در دریافت حضور و غیاب" };
            }
            } , 
            changeStatus : async(id , status) => {


                try{

                
                await Attendance.findByIdAndUpdate(id , {status : status})

                    if (!updated) {
                        throw new Error("Attendance not found"); 
                        }

                return { code: 200 , message : "تغیرات با موفیت انجام شد"};

                }catch(err){
                    console.log("adasasdsadddasddsad")
                    
                const user = await User.findById(id)

                
                const fullname = user.firstname + " " + user.lastname


                if(!user){
                    return {
                        message : "این دانش آموز وجود ندارد",
                        code : 400 ,
                    }
                }

                const _class = await Class.findById(user.classId)

                console.log(_class)

                if(!_class){
                    return {
                        message : "این کلاس وجود ندارد" ,
                        code : 400 ,
                    }
                }

                
                const time = global.utils.time.iran() 

                console.log(time)

                const config = await global.utils.config.read()

                console.log(config)

                const jDate = jalaali.toJalaali(new Date())

                const date  = `${jDate.jy}/${jDate.jm}/${jDate.jd}`

                console.log(date)

                const existing = await Attendance.findOne({
                    userId: user._id,
                    date: date
                });

                console.log(existing)

                if(existing){
                    return {
                        message :"حضور شما ثبت شده" ,
                        code : 500 ,
                    }
                }


                const data = Attendance({
                    className: _class.name , 
                    userId: user._id ,
                    classId : _class._id,
                    checkIn : time  ,
                    status : status ,
                    userFullName :fullname, 
                    date :date
                    })

                data.save()
                }
            } , 
            sendsms : async () => {
 try {
                const jDate = jalaali.toJalaali(new Date());
                const today = `${jDate.jy}/${jDate.jm}/${jDate.jd}`;

                const students = await User.find({ role: "Student" });
                const todayRecords = await Attendance.find({ date: today });

                const result = [];

                for (const stu of students) {
                const record = todayRecords.find(
                    a => a.userId.toString() === stu._id.toString()
                );

                if (record) {
                    
                    result.push({
                    id: record._id,          
                    studentId: stu._id,     
                    status: record.status,
                    checkIn: record.checkIn,
                    date: record.date
                    });
                } else{
                
                const user = await User.findById(stu._id)
                
                const fullname = user.firstname + " " + user.lastname

                if(!user){
                    return {
                        message : "این دانش آموز وجود ندارد",
                        code : 400 ,
                    }
                }

                const _class = await Class.findById(user.classId)

                console.log(_class)

                if(!_class){
                    return {
                        message : "این کلاس وجود ندارد" ,
                        code : 400 ,
                    }
                }

                
                const time = global.utils.time.iran() 

                console.log(time)

                const config = await global.utils.config.read()

                console.log(config)

                const jDate = jalaali.toJalaali(new Date())

                const date  = `${jDate.jy}/${jDate.jm}/${jDate.jd}`

                console.log(date)

                const existing = await Attendance.findOne({
                    userId: user._id,
                    date: date
                });

                console.log(existing)

                if(existing){
                    return {
                        message :"حضور شما ثبت شده" ,
                        code : 500 ,
                    }
                }


                const data = Attendance({
                    className: _class.name , 
                    userId: user._id ,
                    classId : _class._id,
                    checkIn : "--"  ,
                    status : "غایب" ,
                    userFullName :fullname, 
                    date :date
                    })

                data.save()
                }
                }

                const users = await Attendance.find()
                
                let student = ""

                let data =[]

                for (const i of users){

                    console.log(i)
                    if(i.status == "غایب"){
                    student = await User.findById(i.userId)
                    
                    global.sms.send.absent(student.ParentNumber , student.firstname + " " +student.lastname )

                    data.push([student.ParentNumber , student.firstname + " " +student.lastname])
                    }
                }
                    global.sms.send.manager("09173819218" , "آقا عسجدی")
                
                    return { code: 200, message:  data};
                

            } catch (err) {
                console.error("❌ ERROR getall:", err);
                return { code: 500, message: "خطا در دریافت حضور و غیاب" };
            }
            } , 
            changeStarttime : async (time) =>{
                await global.utils.config.edit(time)
            }, 
            getUserInfo : async (token) => { 

                try{
                    payload = global.utils.token.verify(token)
                }catch{
                    return {message : "توکن واقعی نیست " , code : 500}
                }

                const userId = payload.userId

                const Info = (await Attendance.find({userId : userId} , {"checkIn" : 1 , "status" :1  , "date" : 1  , "_id" :0 })).reverse()

                const presentCount = Info.filter(i => i.status === "حاضر").length
                 
                const latenessCount = Info.filter(i => i.status === "دیر اومده").length
                
                const absentCount = Info.filter(i => i.status === "غایب").length


                return {"Info" : Info , "total" : {"present" : presentCount , "lateness" :latenessCount ,  "absent" : absentCount}}
            }
        } , 
        otp : {
            SendWithNumber : async (num , role) => {
    
                const code = global.utils.random.getOtpCode()
                

                const user = await User.findOne({  
                    number : num 
                })

                console.log(user)
                
                if (!user){ 
                    return {message : "دسترسی غیر مجاز" , code : 500}
                }

                if(user.role != role){
                    return {message : "ادمین معتبر نیست" , code : 500}
                }

                const otpBlock = await Otp.findOne({
                    number  : num
                })


                console.log(otpBlock )
                if (otpBlock){
                    console.log("hi")
                    const checkTime = global.utils.time.CheckOtp(otpBlock.checkIn)

                    if (checkTime){
                        await global.sms.send.otp(num  ,code)

                        await otpBlock.deleteOne()

                        const o= Otp({
                            checkIn  : Date.now() ,  
                            code : code ,
                            number : num
                        })

                        o.save()

                        return {message : "کد ارسال شد !!!" , code : 200}
                    }else{
                        return {message : "لطفا دقایق دیگر تلاش کنید" , code : 500}
                    }

                }else{
                    
                    await global.sms.send.otp(num ,code)

                    const o= Otp({
                        checkIn  :  Date.now() , 
                        code : code ,
                        number : num , 
                        role : role
                    })

                    o.save()


                    return {message : "کد ارسال شد !!!" , code : 200}


                }


            } , 
            SendWithUid : async (uid) => {
    
            
                const code = global.utils.random.getOtpCode()
                
                let card = await Card.findOne({uid : uid})

                if(!card){
                    const DexUid = global.utils.hex.hexToDec(uid)
                    card = await Card.findOne({uid : DexUid})
                    if(!card){
                        return {message : "این کارت وجود ندارد" , code : 500}
                    }
                }


                const user = await User.findById(card.ownerId)

                console.log(user)

                const num = user.number
                
                if (!user){ 
                    return {message : "دسترسی غیر مجاز" , code : 500}
                }

                const otpBlock = await Otp.findOne({
                    number  : num
                })


                console.log(otpBlock )
                if (otpBlock){

                    const checkTime = global.utils.time.CheckOtp(otpBlock.checkIn)

                    if (checkTime){
                        await global.sms.send.otp(num  ,code)

                        await otpBlock.deleteOne()

                        const o= Otp({
                        checkIn  : Date.now() , 
                        code : code ,
                        number : num
                        })

                        o.save()

                        return {message : "کد ارسال شد !!!" , code : 200 , number : num}
                    }else{
                        return {message : "لطفا دقایق دیگر تلاش کنید" , code : 500}
                    }

                }else{
                    
                    await global.sms.send.otp(num ,code)

                    const o= Otp({
                        checkIn  :  Date.now(), 
                        code : code ,
                        number : num
                    })

                    o.save()

                    return {message : "کد ارسال شد !!!" , code : 200 , number : num}

                }

            } , 
            check : async (number , code) => { 
                
                const data  = await Otp.findOne({number : number , code : code })

                if(!data){
                    return {message : "کد اشتباه است" , code : 500}
                }

                await data.deleteOne()

                const user = await User.findOne({number : number})

                const userId = user._id

                const token = global.utils.token.newToken(userId , data.role)

                return {message : "کد وارد شده درست است !!!" , code : 200 , token : token}
                
            }
        } , 
        card : { 
            add : async (uid , ownerId) => {


                const checkUid = await Card.findOne({uid : uid})


                if(checkUid != null){
                    return {message : "این کارت برای فرد دیگری ثبت شده است" , code : 500}
                }

                const user = await User.findById(ownerId)

                const role = user.role

                console.log(role)

                if(user == null){
                    return {message : "این یوزر وجود ندارد" , code : 500}
                }


                const c = Card({
                    uid : uid , 
                    ownerId : ownerId , 
                    role  : role
                })

                c.save()

                return {message : "کارت با موفقیت اضافه شد !!" , code : 200}
            }
        } , 
        notification : {
            addToken : async (notificationToken , MainToken) => {
                 
                let payload
                let user 

                try{
                    payload = global.utils.token.verify(MainToken)
                }catch{
                    return {message : "توکن واقعی نیست " , code : 500}
                }

                console.log(payload.userId)

                try{
                    user = await User.findById(payload.userId)
                }catch{
                    return {message : "یوزر وجود ندارد" , code : 500}
                }


                console.log(notificationToken)
                await user.updateOne({notificationToken : notificationToken})

                return {message : "عملیات با موفقیت انجام شد!"  , code : 200}




                

                return {message : "توکن واقعی نیست " , code : 500}



            }
        }
    }

  } catch (e) {
    console.log("❌ Error:", e)
  }
}

module.exports = ConnectTodb
