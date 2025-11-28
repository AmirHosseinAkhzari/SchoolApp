import admin from "firebase-admin";
import fs from "fs";

const serviceAccount = JSON.parse(
  fs.readFileSync("astin-5394e-firebase-adminsdk-fbsvc-692e872797.json", "utf8")
);

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
})

const token = "cl9sSnJPTYSyE2yIXXrdjN:APA91bEUEhCIzF9KOxzG85VK7Pv7tEPCAogdrZhooMccENbcOaVz266-7HqvvgTzgJLsFnB6vJd0t2kbUXyMIEKE_dmi-9fkczuR_EQrJPwTboZL-0a2OPQ"

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
