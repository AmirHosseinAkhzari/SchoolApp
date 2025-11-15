/* --------------------------------------------------
 * Admin panel state and initialization
 * -------------------------------------------------- */




function ReadClass() {
  return fetch("/class/read", {
    method: "GET",
    headers: { "Content-Type": "application/json" },
  })
    .then(res => res.json()) // ← خیلی مهم
    .catch(err => {
      console.error("FETCH ERROR:", err);
      return []
    });
}


function addClass(name) {
  return fetch("/class/add", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body : JSON.stringify({name : name})
  })
    .then(res => res.json()) 
    .catch(err => {
      console.error("FETCH ERROR:", err);
      return []
    });
}

function deleteClass(id) {
  return fetch("/class/delete", {
    method: "DELETE",
    headers: { "Content-Type": "application/json" },
    body : JSON.stringify({id : id})
  })
    .then(res => res.json()) 
    .catch(err => {
      console.error("FETCH ERROR:", err);
      return []
    });
}


function changeNameClass(id , name) {
    console.log("hi")
  return fetch("/class/changeName", {
    method: "PATCH",
    headers: { "Content-Type": "application/json" },
    body : JSON.stringify({id : id  , name : name})
  })
    .then(res => res.json()) 
    .catch(err => {
      console.error("FETCH ERROR:", err);
      return []
    });
}




function ReadStu() {
  return fetch("/student/read", {
    method: "GET",
    headers: { "Content-Type": "application/json" },
  })
    .then(res => res.json()) 
    .catch(err => {
      console.error("FETCH ERROR:", err);
      return []
    });
}


function ReadAttendance() {
  return fetch("/attendance/read", {
    method: "GET",
    headers: { "Content-Type": "application/json" },
  })
    .then(res => res.json()) 
    .catch(err => {
      console.error("FETCH ERROR:", err);
      return []
    });
}


function changeStatus(id , status) {
  return fetch("/attendance/changeStatus", {
    method: "PATCH",
    headers: { "Content-Type": "application/json" },
    body : JSON.stringify({id : id  , status : status})
  })
    .then(res => res.json()) 
    .catch(err => {
      console.error("FETCH ERROR:", err);
      return []
    });
}


function changeStarttime(time ) {
  return fetch("/attendance/changeStarttime", {
    method: "PATCH",
    headers: { "Content-Type": "application/json" },
    body : JSON.stringify({time : time})
  })
    .then(res => res.json()) 
    .catch(err => {
      console.error("FETCH ERROR:", err);
      return []
    });
}



function deleteStu(id) {
  return fetch("/student/delete", {
    method: "DELETE",
    headers: { "Content-Type": "application/json" },
    body : JSON.stringify({id : id})
  })
    .then(res => res.json()) 
    .catch(err => {
      console.error("FETCH ERROR:", err);
      return []
    });
}


const state = {
  classes: [],
  students: [],
  attendance: []
};

const uiState = {
  activePage: "dashboard",
  studentClassFilter: "all",
  attendanceFilters: {
    classId: "all",
    status: "all",
    search: ""
  }
};

const deleteContext = {
  entity: null,
  id: null
};

const editContext = {
  entity: null,
  id: null
};

let activeClassId = null;
let activeStudentId = null;

const content = document.getElementById("content");
const menuItems = Array.from(document.querySelectorAll(".menu-item"));

/* --------------------------------------------------
 * Setup and global event handlers
 * -------------------------------------------------- */
function init() {
  setupMenu();
  bindGlobalEvents();




      ReadStu().then(res => {
    state.students = res || [];
    })


    ReadClass().then(res => {
    state.classes = res || [];
    })

    ReadAttendance().then(res => {
    state.attendance = res.data || [];
    console.log(res.data)
    })




  renderPage(uiState.activePage);
}

function setupMenu() {
  menuItems.forEach((item) => {
    item.addEventListener("click", () => {
      const page = item.dataset.page;
      if (!page || page === uiState.activePage) return;
      renderPage(page);
    });
  });
}

