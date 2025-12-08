import admin from "firebase-admin";
import fs from "fs";

const serviceAccount = JSON.parse(
  fs.readFileSync("astin-5394e-firebase-adminsdk-fbsvc-692e872797.json", "utf8")
);

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
})

const token = "fPZ3JdTpSEWe6iTdL7zC_D:APA91bEXnMt7RjEP74NtsCVTVsu1RAmyJStmXvrIL71R2MUTCDI5JoPvkzbgEkQSLT2x2PTFo6NVzdZZFSxttdKmbPC0ndFFlw5y0hSGZ153nuw-u5aFNrg"
async function send() {
  try {
    const response = await admin.messaging().send({
      token: token,
      notification: {
        title: "سلام امیرحسین 👋🔥",
        body: "نوتیفیکیشن با کتابخونه رسمی ارسال شد",
      },
    });

    console.log("Sent:", response);
  } catch (err) {
    console.error("ERR", err);
  }
}

send();
