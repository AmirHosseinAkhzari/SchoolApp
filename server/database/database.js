

const mongoose = require('mongoose')


const utils = require('../utils/utils')

const jalaali = require('jalaali-js');

const jwt  = require('jsonwebtoken');
const { use } = require('../router/login');

// ------- User Schema -------
const userSchema = new mongoose.Schema({
    firstname : String , 
    lastname : String , 
    birthday : Date , 
    nationalid : String , 
    number : String , 
    ParentNumber : String , 
    LocalNumber : String , 
    classId : mongoose.Schema.Types.ObjectId  , 
    role : String
})


// ------- class Schema -------
const classSchema = new mongoose.Schema({
    name : String , 
})


// ----------- OPT Schema ------------
const otpSchema = new mongoose.Schema({
    code : String  , 
    number : String ,  
    checkIn : String , 
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
    //   studentNumber: "123456",
    //   parentPhone: "۰۹۱۲۳۴۵۶۷۸۹",
    //   homePhone: "۰۷۱۵۲۳۴۵۶۷۸",
    //   photo: "https://i.pravatar.cc/150?u=1"
    // },
     global.db = {
        user : { 
            add : async (data) => { 
                const s = new User(data)
                await s.save()    
            } , 
            update : async (id , data) => { 
                await User.findByIdAndUpdate(id , data , {new : true})
            }  , 
            delete : async ( id) => { 
                await User.findByIdAndDelete(id )
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
                return await User.find().select("_id firstname lastname birthday nationalid number ParentNumber LocalNumber classId")
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
                    return {
                        message :"حضور شما ثبت شده" ,
                        code : 500 ,
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
            getall : async () => {
                return await Attendance.find()
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
                        checkIn  : global.utils.time.iran2() , 
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
                        checkIn  : global.utils.time.iran2() , 
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
                
                const card = await Card.findOne({uid : uid})

                if(!card){
                    return {message : "این کارت وجود ندارد" , code : 500}

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
                        checkIn  : global.utils.time.iran2() , 
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
                        checkIn  : global.utils.time.iran2() , 
                        code : code ,
                        number : num
                    })

                    o.save()


                    return {message : "کد ارسال شد !!!" , code : 200}


                }

            } , 
            check : async (number , code) => { 
                
                const data  = await Otp.findOne({number : number , code : code })

                if(!data){
                    return {message : "کد اشتباه است" , code : 500}
                }


                await data.deleteOne()

                const SECRET_KEY = "asdeashndjoasndfekswfeiw0mrfmn4rj24u9fr"

                const payload  = {
                    number : number , 
                    role : data.role
                }

                const token = jwt.sign(payload , SECRET_KEY )

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
        }
    }

  } catch (e) {
    console.log("❌ Error:", e)
  }
}

module.exports = ConnectTodb
