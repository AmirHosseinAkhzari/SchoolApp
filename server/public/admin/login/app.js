// login.js — نسخه راست‌به‌چپ با چک وضعیت 500 و پیام خطای سرور

// تابع ارسال شماره برای دریافت OTP
async function Login(number) {
  try {
    const res = await fetch("http://192.168.1.9:3000/admin/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ number })
    });

    if (res.status === 500) {
      console.error("❌ خطای سرور (500)");
      const respo = await res.json();
      return { error: true, code: 500, message: respo.message || "خطای سرور" };
    }

    const data = await res.json();
    return { error: false, code: res.status, data };
  } catch (err) {
    console.error("❌ خطا در اتصال:", err);
    return { error: true, code: 0, message: "اتصال به سرور برقرار نشد" };
  }
}

// تابع بررسی OTP در سرور
async function Otp(code, number) {
  try {
    const res = await fetch("http://192.168.1.9:3000/admin/check", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ code, number })
    });

    if (res.status === 500) {
      console.error("❌ خطای سرور (500)");
      const respo = await res.json();
      return { error: true, code: 500, message: respo.message || "خطای سرور" };
    }

    const data = await res.json();
    return { error: false, code: res.status, data };
  } catch (err) {
    console.error("❌ خطا در اتصال:", err);
    return { error: true, code: 0, message: "اتصال به سرور برقرار نشد" };
  }
}

// عناصر صفحه
const phoneForm = document.getElementById("phoneForm");
const otpForm = document.getElementById("otpForm");
const msg = document.getElementById("loginMessage");
const otpMsg = document.getElementById("otpMessage");
const loginTitle = document.getElementById("loginTitle");

let tempPhone = "";

// مرحله اول: ارسال شماره برای OTP
phoneForm.addEventListener("submit", async (e) => {
  e.preventDefault();
  msg.textContent = "";

  const phone = document.getElementById("phone").value.trim();
  if (!/^09\d{9}$/.test(phone)) {
    msg.textContent = "شماره واردشده معتبر نیست.";
    return;
  }

  const result = await Login(phone);
  console.log("پاسخ لاگین:", result);

  if (result.error || result.code === 500) {
    msg.style.color = "red";
    msg.textContent = result.message || "❌ خطا در سرور. لطفاً بعداً تلاش کنید.";
    return;
  }

  msg.style.color = "limegreen";
  msg.textContent = "✅ کد ورود ارسال شد.";
  tempPhone = phone;

  phoneForm.style.display = "none";
  otpForm.style.display = "block";
  loginTitle.textContent = "تأیید کد ورود";

  const otpInputs = document.querySelectorAll(".otp-input");
  otpInputs[otpInputs.length - 1].focus();
});

// مرحله دوم: وارد کردن OTP
const otpInputs = document.querySelectorAll(".otp-input");
const rtlInputs = Array.from(otpInputs).reverse();

rtlInputs.forEach((input, index) => {
  input.addEventListener("input", async (e) => {
    const value = e.target.value.replace(/\D/g, "");
    e.target.value = value.slice(0, 1);

    if (value && index < rtlInputs.length - 1) {
      rtlInputs[index + 1].focus();
    }

    const otp = rtlInputs.map((i) => i.value).join("");
    if (otp.length === 5) {
      const result = await Otp(otp, tempPhone);
      console.log("پاسخ OTP:", result);

      if (result.error || result.code === 500) {
        otpMsg.style.color = "red";
        otpMsg.textContent = result.message || "❌ خطا در سرور. لطفاً بعداً تلاش کنید.";
        return;
      }

      otpMsg.style.color = "limegreen";
      otpMsg.textContent = "✅ ورود موفق!";
      setTimeout(() => {window.location.href = "/adminPanel"}, 1000);
    }
  });

  input.addEventListener("keydown", (e) => {
    if (e.key === "Backspace" && !input.value && index > 0) {
      rtlInputs[index - 1].focus();
    }
  });
});

