// ==== Wake Lock ====
let wakeLock = null;

async function enableWakeLock() {
  try {
    if ('wakeLock' in navigator) {
      wakeLock = await navigator.wakeLock.request('screen');
      console.log('Wake Lock فعال شد');

      wakeLock.addEventListener('release', () => {
        console.log('Wake Lock آزاد شد');
      });
    }
  } catch (err) {
    console.error('Wake Lock خطا:', err);
  }
}

// ==== گرفتن لوگو ====
const logo = document.querySelector('.logo');

// وقتی روی لوگو کلیک شد → fullscreen + wake lock
logo.addEventListener('click', async () => {
  const elem = document.documentElement;

  if (elem.requestFullscreen) {
    await elem.requestFullscreen();
  } else if (elem.webkitRequestFullscreen) {
    await elem.webkitRequestFullscreen();
  } else if (elem.msRequestFullscreen) {
    await elem.msRequestFullscreen();
  }

  // فعال‌سازی جلوگیری از خاموش شدن صفحه
  enableWakeLock();
});

// جلوگیری از خروج از fullscreen
document.addEventListener('fullscreenchange', () => {
  if (!document.fullscreenElement) {
    const elem = document.documentElement;
    if (elem.requestFullscreen) elem.requestFullscreen();
  }
});

// جلوگیری از ESC
document.addEventListener('keydown', (e) => {
  if (e.key === "Escape") {
    e.preventDefault();
    const elem = document.documentElement;
    if (elem.requestFullscreen) elem.requestFullscreen();
  }
});


// ==== کد موجود شما برای input و کارت خوان ====
const input = document.getElementById('hidden-input');
const nameBox = document.getElementById('nameBox');
const tit = document.getElementById('tit');
const flash = document.getElementById('flash');

function keepFocus() {
  if (document.activeElement !== input) input.focus();
}
input.focus();
setInterval(keepFocus, 100);

input.addEventListener('keydown', async (event) => {
  if (event.key === 'Enter') {
    const cardData = input.value.trim();
    if (cardData === '') return;

    try {
      const res = await fetch('/attendance/check', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ uid: cardData })
      });

      const data = await res.json();

      if (res.ok && data.code === 200) {
        flash.classList.remove('shrink');
        flash.classList.add('expand');

        document.body.style.color = "black";
        tit.textContent = "حضورت ثبت شد";
        nameBox.textContent = data.message;

        setTimeout(() => {
          flash.classList.remove('expand');
          flash.classList.add('shrink');
          document.body.style.color = "white";
          tit.textContent = "آستینتو بالا بزن، حضورتو ثبت کن";
          nameBox.textContent = "منتظر آستینم...";
        }, 2000);
      } else {
        showError(data.message || "کارت شناسایی نشد");
      }
    } catch (err) {
      showError("خطا در اتصال به سرور");
    }

    input.value = '';
  }
});

function showError(message) {
  tit.textContent = message;
  nameBox.textContent = "";
  document.body.classList.add("error-mode");

  setTimeout(() => {
    document.body.classList.remove("error-mode");
    tit.textContent = "آستینتو بالا بزن، حضورتو ثبت کن";
    nameBox.textContent = "منتظر آستینم...";
  }, 2500);
}
