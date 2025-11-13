/* --------------------------------------------------
 * Admin panel state and initialization
 * -------------------------------------------------- */
const state = {
  classes: [
    { id: "class-7", name: "هفتم" },
    { id: "class-8", name: "هشتم" },
    { id: "class-9", name: "نهم" },
    { id: "class-10", name: "دهم ریاضی" },
    { id: "class-11", name: "یازدهم تجربی" }
  ],
  students: [
    {
      id: "stu-1",
      firstName: "امیرحسین",
      lastName: "اخضری",
      classId: "class-8",
      birthDate: "1403/05/12",
      nationalId: "0056732941",
      studentNumber: "123456",
      parentPhone: "۰۹۱۲۳۴۵۶۷۸۹",
      homePhone: "۰۷۱۵۲۳۴۵۶۷۸",
      photo: "https://i.pravatar.cc/150?u=1"
    },
    {
      id: "stu-2",
      firstName: "علی",
      lastName: "رضایی",
      classId: "class-9",
      birthDate: "1403/02/03",
      nationalId: "0076543210",
      studentNumber: "789123",
      parentPhone: "۰۹۰۱۲۳۴۵۶۷۸",
      homePhone: "۰۲۱۳۳۳۳۳۳۳",
      photo: "https://i.pravatar.cc/150?u=2"
    },
    {
      id: "stu-3",
      firstName: "محمد",
      lastName: "حسینی",
      classId: "class-7",
      birthDate: "1402/11/22",
      nationalId: "0012345678",
      studentNumber: "456987",
      parentPhone: "۰۹۳۵۴۳۲۱۰۹۸",
      homePhone: "۰۲۶۴۲۱۰۰۰۰",
      photo: "https://i.pravatar.cc/150?u=3"
    }
  ],
  attendance: [
    { id: "att-1", studentId: "stu-1", status: "حاضر", checkIn: "07:55", date: "1404/08/19" },
    { id: "att-2", studentId: "stu-2", status: "دیر اومده", checkIn: "08:20", date: "1404/08/19" },
    { id: "att-3", studentId: "stu-3", status: "غایب", checkIn: "—", date: "1404/08/19" }
  ]
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

  const dayPicker = document.getElementById("dayPicker");
  dayPicker.addEventListener("click", (event) => {
    const dayButton = event.target.closest(".day");
    if (dayButton) {
      dayButton.classList.toggle("active");
    }
  });

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
  const rows = state.classes
    .map(
      (cls, index) => `
        <tr data-class-id="${cls.id}">
          <td>${index + 1}</td>
          <td>${cls.name}</td>
          <td class="actions">
            <button type="button" class="icon-btn edit-btn" data-action="edit-class" data-id="${cls.id}">
              <img src="icons/edit.svg" alt="edit" />
            </button>
            <button type="button" class="icon-btn delete-btn" data-action="delete-item" data-entity="class" data-id="${cls.id}">
              <img src="icons/delete.svg" alt="delete" />
            </button>
          </td>
        </tr>
      `
    )
    .join("");

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
        <tbody>${rows}</tbody>
      </table>
    </div>
  `;
}

function renderStudents() {
  const rows = getFilteredStudents()
    .map(
      (student, index) => `
        <tr data-student-id="${student.id}">
          <td>${index + 1}</td>
          <td><img src="${student.photo}" alt="student" class="profile-pic" /></td>
          <td>${getClassName(student.classId)}</td>
          <td>${student.firstName}</td>
          <td>${student.lastName}</td>
          <td class="actions">
            <button type="button" class="icon-btn edit-btn" data-action="edit-student" data-id="${student.id}">
              <img src="icons/edit.svg" alt="edit" />
            </button>
            <button type="button" class="icon-btn delete-btn" data-action="delete-item" data-entity="student" data-id="${student.id}">
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
        <tbody>${rows}</tbody>
      </table>
    </div>
  `;
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
          <td><img src="${student.photo}" alt="student" class="profile-pic" /></td>
          <td>${student.firstName} ${student.lastName}</td>
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

  state.classes.push({ id: generateId("class"), name });
  input.value = "";
  closePopup("addClassPopup");
  renderPage("classes");
}

function openEditClass(id) {
  const cls = state.classes.find((item) => item.id === id);
  if (!cls) return;
  activeClassId = id;
  document.getElementById("editClassInput").value = cls.name;
  openPopup("editClassPopup");
}

function handleEditClass(event) {
  event.preventDefault();
  if (!activeClassId) return;
  const cls = state.classes.find((item) => item.id === activeClassId);
  if (!cls) return;

  cls.name = document.getElementById("editClassInput").value.trim();
  activeClassId = null;
  closePopup("editClassPopup");
  renderPage("classes");
}

function openAddStudentPopup() {
  const form = document.getElementById("addStudentForm");
  form.reset();
  document.getElementById("addStudentPhotoPreview").src = "https://i.pravatar.cc/150?u=new";
  populateClassSelect("addStudentClass", "", { includePlaceholder: true });
  openPopup("addStudentPopup");
}

function handleAddStudent(event) {
  event.preventDefault();

  const newStudent = {
    id: generateId("stu"),
    firstName: document.getElementById("addFirstName").value.trim(),
    lastName: document.getElementById("addLastName").value.trim(),
    classId: document.getElementById("addStudentClass").value,
    birthDate: document.getElementById("addBirth").value.trim(),
    nationalId: document.getElementById("addNationalId").value.trim(),
    studentNumber: document.getElementById("addStudentNumber").value.trim(),
    parentPhone: document.getElementById("addParentPhone").value.trim(),
    homePhone: document.getElementById("addHomePhone").value.trim(),
    photo: document.getElementById("addStudentPhotoPreview").src
  };

  if (!newStudent.firstName || !newStudent.lastName) return;

  state.students.push(newStudent);
  state.attendance.push({
    id: generateId("att"),
    studentId: newStudent.id,
    status: "حاضر",
    checkIn: "—",
    date: "1404/08/19"
  });

  closePopup("addStudentPopup");
  renderPage("students");
}

function openEditStudentPopup(id) {
  const student = state.students.find((item) => item.id === id);
  if (!student) return;
  activeStudentId = id;

  document.getElementById("studentPhotoPreview").src = student.photo;
  document.getElementById("studentFirstName").value = student.firstName;
  document.getElementById("studentLastName").value = student.lastName;
  document.getElementById("studentBirth").value = student.birthDate;
  document.getElementById("studentNationalId").value = student.nationalId || "";
  document.getElementById("studentNumber").value = student.studentNumber || "";
  document.getElementById("studentParentPhone").value = student.parentPhone || "";
  document.getElementById("studentHomePhone").value = student.homePhone || "";

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
  student.birthDate = document.getElementById("studentBirth").value.trim();
  student.nationalId = document.getElementById("studentNationalId").value.trim();
  student.studentNumber = document.getElementById("studentNumber").value.trim();
  student.parentPhone = document.getElementById("studentParentPhone").value.trim();
  student.homePhone = document.getElementById("studentHomePhone").value.trim();
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

  if (deleteContext.entity === "student") {
    state.students = state.students.filter((student) => student.id !== deleteContext.id);
    state.attendance = state.attendance.filter((record) => record.studentId !== deleteContext.id);
  } else if (deleteContext.entity === "class") {
    state.classes = state.classes.filter((cls) => cls.id !== deleteContext.id);
    const removedStudents = new Set(
      state.students.filter((student) => student.classId === deleteContext.id).map((student) => student.id)
    );
    state.students = state.students.filter((student) => student.classId !== deleteContext.id);
    state.attendance = state.attendance.filter((record) => !removedStudents.has(record.studentId));

    if (uiState.studentClassFilter === deleteContext.id) {
      uiState.studentClassFilter = "all";
    }
    if (uiState.attendanceFilters.classId === deleteContext.id) {
      uiState.attendanceFilters.classId = "all";
    }
  }

  deleteContext.entity = null;
  deleteContext.id = null;

  closePopup("deletePopup");
  renderPage(uiState.activePage);
}

function handleStatusChange(attendanceId, status) {
  const record = state.attendance.find((item) => item.id === attendanceId);
  if (!record) return;
  record.status = status;
  renderPage("attendance");
}

function openAttendanceDetail(attendanceId) {
  const record = state.attendance.find((item) => item.id === attendanceId);
  if (!record) return;
  const student = getStudentById(record.studentId);
  if (!student) return;

  document.getElementById("detailName").textContent = `${student.firstName} ${student.lastName}`;
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
    .map((cls) => `<option value="${cls.id}"${cls.id === selectedId ? " selected" : ""}>${cls.name}</option>`)
    .join("");

  return html;
}

function populateClassSelect(selectId, selectedId = "", options = {}) {
  const select = document.getElementById(selectId);
  if (!select) return;
  select.innerHTML = buildClassOptions(selectedId, options);
}

function getFilteredStudents() {
  if (uiState.studentClassFilter === "all") {
    return state.students;
  }
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
  return state.students.find((student) => student.id === id);
}

function getClassName(classId) {
  return state.classes.find((cls) => cls.id === classId)?.name || "—";
}

function countAttendanceByStatus(status) {
  return state.attendance.filter((record) => record.status === status).length;
}

function generateId(prefix) {
  return `${prefix}-${Math.random().toString(36).slice(2, 9)}`;
}

init();
