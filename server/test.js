function checkTimeStatus(time, start, end) {
    const [h1, m1] = time.split(":").map(Number);
    const [h2, m2] = start.split(":").map(Number);
    const [h3, m3] = end.split(":").map(Number);

    const timeMinutes = h1 * 60 + m1;
    const startMinutes = h2 * 60 + m2;
    const endMinutes = h3 * 60 + m3;

    if (timeMinutes < startMinutes) return "زود اومده";
    if (timeMinutes > endMinutes) return "دیر اومده";
    return "سر تایم";
}

// تست
console.log(checkTimeStatus("06:30", "07:00", "12:00")); // زود اومده
console.log(checkTimeStatus("08:45", "07:00", "12:00")); // سر تایم
console.log(checkTimeStatus("12:30", "07:00", "12:00")); // دیر اومده
