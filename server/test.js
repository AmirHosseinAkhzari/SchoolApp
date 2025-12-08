import admin from "firebase-admin";
import fs from "fs";

const serviceAccount = JSON.parse(
  fs.readFileSync("astin-5394e-firebase-adminsdk-fbsvc-692e872797.json", "utf8")
);

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
})

const token = "cKSy12JmSXK4a7Rshcd309:APA91bHL78YjMyuNsVpfujkpi4bgTMLFPNQjrx0tGiTQubht66EE4n68RTt4_QARXKg52mLXDimkotA6FhCf4LBozP1brLpzT2uP4zzmNk7PYUCG1PAzcW0"

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