function bindGlobalEvents() {
  content.addEventListener("click", handleContentClick);
  content.addEventListener("change", handleContentChange);
  content.addEventListener("input", handleContentInput);

  document.addEventListener("click", handleGlobalClick);

  document.getElementById("addClassForm").addEventListener("submit", handleAddClass);
  document.getElementById("editClassForm").addEventListener("submit", handleEditClass);
  document.getElementById("addStudentForm").addEventListener("submit", handleAddStudent);
  document.getElementById("editStudentForm").addEventListener("submit", handleEditStudent);

  document.getElementById("confirmDelete").addEventListener("click", handleDeleteConfirm);

  const addPhotoInput = document.getElementById("addStudentPhoto");
  const editPhotoInput = document.getElementById("studentPhoto");

  addPhotoInput.addEventListener("change", () => updatePhotoPreview(addPhotoInput, "addStudentPhotoPreview"));
  editPhotoInput.addEventListener("change", () => updatePhotoPreview(editPhotoInput, "studentPhotoPreview"));

//   const dayPicker = document.getElementById("dayPicker");
//   dayPicker.addEventListener("click", (event) => {
//     const dayButton = event.target.closest(".day");
//     if (dayButton) {
//       dayButton.classList.toggle("active");
//     }
//   });

  const endTimeInput = document.getElementById("attendanceEndTime");
  endTimeInput.addEventListener("input", (event) => {
    const value = event.target.value.trim();
    const isValid = !value || /^([01]?\d|2[0-3]):[0-5]\d$/.test(value);
    event.target.classList.toggle("invalid", !isValid);
  });
}

function handleGlobalClick(event) {
  const closeTarget = event.target.closest("[data-popup-close]");
  if (closeTarget) {
    closePopup(closeTarget.dataset.popupClose);
    return;
  }

  const photoTrigger = event.target.closest('[data-action="select-photo"]');
  if (photoTrigger) {
    const input = document.getElementById(photoTrigger.dataset.target);
    if (input) {
      input.click();
    }
  }
}

/* --------------------------------------------------
 * Rendering helpers
 * -------------------------------------------------- */
function renderPage(page) {
  uiState.activePage = page;
  updateMenuState(page);

  switch (page) {
    case "attendance":
      renderAttendance();
      break;
    case "students":
      renderStudents();
      break;
    case "classes":
      renderClasses();
      break;
    default:
      renderDashboard();
  }
}

function updateMenuState(page) {
  menuItems.forEach((item) => {
    item.classList.toggle("active", item.dataset.page === page);
  });
}

function renderDashboard() {
  const cards = [
    { title: "حضور امروز", value: countAttendanceByStatus("حاضر") },
    { title: "دیرکردها", value: countAttendanceByStatus("دیر اومده") },
    { title: "غیبت‌ها", value: countAttendanceByStatus("غایب") }
  ];

  const cardsMarkup = cards
    .map(
      (card) => `
        <div class="card">
          <div class="card-title">${card.title}</div>
          <div class="card-value">${card.value}</div>
        </div>
      `
    )
    .join("");

  content.innerHTML = `
    <section class="page-header">
      <div>
        <h2 class="page-title">داشبورد</h2>
        <p class="page-subtitle">نمای کلی سامانه حضور و غیاب آستین</p>
      </div>
    </section>
    <section class="cards">${cardsMarkup}</section>
  `;
}

function renderClasses() {
  ReadClass().then(rows => {

    state.classes = rows;  // ← ← ← خیلی مهم

    const htmlRows = rows
      .map((cls, index) => `
        <tr data-class-id="${cls._id}">
          <td>${index + 1}</td>
          <td>${cls.name}</td>
          <td class="actions">
            <button type="button" class="icon-btn edit-btn" data-action="edit-class" data-id="${cls._id}">
              <img src="icons/edit.svg" alt="edit">
            </button>
            <button type="button" class="icon-btn delete-btn" data-action="delete-item" data-entity="class" data-id="${cls._id}">
              <img src="icons/delete.svg" alt="delete">
            </button>
          </td>
        </tr>
      `).join("");

    content.innerHTML = `
      <section class="page-header">
        <div>
          <h2 class="page-title">کلاس‌ها</h2>
          <p class="page-subtitle">مدیریت کلاس‌ها</p>
        </div>
        <button type="button" class="btn primary add-btn" data-action="open-add-class">➕ افزودن کلاس</button>
      </section>
      <div class="table-wrap">
        <table class="class-table">
          <thead>
            <tr>
              <th>#</th>
              <th>نام کلاس</th>
              <th>عملیات</th>
            </tr>
          </thead>
          <tbody>${htmlRows}</tbody>
        </table>
      </div>
    `;
  });
}



