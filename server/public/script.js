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

    console.log('کارت خوانده شد:', cardData);

    try {
      const res = await fetch('/attendance/check', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ uid: cardData })
      });

      const data = await res.json();
      console.log('پاسخ سرور:', data);

      if (res.ok && data.code === 200) {
        // ✅ حالت موفق
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
        // ❌ خطا از سرور
        showError(data.message || "کارت شناسایی نشد");
      }
    } catch (err) {
      console.error('خطا در ارتباط با سرور:', err);
      showError("خطا در اتصال به سرور");
    }

    input.value = '';
  }
});

function showError(message) {
  tit.textContent =  message;
  nameBox.textContent = "";

 
  document.body.classList.add("error-mode");

  setTimeout(() => {
    document.body.classList.remove("error-mode");
    tit.textContent = "آستینتو بالا بزن، حضورتو ثبت کن";
    nameBox.textContent = "منتظر آستینم...";
  }, 2500);
}
