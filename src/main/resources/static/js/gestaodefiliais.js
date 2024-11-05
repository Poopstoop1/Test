const sidebar = document.querySelector(".sidebar");
const sidebarOpenBtn = document.querySelector("#sidebar-open");
const sidebarCloseBtn = document.querySelector("#sidebar-close");
const sidebarLockBtn = document.querySelector("#lock-icon");
const sidebarClosedIcon = document.querySelector("#sidebar-closed-icon");

// Obtém os elementos popup
const popup = document.getElementById("popup");
const openPopup = document.getElementById("openPopup");
const closePopup = document.getElementById("closePopup");



const toggleLock = () => {
  sidebar.classList.toggle("locked");
  if (!sidebar.classList.contains("locked")) {
    sidebar.classList.add("hoverable");
    sidebarLockBtn.classList.replace("bx-lock-alt", "bx-lock-open-alt");
  } else {
    sidebar.classList.remove("hoverable");
    sidebarLockBtn.classList.replace("bx-lock-open-alt", "bx-lock-alt");
  }
};

const hideSidebar = () => {
  if (sidebar.classList.contains("hoverable")) {
    sidebar.classList.add("close");
    sidebarCloseBtn.style.display = "block";
    sidebarClosedIcon.style.display = "none";
  }
};

const showSidebar = () => {
  if (sidebar.classList.contains("hoverable")) {
    sidebar.classList.remove("close");
    sidebarCloseBtn.style.display = "none";
    sidebarClosedIcon.style.display = "block";
  }
};

const toggleSidebar = () => {
  sidebar.classList.toggle("close");
  
  if (sidebar.classList.contains("close")) {
    sidebarCloseBtn.style.display = "block";
    sidebarClosedIcon.style.display = "none";
  } else {
    sidebarCloseBtn.style.display = "none";
    sidebarClosedIcon.style.display = "block";
  }
};

sidebarLockBtn.addEventListener("click", toggleLock);
sidebar.addEventListener("mouseleave", hideSidebar);
sidebar.addEventListener("mouseenter", showSidebar);
sidebarCloseBtn.addEventListener("click", toggleSidebar);
sidebarClosedIcon.addEventListener("click", toggleSidebar);

if (window.innerWidth < 800) {
  sidebar.classList.add("close");
  sidebar.classList.remove("locked");
  sidebar.classList.remove("hoverable");
}

// Abre o pop-up quando o botão é clicado
openPopup.addEventListener("click", () => {
  popup.style.display = "block";
});

// Fecha o pop-up ao clicar no "X"
closePopup.addEventListener("click", () => {
  popup.style.display = "none";
});

// Fecha o pop-up ao clicar fora dele
window.addEventListener("click", (event) => {
  if (event.target == popup) {
      popup.style.display = "none";
  }
});

function openEditPopup(element) {
    // Recupera os dados a partir dos atributos data- do elemento
    const cnpj = element.getAttribute('data-cnpj');
    const nome = element.getAttribute('data-nome');
    const razaosocial = element.getAttribute('data-razaosocial');

    // Define os valores nos campos do formulário
    document.getElementById('editCnpj').value = cnpj;
    document.getElementById('editNome').value = nome;
    document.getElementById('editRazaoSocial').value = razaosocial;

    // Abre o pop-up de edição
    document.getElementById('popupEdit').style.display = 'block';
}
// Função para fechar o pop-up de edição
document.getElementById("closePopupEdit").onclick = function() {
    document.getElementById("popupEdit").style.display = "none";
}
// Fechar o pop-up de edição se clicar fora dele
window.onclick = function(event) {
    const popupEdit = document.getElementById("popupEdit");
    if (event.target == popupEdit) {
        popupEdit.style.display = "none";
    }
}