function renderStudents() {
  ReadStu().then(rows => {

    state.students = rows; 
    
    const filteredStudents = getFilteredStudents();

    const htmlRows = filteredStudents
      .map(
        (student, index) => `
          <tr data-student-id="${student._id}">
            <td>${index + 1}</td>
            <td><img src="/image/profile/?id=${student._id}" alt="student" class="profile-pic" /></td>
            <td>${getClassName(student.classId)}</td>
            <td>${student.firstname}</td>
            <td>${student.lastname}</td>
            <td class="actions">
              <button type="button" class="icon-btn edit-btn" data-action="edit-student" data-id="${student._id}">
                <img src="icons/edit.svg" alt="edit" />
              </button>
              <button type="button" class="icon-btn delete-btn" data-action="delete-item" data-entity="student" data-id="${student._id}">
                <img src="icons/delete.svg" alt="delete" />
              </button>
            </td>
          </tr>
        `
      )
      .join("");

    const classOptions = buildClassOptions(uiState.studentClassFilter, {
      includeAll: true,
      allLabel: "همه کلاس‌ها"
    });

    content.innerHTML = `
      <section class="page-header">
        <div>
          <h2 class="page-title">دانش‌آموزان</h2>
          <p class="page-subtitle">مدیریت اطلاعات دانش‌آموزان</p>
        </div>
        <div class="page-actions">
          <select id="studentClassFilter" class="filter-select">${classOptions}</select>
          <button type="button" class="btn primary add-btn" data-action="open-add-student">➕ افزودن دانش‌آموز</button>
        </div>
      </section>
      <div class="table-wrap">
        <table class="class-table students-table">
          <thead>
            <tr>
              <th>#</th>
              <th>عکس</th>
              <th>کلاس</th>
              <th>نام</th>
              <th>نام خانوادگی</th>
              <th>عملیات</th>
            </tr>
          </thead>
          <tbody>${htmlRows}</tbody>
        </table>
      </div>
    `;
  });
}


function renderAttendance() {
  const rows = getFilteredAttendance()
    .map((record, index) => {
      const student = getStudentById(record.studentId);
      if (!student) return "";
      const statusConfig = getStatusConfig(record.status);
      return `
        <tr data-attendance-id="${record.id}">
          <td>${index + 1}</td>
          <td><img src="/image/profile/?id=${student._id}" alt="student" class="profile-pic" /></td>
          <td>${student.firstname} ${student.lastname}</td>
          <td>${getClassName(student.classId)}</td>
          <td><span class="status-text" style="color:${statusConfig.color}">${record.status}</span></td>
          <td class="actions">
            <div class="status-switch" data-id="${record.id}">
              ${renderStatusDots(record)}
            </div>
          </td>
        </tr>
      `;
    })
    .join("");

  const classOptions = buildClassOptions(uiState.attendanceFilters.classId, {
    includeAll: true,
    allLabel: "همه کلاس‌ها"
  });

  content.innerHTML = `
    <section class="page-header">
      <div>
        <h2 class="page-title">حضور و غیاب</h2>
        <p class="page-subtitle">پیگیری وضعیت حضور دانش‌آموزان</p>
      </div>
      <button type="button" class="btn secondary" data-action="open-settings">تنظیمات</button>
    </section>
    <div class="filter-bar">
      <div class="filter-group">
        <label for="filterClass">کلاس:</label>
        <select id="filterClass" class="filter-select">${classOptions}</select>
      </div>
      <div class="filter-group">
        <label for="filterStatus">وضعیت:</label>
        <select id="filterStatus" class="filter-select">
          ${buildStatusOptions(uiState.attendanceFilters.status)}
        </select>
      </div>
      <div class="filter-group search-field">
        <input type="text" id="searchName" placeholder="جستجو بر اساس نام..." value="${uiState.attendanceFilters.search}" />
      </div>
    </div>
    <div class="table-wrap">
      <table class="class-table students-table">
        <thead>
          <tr>
            <th>#</th>
            <th>عکس</th>
            <th>نام کامل</th>
            <th>کلاس</th>
            <th>وضعیت</th>
            <th>عملیات</th>
          </tr>
        </thead>
        <tbody id="attendanceBody">${rows}</tbody>
      </table>
    </div>
  `;
}

