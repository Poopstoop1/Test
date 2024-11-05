const sidebar = document.querySelector(".sidebar");
const sidebarOpenBtn = document.querySelector("#sidebar-open");
const sidebarCloseBtn = document.querySelector("#sidebar-close");
const sidebarLockBtn = document.querySelector("#lock-icon");
const sidebarClosedIcon = document.querySelector("#sidebar-closed-icon");

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

var metaTotal = /*[[${metaTotal}]]*/ 0;

var ctx = document.getElementById('performanceChart').getContext('2d');
            var performanceChart = new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: ['Base', 'Sul','Crescer'],  // Filiais
                    datasets: [
                        {
                            label: 'Meta',
                            data: [30, 40, 50, 9],  // Premiações por filial para o grupo 'Base'
                            backgroundColor: 'rgba(255, 99, 132, 0.2)',
                            borderColor: 'rgba(255, 99, 132, 1)',
                            borderWidth: 1
                        },
                        {
                            label: 'Realizado',
                            data: [45, 10],  // Premiações por filial para o grupo 'Crescer'
                            backgroundColor: 'rgba(54, 162, 235, 0.2)',
                            borderColor: 'rgba(54, 162, 235, 1)',
                            borderWidth: 1
                        }
                    ]
                },
                options: {
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    },
                    plugins: {
                        title: {
                            display: true,
                            text: 'Desempenho por Filial e Grupo (Valor das Premiações)'
                        }
                    }
                }
            });
