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


function readAdmins() {
  return fetch("/adminControl/read", {
    method: "GET",
    headers: { "Content-Type": "application/json" },
  })
    .then(res => res.json())
    .catch(err => {
      console.error("FETCH ERROR:", err);
      return [];
    });
}

function addAdmin(firstname, lastname, number) {
  return fetch("/adminControl/add", {
    method: "PATCH", // طبق مستندات شما PATCH است
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ firstname, lastname, number })
  })
    .then(res => res.json());
}

function deleteAdmin(id) {
  return fetch("/adminControl/delete", {
    method: "DELETE",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ id: id })
  })
    .then(res => res.json());
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

function ReadCards() {
  return fetch("/card/read", {
    method: "GET",
    headers: { "Content-Type": "application/json" },
  })
    .then(res => res.json()) 
    .catch(err => {
      console.error("FETCH ERROR:", err);
      return []
    });
}

function addCard(uid, ownerId) {
  return fetch("/card/add", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({uid: uid, ownerId: ownerId})
  })
    .then(res => res.json()) 
    .catch(err => {
      console.error("FETCH ERROR:", err);
      return {code: 500, message: "خطا در ارتباط با سرور"}
    });
}

function deleteCard(id) {
  return fetch("/card/delete", {
    method: "DELETE",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({id: id})
  })
    .then(res => res.json()) 
    .catch(err => {
      console.error("FETCH ERROR:", err);
      return {code: 500, message: "خطا در ارتباط با سرور"}
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
  attendance: [],
  cards: [] , 
  admins: [] 
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

  // Load all data in parallel, then render
  Promise.all([
    ReadStu().then(res => {
      state.students = res || [];
    }),
    ReadClass().then(res => {
      state.classes = res || [];
    }),
    ReadAttendance().then(res => {
      state.attendance = res.data || [];
      console.log("Attendance loaded:", res.data);
    }),
    ReadCards().then(res => {
      state.cards = res || [];
    }) , 
    readAdmins().then(res => state.admins = res || [])
    
  ]).then(() => {
    renderPage(uiState.activePage);
  });
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
  document.getElementById("addCardForm").addEventListener("submit", handleAddCard);

  document.getElementById("addAdminForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  const fname = document.getElementById("adminFirstname").value;
  const lname = document.getElementById("adminLastname").value;
  const num = document.getElementById("adminNumber").value;

  const res = await addAdmin(fname, lname, num);
  if (res.code === 200) {
    closePopup("addAdminPopup");
    renderAdmins();
  } else {
    alert(res.message);
  }
});
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
    case "cards":
      renderCards();
      break;
    case "admins":
      renderAdmins();
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
  // Show loading state
  content.innerHTML = `
    <section class="page-header">
      <div>
        <h2 class="page-title">داشبورد</h2>
        <p class="page-subtitle">نمای کلی سامانه حضور و غیاب آستین</p>
      </div>
    </section>
    <section class="cards">
      <div class="card">
        <div class="card-title">در حال بارگذاری...</div>
        <div class="card-value">—</div>
      </div>
    </section>
  `;
  
  // Refresh attendance data when rendering dashboard
  ReadAttendance().then(res => {
    state.attendance = res.data || [];
    
    const cards = [
      { title: "حضور امروز", value: countAttendanceByStatus("حاضر"), icon: "✅" },
      { title: "دیرکردها", value: countAttendanceByStatus("دیر اومده"), icon: "⏰" },
      { title: "غیبت‌ها", value: countAttendanceByStatus("غایب"), icon: "❌" }
    ];

    const cardsMarkup = cards
      .map(
        (card) => `
          <div class="card">
            <div class="card-title">${card.icon} ${card.title}</div>
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
  }).catch(err => {
    console.error("Error loading attendance:", err);
    content.innerHTML = `
      <section class="page-header">
        <div>
          <h2 class="page-title">داشبورد</h2>
          <p class="page-subtitle">نمای کلی سامانه حضور و غیاب آستین</p>
        </div>
      </section>
      <section class="cards">
        <div class="card">
          <div class="card-title">خطا در بارگذاری داده‌ها</div>
          <div class="card-value">—</div>
        </div>
      </section>
    `;
  });
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

function renderCards() {
  ReadCards().then(cards => {
    state.cards = cards || [];
    
    if (state.cards.length === 0) {
      content.innerHTML = `
        <section class="page-header">
          <div>
            <h2 class="page-title">کارت‌ها</h2>
            <p class="page-subtitle">مدیریت کارت‌های NFC دانش‌آموزان</p>
          </div>
          <button type="button" class="btn primary add-btn" data-action="open-add-card">➕ افزودن کارت</button>
        </section>
        <div class="empty-state">
          <p>هنوز کارتی ثبت نشده است</p>
          <p class="empty-state-subtitle">برای شروع، کارت جدیدی اضافه کنید</p>
        </div>
      `;
      return;
    }
    
    const htmlRows = state.cards
      .map((card, index) => {
        // Handle both populated and non-populated ownerId
        const ownerId = card.ownerId?._id || card.ownerId;
        const student = ownerId ? state.students.find(s => s._id === ownerId || s._id?.toString() === ownerId?.toString()) : null;
        const studentName = student ? `${student.firstname || ""} ${student.lastname || ""}`.trim() : "—";
        const studentClass = student ? getClassName(student.classId) : "—";
        
        return `
          <tr data-card-id="${card._id}">
            <td>${index + 1}</td>
            <td><code class="uid-code">${card.uid || "—"}</code></td>
            <td>${studentName}</td>
            <td>${studentClass}</td>
            <td><span class="role-badge">${card.role || "—"}</span></td>
            <td class="actions">
              <button type="button" class="icon-btn delete-btn" data-action="delete-item" data-entity="card" data-id="${card._id}">
                <img src="icons/delete.svg" alt="delete">
              </button>
            </td>
          </tr>
        `;
      })
      .join("");

    content.innerHTML = `
      <section class="page-header">
        <div>
          <h2 class="page-title">کارت‌ها</h2>
          <p class="page-subtitle">مدیریت کارت‌های NFC دانش‌آموزان (${state.cards.length} کارت)</p>
        </div>
        <button type="button" class="btn primary add-btn" data-action="open-add-card">➕ افزودن کارت</button>
      </section>
      <div class="table-wrap">
        <table class="class-table students-table">
          <thead>
            <tr>
              <th>#</th>
              <th>UID کارت</th>
              <th>دانش‌آموز</th>
              <th>کلاس</th>
              <th>نقش</th>
              <th>عملیات</th>
            </tr>
          </thead>
          <tbody>${htmlRows}</tbody>
        </table>
      </div>
    `;
  });
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
      case "open-add-card":
        openAddCardPopup();
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
  const student = state.students.find((item) => item._id === id || item.id === id);
  if (!student) return;
  activeStudentId = student._id || student.id;

  document.getElementById("studentPhotoPreview").src = `/image/profile/?id=${activeStudentId}`;
  document.getElementById("studentFirstName").value = student.firstname || "";
  document.getElementById("studentLastName").value = student.lastname || "";
  document.getElementById("studentBirth").value = student.birthday || "";
  document.getElementById("studentNationalId").value = student.nationalid || "";
  document.getElementById("number").value = student.number || "";
  document.getElementById("studentParentPhone").value = student.ParentNumber || "";
  document.getElementById("studentHomePhone").value = student.LocalNumber || "";

  populateClassSelect("studentClass", student.classId, { includePlaceholder: true });
  openPopup("editStudentPopup");
}

function handleEditStudent(event) {
  event.preventDefault();
  if (!activeStudentId) return;

  const student = state.students.find((item) => (item._id === activeStudentId || item.id === activeStudentId));
  if (!student) return;

  const firstNameInput = document.getElementById("studentFirstName").value.trim();
  const lastNameInput = document.getElementById("studentLastName").value.trim();
  const birthdayInput = document.getElementById("studentBirth").value.trim();
  const nationalIdInput = document.getElementById("studentNationalId").value.trim();
  const numberInput = document.getElementById("number").value.trim();
  const parentPhoneInput = document.getElementById("studentParentPhone").value.trim();
  const homePhoneInput = document.getElementById("studentHomePhone").value.trim();
  const classIdInput = document.getElementById("studentClass").value;

  if (!firstNameInput || !lastNameInput) {
    alert("نام و نام خانوادگی نباید خالی باشد.");
    return;
  }

  const classObj = state.classes.find(cls => cls._id === classIdInput);
  if (!classObj) {
    alert("کلاس انتخاب‌شده نامعتبر است.");
    return;
  }

  const body = {
    id: activeStudentId,
    firstname: firstNameInput,
    lastname: lastNameInput,
    birthday: birthdayInput,
    nationalid: nationalIdInput,
    number: numberInput,
    ParentNumber: parentPhoneInput,
    LocalNumber: homePhoneInput,
    classId: classObj._id
  };

  fetch("/student/update", {
    method: "PATCH",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body)
  })
    .then(res => res.json())
    .then(data => {
      console.log("STUDENT UPDATE RESPONSE:", data);
      closePopup("editStudentPopup");
      activeStudentId = null;
      renderPage("students");
    })
    .catch(err => {
      console.error("STUDENT UPDATE ERROR:", err);
      alert("خطا در ذخیره تغییرات. لطفاً دوباره تلاش کنید.");
    });
}

function openDeletePopup(entity, id) {
  deleteContext.entity = entity;
  deleteContext.id = id;

  const title = document.getElementById("deleteTitle");
  const message = document.getElementById("deleteMessage");

  if (entity === "student") {
    title.textContent = "حذف دانش‌آموز";
    message.textContent = "با حذف این دانش‌آموز تمام سوابق حضور و غیاب او نیز حذف می‌شود.";
  } else if (entity === "card") {
    title.textContent = "حذف کارت";
    message.textContent = "آیا مطمئن هستید که می‌خواهید این کارت را حذف کنید؟";
  } else {
    title.textContent = "حذف کلاس";
    message.textContent = "با حذف این کلاس تمام دانش‌آموزان مرتبط نیز حذف خواهند شد.";
  }

  openPopup("deletePopup");
}

async function handleDeleteConfirm() {
  if (!deleteContext.entity || !deleteContext.id) {
    closePopup("deletePopup");
    return;
  }

  const { entity, id } = deleteContext;

  // ۱. حذف ادمین (فیکس شده)
  if (entity === "admin") {
    try {
      const result = await deleteAdmin(id);
      if (result.code === 200) {
        // آپدیت استیت محلی برای حذف فوری از جدول
        state.admins = state.admins.filter(admin => admin._id !== id);
        closePopup("deletePopup");
        renderAdmins(); // رندر مجدد لیست ادمین‌ها
      } else {
        alert(result.message || "خطا در حذف مدیر");
      }
    } catch (err) {
      console.error("Error deleting admin:", err);
    }
    return;
  }

  // ۲. حذف دانش‌آموز
  if (entity === "student") {
    deleteStu(id).then(() => {
      closePopup("deletePopup");
      renderPage("students");
    });
    return;
  }

  // ۳. حذف کلاس
  if (entity === "class") {
    deleteClass(id).then(() => {
      closePopup("deletePopup");
      renderPage("classes");
    });
    return;
  }

  // ۴. حذف کارت
  if (entity === "card") {
    deleteCard(id).then((result) => {
      if (result.code === 200) {
        closePopup("deletePopup");
        renderPage("cards");
      }
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
    const fullName = `${student.firstname || ""} ${student.lastname || ""}`;
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

let cardStudentSearchFilter = "";
let cardSearchableSelectOpen = false;

function openAddCardPopup() {
  const form = document.getElementById("addCardForm");
  form.reset();
  cardStudentSearchFilter = "";
  cardSearchableSelectOpen = false;
  
  const searchInput = document.getElementById("addCardStudentSearch");
  const hiddenInput = document.getElementById("addCardStudent");
  const dropdown = document.getElementById("addCardStudentDropdown");
  
  if (searchInput) {
    searchInput.value = "";
    searchInput.addEventListener("focus", handleCardSelectFocus);
    searchInput.addEventListener("input", handleCardSelectSearch);
    searchInput.addEventListener("blur", () => {
      // Delay to allow click on dropdown item
      setTimeout(() => {
        cardSearchableSelectOpen = false;
        if (dropdown) dropdown.style.display = "none";
      }, 200);
    });
  }
  
  if (hiddenInput) {
    hiddenInput.value = "";
  }
  
  updateCardStudentDropdown();
  openPopup("addCardPopup");
}

function handleCardSelectFocus() {
  cardSearchableSelectOpen = true;
  const dropdown = document.getElementById("addCardStudentDropdown");
  if (dropdown) {
    dropdown.style.display = "block";
  }
}

function handleCardSelectSearch(e) {
  cardStudentSearchFilter = e.target.value.trim().toLowerCase();
  updateCardStudentDropdown();
}

function updateCardStudentDropdown() {
  const filteredStudents = state.students.filter(student => {
    if (!cardStudentSearchFilter) return true;
    const fullName = `${student.firstname || ""} ${student.lastname || ""}`.toLowerCase();
    const className = getClassName(student.classId).toLowerCase();
    return fullName.includes(cardStudentSearchFilter) || className.includes(cardStudentSearchFilter);
  });
  
  const dropdown = document.getElementById("addCardStudentDropdown");
  if (!dropdown) return;
  
  if (filteredStudents.length === 0) {
    dropdown.innerHTML = '<div class="searchable-select-item disabled">هیچ دانش‌آموزی یافت نشد</div>';
    return;
  }
  
  const itemsHtml = filteredStudents
    .map(student => {
      const displayText = `${student.firstname} ${student.lastname} - ${getClassName(student.classId)}`;
      return `<div class="searchable-select-item" data-value="${student._id}" data-text="${displayText}">${displayText}</div>`;
    })
    .join("");
  
  dropdown.innerHTML = itemsHtml;
  
  // Add click handlers
  dropdown.querySelectorAll(".searchable-select-item:not(.disabled)").forEach(item => {
    item.addEventListener("click", (e) => {
      const value = item.dataset.value;
      const text = item.dataset.text;
      const searchInput = document.getElementById("addCardStudentSearch");
      const hiddenInput = document.getElementById("addCardStudent");
      
      if (searchInput) searchInput.value = text;
      if (hiddenInput) hiddenInput.value = value;
      
      cardSearchableSelectOpen = false;
      dropdown.style.display = "none";
    });
  });
}

function handleAddCard(event) {
  event.preventDefault();
  
  const uid = document.getElementById("addCardUid").value.trim();
  const ownerId = document.getElementById("addCardStudent").value;
  
  if (!uid) {
    alert("لطفاً UID کارت را وارد کنید.");
    return;
  }
  
  if (!ownerId) {
    alert("لطفاً دانش‌آموز را انتخاب کنید.");
    return;
  }
  
  addCard(uid, ownerId).then((result) => {
    if (result.code === 200) {
      closePopup("addCardPopup");
      renderPage("cards");
    } else {
      alert(result.message || "خطا در افزودن کارت");
    }
  });
}



/* --------------------------------------------------
 * Admin Management Logic
 * -------------------------------------------------- */

async function renderAdmins() {
  uiState.activePage = "admins";
  const admins = await readAdmins();
  state.admins = admins || [];

  content.innerHTML = `
    <div class="page-header">
      <div>
        <h2 class="page-title">کنترل مدیران سیستم</h2>
        <p class="page-subtitle">لیست افراد دارای دسترسی به پنل مدیریت</p>
      </div>
      <button class="btn primary add-btn" onclick="openPopup('addAdminPopup')">
        <span>+ مدیر جدید</span>
      </button>
    </div>

    <div class="table-wrap">
      <table class="class-table">
        <thead>
          <tr>
            <th>نام و نام خانوادگی</th>
            <th>شماره</th>
            <th>نقش</th>
            <th>عملیات</th>
          </tr>
        </thead>
        <tbody>
          ${state.admins.map(admin => `
            <tr>
              <td>${admin.firstname} ${admin.lastname}</td>
              <td style="font-family: monospace;">${admin.number}</td>
              <td><span class="status-badge present">${admin.role}</span></td>
              <td>
                <button class="icon-btn delete-btn" onclick="confirmDelete('admin', '${admin._id}')">
                  <img src="icons/trash.svg" alt="حذف">
                </button>
              </td>
            </tr>
          `).join('')}
        </tbody>
      </table>
    </div>
  `;
}



async function renderAdmins() {
  uiState.activePage = "admins";
  updateMenuState("admins");
  
  // لود دوباره برای اطمینان از تازگی دیتا
  const admins = await readAdmins();
  state.admins = admins || [];

  content.innerHTML = `
    <section class="page-header">
      <div>
        <h2 class="page-title">مدیریت مدیران</h2>
        <p class="page-subtitle">لیست افراد دارای دسترسی به پنل</p>
      </div>
      <button class="btn primary add-btn" onclick="openPopup('addAdminPopup')">
        <span>+ ادمین جدید</span>
      </button>
    </section>

    <div class="table-wrap">
      <table class="class-table">
        <thead>
          <tr>
            <th>نام و نام خانوادگی</th>
            <th>شماره / شناسه</th>
            <th>نقش</th>
            <th>عملیات</th>
          </tr>
        </thead>
        <tbody>
          ${state.admins.map(admin => `
            <tr>
              <td>${admin.firstname} ${admin.lastname}</td>
              <td dir="ltr">${admin.number}</td>
              <td><span class="status-badge present">${admin.role || 'admin'}</span></td>
              <td>
                <button class="icon-btn delete-btn" onclick="openAdminDeletePopup('${admin._id}')">
                  <img src="icons/trash.svg" alt="حذف">
                </button>
              </td>
            </tr>
          `).join('')}
        </tbody>
      </table>
    </div>
  `;
}


function confirmDelete(entity, id) {
  deleteContext.entity = entity;
  deleteContext.id = id;
  
  // باز کردن پاپ‌آپ حذف (مطابق ID شما در HTML)
  const popup = document.getElementById("deletePopup");
  if (popup) popup.classList.add("active");
}


let adminIdToDelete = null; // متغیر موقت برای ذخیره ID ادمین

// ۱. تابع باز کردن پاپ‌آپ اختصاصی
function openAdminDeletePopup(id) {
    adminIdToDelete = id; // ذخیره ID
    document.getElementById("deleteAdminPopup").classList.add("active");
}

// ۲. هندل کردن کلیک روی دکمه حذف نهایی در پاپ‌آپ جدید
document.getElementById("confirmAdminDeleteBtn").addEventListener("click", async () => {
    if (!adminIdToDelete) return;

    try {
        // فراخوانی API حذف که در ابتدای کدتان داشتید
        const res = await deleteAdmin(adminIdToDelete);

        if (res.code === 200 || res.message.includes("موفقیت")) {
            // ۱. آپدیت استیت (حذف از لیست محلی)
            state.admins = state.admins.filter(a => a._id !== adminIdToDelete);
            
            // ۲. بستن پاپ‌آپ
            document.getElementById("deleteAdminPopup").classList.remove("active");
            
            // ۳. رندر مجدد جدول ادمین‌ها
            renderAdmins();
            
            adminIdToDelete = null; // ریست کردن متغیر
        } else {
            alert(res.message || "خطا در حذف ادمین از سرور");
        }
    } catch (err) {
      console.error("Network Error:", err);
      alert("خطا در برقراری ارتباط با سرور");
    }
});
init();