/* --------------------------------------------------
 * Event handlers for page content
 * -------------------------------------------------- */
function handleContentClick(event) {
  const actionButton = event.target.closest("[data-action]");
  if (actionButton) {
    const { action } = actionButton.dataset;
    switch (action) {
      case "open-add-class":
        openPopup("addClassPopup");
        document.getElementById("addClassInput").focus();
        return;
      case "edit-class":
        openEditClass(actionButton.dataset.id);
        return;
      case "delete-item":
        openDeletePopup(actionButton.dataset.entity, actionButton.dataset.id);
        return;
      case "open-add-student":
        openAddStudentPopup();
        return;
      case "edit-student":
        openEditStudentPopup(actionButton.dataset.id);
        return;
      case "open-settings":
        openPopup("settingsPopup");
        return;
      case "set-status":
        handleStatusChange(actionButton.dataset.id, actionButton.dataset.status);
        return;
      default:
        break;
    }
  }

  const row = event.target.closest("tr[data-attendance-id]");
  if (row && !event.target.closest(".status-switch")) {
    openAttendanceDetail(row.dataset.attendanceId);
  }
}

function handleContentChange(event) {
  if (event.target.id === "studentClassFilter") {
    uiState.studentClassFilter = event.target.value;
    renderPage("students");
  }

  if (event.target.id === "filterClass") {
    uiState.attendanceFilters.classId = event.target.value;
    renderPage("attendance");
  }

  if (event.target.id === "filterStatus") {
    uiState.attendanceFilters.status = event.target.value;
    renderPage("attendance");
  }
}

function handleContentInput(event) {
  if (event.target.id === "searchName") {
    uiState.attendanceFilters.search = event.target.value.trim();
    renderPage("attendance");
  }
}

function handleAddClass(event) {
  event.preventDefault();
  const input = document.getElementById("addClassInput");
  const name = input.value.trim();
  if (!name) return;

    addClass(name).then(() => {
        closePopup("addClassPopup");
        renderPage("classes");
    });

}

function openEditClass(id) {
  const cls = state.classes.find((item) => item._id === id);
  if (!cls) {
    console.log("CLASS NOT FOUND", id, state.classes);
    return;
  }

  activeClassId = id;
  document.getElementById("editClassInput").value = cls.name;
  openPopup("editClassPopup");
}


function handleEditClass(event) {
  event.preventDefault();
  console.log("hi")
  if (!activeClassId) return;
  console.log("hi")

  const cls = state.classes.find((item) => item._id === activeClassId);
  if (!cls) return;
  console.log("hi")


  const newName = document.getElementById("editClassInput").value
changeNameClass(activeClassId, newName).then(() => {
    closePopup("editClassPopup");
    renderPage("classes");
});


  activeClassId = null;
}

function openAddStudentPopup() {
  const form = document.getElementById("addStudentForm");
  form.reset();
  document.getElementById("addStudentPhotoPreview").src = "/image/profile/?id=asdasdad";
  populateClassSelect("addStudentClass", "", { includePlaceholder: true });
  openPopup("addStudentPopup");
}

function handleAddStudent(event) {
  event.preventDefault();

  // 1) گرفتن مقادیر خام از input ها
  const firstNameInput = document.getElementById("addFirstName").value.trim();
  const lastNameInput  = document.getElementById("addLastName").value.trim();
  const classIdInput   = document.getElementById("addStudentClass").value;
  const birthdayInput  = document.getElementById("addBirth").value.trim();
  const nationalIdInput = document.getElementById("addNationalId").value.trim();
  const numberInput      = document.getElementById("addStudentNumber").value.trim();
  const parentPhoneInput = document.getElementById("addParentPhone").value.trim();
  const homePhoneInput   = document.getElementById("addHomePhone").value.trim();

  // 2) چک خالی نبودن اسم و فامیل
  if (!firstNameInput || !lastNameInput) {
    alert("نام و نام خانوادگی نباید خالی باشد.");
    return;
  }

  // 3) چک کردن اینکه classId حتماً یکی از کلاس‌های state باشد
  const classObj = state.classes.find(cls => cls.name === classIdInput || cls._id === classIdInput);

  console.log(state.classes)
  console.log(classIdInput)
  if (!classObj) {
    alert("کلاس انتخاب‌شده نامعتبر است.");
    return;
  }

  // 4) ولیدیشن تاریخ جلالی
  if (!isValidJalaliDate(birthdayInput)) {
    alert("تاریخ تولد باید به صورت جلالی و با فرمت YYYY/MM/DD باشد، مثلا 1340/10/19");
    return;
  }

  // 5) ولیدیشن کد ملی (۱۰ رقم)
  const nationalId = normalizeNationalId(nationalIdInput);
  if (!nationalId) {
    alert("کد ملی باید دقیقا ۱۰ رقم باشد.");
    return;
  }

  // 6) ولیدیشن شماره‌ها (۱۱ رقم، شروع با ۰)
  const number = normalizeIranPhone(numberInput);
  if (!number) {
    alert("شماره دانش‌آموز باید ۱۱ رقمی و با ۰ شروع شود (مثلاً 0930...).");
    return;
  }

  const ParentNumber = normalizeIranPhone(parentPhoneInput);
  if (!ParentNumber) {
    alert("شماره ولی باید ۱۱ رقمی و با ۰ شروع شود.");
    return;
  }

  const LocalNumber = normalizeIranPhone(homePhoneInput);
  if (!LocalNumber) {
    alert("شماره ثابت باید ۱۱ رقمی و با ۰ شروع شود (مثلاً 0715...).");
    return;
  }

  // 7) ساخت body نهایی برای API (خیلی مهم: اسم فیلدها)
  const body = {
    firstname: firstNameInput,          // دقت به کوچکی/بزرگی حروف
    lastname:  lastNameInput,
    nationalid: nationalId,
    number: number,
    ParentNumber: ParentNumber,
    LocalNumber: LocalNumber,
    role: "Student",
    birthday: birthdayInput,
    classId: classObj._id || classObj.id
  };

  console.log("STUDENT PAYLOAD:", body);

  // 8) ارسال به API
  fetch("/student/add", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body)
  })
    .then(res => res.json())
    .then(data => {
      console.log("STUDENT ADD RESPONSE:", data);

      // اگر خواستی می‌تونی چک کنی data.code === 200 و بعد:
      closePopup("addStudentPopup");
      renderPage("students");
    })
    .catch(err => {
      console.error("STUDENT ADD ERROR:", err);
      alert("خطا در ذخیره دانش‌آموز. لطفاً دوباره تلاش کنید.");
    });
}


function openEditStudentPopup(id) {
  const student = state.students.find((item) => item.id === id);
  if (!student) return;
  activeStudentId = id;

  document.getElementById("studentPhotoPreview").src = student.photo;
  document.getElementById("studentFirstName").value = student.firstName;
  document.getElementById("studentLastName").value = student.lastName;
  document.getElementById("studentBirth").value = student.birthday;
  document.getElementById("studentNationalId").value = student.nationalId || "";
  document.getElementById("number").value = student.number || "";
  document.getElementById("studentParentPhone").value = student.ParentNumber || "";
  document.getElementById("studentHomePhone").value = student.LocalNumber || "";

  populateClassSelect("studentClass", student.classId, { includePlaceholder: true });
  openPopup("editStudentPopup");
}

function handleEditStudent(event) {
  event.preventDefault();
  if (!activeStudentId) return;

  const student = state.students.find((item) => item.id === activeStudentId);
  if (!student) return;

  student.photo = document.getElementById("studentPhotoPreview").src;
  student.firstName = document.getElementById("studentFirstName").value.trim();
  student.lastName = document.getElementById("studentLastName").value.trim();
  student.birthday = document.getElementById("studentBirth").value.trim();
  student.nationalId = document.getElementById("studentNationalId").value.trim();
  student.number = document.getElementById("number").value.trim();
  student.ParentNumber = document.getElementById("studentParentPhone").value.trim();
  student.LocalNumber = document.getElementById("studentHomePhone").value.trim();
  student.classId = document.getElementById("studentClass").value;

  activeStudentId = null;
  closePopup("editStudentPopup");
  renderPage("students");
}

function openDeletePopup(entity, id) {
  deleteContext.entity = entity;
  deleteContext.id = id;

  const title = document.getElementById("deleteTitle");
  const message = document.getElementById("deleteMessage");

  if (entity === "student") {
    title.textContent = "حذف دانش‌آموز";
    message.textContent = "با حذف این دانش‌آموز تمام سوابق حضور و غیاب او نیز حذف می‌شود.";
  } else {
    title.textContent = "حذف کلاس";
    message.textContent = "با حذف این کلاس تمام دانش‌آموزان مرتبط نیز حذف خواهند شد.";
  }

  openPopup("deletePopup");
}

function handleDeleteConfirm() {
  if (!deleteContext.entity || !deleteContext.id) {
    closePopup("deletePopup");
    return;
  }

  // حذف دانش‌آموز
  if (deleteContext.entity === "student") {

    deleteStu(deleteContext.id).then(() => {

      closePopup("deletePopup");

      // رندر دوباره دانش‌آموزها
      renderPage("students");
    });

    return;
  }

  // حذف کلاس
  if (deleteContext.entity === "class") {

    deleteClass(deleteContext.id).then(() => {
      closePopup("deletePopup");
      renderPage("classes");
    });

    return;
  }
}


function handleStatusChange(attendanceId, status) {
  const record = state.attendance.find((item) => item.id === attendanceId);
  
    changeStatus(attendanceId , status)
  if (!record) return;
  record.status = status;
  renderPage("attendance");
}

function openAttendanceDetail(attendanceId) {
  const record = state.attendance.find((item) => item.id === attendanceId);
  if (!record) return;
  const student = getStudentById(record.studentId);
  if (!student) return;

  document.getElementById("detailName").textContent = `${student.firstname} ${student.lastname}`;
  document.getElementById("detailClass").textContent = getClassName(student.classId);
  document.getElementById("detailDate").textContent = record.date;
  document.getElementById("detailCheckIn").textContent = record.checkIn;

  const statusInfo = getStatusConfig(record.status);
  const statusElement = document.getElementById("detailStatus");
  statusElement.textContent = record.status;
  statusElement.style.color = statusInfo.color;

  openPopup("attendanceDetailPopup");
}

/* --------------------------------------------------
 * Utility helpers
 * -------------------------------------------------- */
function buildClassOptions(selectedId, options = {}) {
  const { includeAll = false, allLabel = "همه", includePlaceholder = false, placeholderLabel = "انتخاب کنید…" } = options;
  let html = "";

  if (includeAll) {
    const isSelected = selectedId === "all";
    html += `<option value="all"${isSelected ? " selected" : ""}>${allLabel}</option>`;
  }

  if (includePlaceholder) {
    const isSelected = !selectedId;
    html += `<option value="" disabled${isSelected ? " selected" : ""}>${placeholderLabel}</option>`;
  }

  html += state.classes
    .map((cls) => `<option value="${cls._id}"${cls._id === selectedId ? " selected" : ""}>${cls.name}</option>`)
    .join("");

  return html;
}

function populateClassSelect(selectId, selectedId = "", options = {}) {
  const select = document.getElementById(selectId);
  if (!select) return;
  select.innerHTML = buildClassOptions(selectedId, options);
}

function getFilteredStudents() {
    console.log(uiState.studentClassFilter )
  if (uiState.studentClassFilter === "all") {
    return state.students;
  }

  console.log(state.students)
  return state.students.filter((student) => student.classId === uiState.studentClassFilter);
}

function getFilteredAttendance() {
  const { classId, status, search } = uiState.attendanceFilters;
  return state.attendance.filter((record) => {
    const student = getStudentById(record.studentId);
    if (!student) return false;

    const matchesClass = classId === "all" || student.classId === classId;
    const matchesStatus = status === "all" || record.status === status;
    const fullName = `${student.firstName} ${student.lastName}`;
    const matchesSearch = !search || fullName.includes(search);

    return matchesClass && matchesStatus && matchesSearch;
  });
}

function buildStatusOptions(selectedStatus) {
  const options = [
    { value: "all", label: "همه" },
    { value: "حاضر", label: "حاضر" },
    { value: "دیر اومده", label: "دیر اومده" },
    { value: "غایب", label: "غایب" }
  ];

  return options
    .map((option) => `<option value="${option.value}"${option.value === selectedStatus ? " selected" : ""}>${option.label}</option>`)
    .join("");
}

function renderStatusDots(record) {
  const options = [
    { value: "حاضر", className: "green" },
    { value: "دیر اومده", className: "orange" },
    { value: "غایب", className: "red" }
  ];

  return options
    .map((option) => {
      const isActive = record.status === option.value;
      return `
        <button type="button" class="status-dot ${option.className}${isActive ? " active" : ""}" data-action="set-status" data-id="${record.id}" data-status="${option.value}"></button>
      `;
    })
    .join("");
}

function getStatusConfig(status) {
  switch (status) {
    case "حاضر":
      return { color: "#2ecc71" };
    case "دیر اومده":
      return { color: "#f39c12" };
    case "غایب":
      return { color: "#e74c3c" };
    default:
      return { color: "var(--text)" };
  }
}

function updatePhotoPreview(input, previewId) {
  const file = input.files?.[0];
  if (!file) return;
  const reader = new FileReader();
  reader.onload = (event) => {
    const preview = document.getElementById(previewId);
    if (preview) {
      preview.src = event.target?.result;
    }
  };
  reader.readAsDataURL(file);
}

function openPopup(id) {
  const popup = document.getElementById(id);
  if (!popup) return;
  popup.classList.add("active");
  if (popup.classList.contains("full")) {
    document.body.classList.add("no-scroll");
  }
}

function closePopup(id) {
  const popup = document.getElementById(id);
  if (!popup) return;
  popup.classList.remove("active");
  if (popup.classList.contains("full")) {
    document.body.classList.remove("no-scroll");
  }
}

function getStudentById(id) {

  return state.students.find((student) => student._id === id)
}

function getClassName(classId) {
   console.log(classId , )
   console.log(state.classes)
  return state.classes.find((cls) => cls._id === classId)?.name || "—";
}

function countAttendanceByStatus(status) {
  return state.attendance.filter((record) => record.status === status).length;
}

function generateId(prefix) {
  return `${prefix}-${Math.random().toString(36).slice(2, 9)}`;
}



// چک کردن تاریخ جلالی به فرمت YYYY/MM/DD
function isValidJalaliDate(str) {
  const m = str.match(/^(\d{4})\/(\d{2})\/(\d{2})$/);
  if (!m) return false;

  const year = Number(m[1]);
  const month = Number(m[2]);
  const day = Number(m[3]);

  if (month < 1 || month > 12) return false;
  if (day < 1 || day > 31) return false;

  // ماه‌های ۷ تا ۱۱ حداکثر ۳۰ روز
  if (month > 6 && day > 30) return false;
  // اسفند (۱۲) حداکثر ۲۹ (خیلی دقیق نیست ولی برای ولیدیشن فرمی خوبه)
  if (month === 12 && day > 29) return false;

  return true;
}

// کد ملی: دقیقا ۱۰ رقم
function normalizeNationalId(value) {
  const digits = value.replace(/\D/g, "");
  return digits.length === 10 ? digits : null;
}

// شماره تلفن ایران (موبایل یا ثابت): ۱۱ رقم و شروع با ۰
function normalizeIranPhone(value) {
  const digits = value.replace(/\D/g, "");
  if (digits.length === 11 && digits.startsWith("0")) {
    return digits;
  }
  return null;
}


document.getElementById("sendSmsBtn").addEventListener("click", async () => {
  const res = await fetch("/attendance/sendsms", { method: "GET" });
  const data = await res.json();
  alert("پیامک‌ها ارسال شدند ✅");
});


document.getElementById("saveSettings").addEventListener("click", async () => {
  console.log("✅ دکمه ذخیره تنظیمات زده شد");

  // گرفتن مقدار از input
  const endTime = document.getElementById("attendanceEndTime").value.trim();

  // ولیدیشن ساده
  if (!endTime) {
    alert("لطفاً ساعت پایان را وارد کنید!");
    return;
  }
  if (!/^([01]?\d|2[0-3]):[0-5]\d$/.test(endTime)) {
    alert("فرمت ساعت باید به صورت HH:MM باشد، مثلاً 14:45");
    return;
  }

  changeStarttime(endTime)


})

init();